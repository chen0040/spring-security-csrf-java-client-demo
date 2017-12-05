/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.chen0040.springclient.service;

import com.github.chen0040.springclient.utils.*;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 *
 * @author chen0469
 */
public class SpringBootClient {


    private static SpringBootClient singleton = null;
    private String csrfToken;
    private String csrfParameterName;
    private String csrfHeader;
    private Cookie jSessionCookie;
    private boolean authenticated;
    private final static Logger logger = LoggerFactory.getLogger(SpringBootClient.class);
    private String baseUrl = "http://localhost:8080";
    private SpringBootHttpClient springBootHttpClient = new SpringBootHttpClient();

    public String getUrlAddress(String relativeAddr){
        return baseUrl +"/"+relativeAddr;
    }
    
    private String getLoginUrl(){
        return getUrlAddress("login");
    }
    
     private String getLogoutUrl(){
        return getUrlAddress("logout");
    }

    public String getBaseUrl() { return baseUrl; }

    public void setBaseUrl(String baseUrl) {
         this.baseUrl = baseUrl;
    }
    
    public String getToken(){
        return csrfToken;
    }
    
    public Cookie getJSessionCookie(){
        return jSessionCookie;
    }
    
    public static SpringBootClient getSingleton(){
        if(singleton == null){
            singleton = new SpringBootClient();
        }
        return singleton;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    private void readJSessionCookie(CookieContainer cc){
        if(cc.containsKey("JSESSIONID")){
            jSessionCookie = cc.get("JSESSIONID");
        }
        if(cc.containsKey("XSRF-TOKEN")) {
            csrfToken = cc.get("XSRF-TOKEN").getValue();
        }
    }
    
    private void ping(Consumer<PingResult> onPingCompleted){
        springBootHttpClient.getAsync(getLoginUrl(), null,
                response -> {

                    PingResult result = new PingResult();

                    if(response.getException()==null){
                        logger.info("cookie: {}", response.getCookie());
                        readJSessionCookie(response.getCookie());
                        result.setSuccess(true);
                    }
                    else{
                        logger.error("ping failed", response.getException());
                        result.setSuccess(false);
                        result.setError(ExceptionUtils.getStackTrace(response.getException()));

                    }
                    onPingCompleted.accept(result);
                });
    }
    
    public void login(String username, String password, Consumer<AuthenticationResult> onLoginCompleted){
        ping((pingResult)->{
            AuthenticationResult result = new AuthenticationResult();
            if(pingResult.isSuccess()){
                _login(username, password, onLoginCompleted);
            }
            else{
                result.setError(pingResult.getError());
                result.setAuthenticated(false);
                onLoginCompleted.accept(result);
            }
        });
    }
    
    public void logout(Consumer<LogoutResult> onLogoutCompleted){
        Map<String, String> authparams =new HashMap<String, String>();
        List<Cookie> cookies = new ArrayList<Cookie>();
       
        this.addSecurityInfo(authparams, cookies);
        springBootHttpClient.postAsync(getLogoutUrl(), authparams, cookies, (response)->{
            LogoutResult result = new LogoutResult();
            if(response.getException() != null){
                result.setError(ExceptionUtils.getStackTrace(response.getException()));
                result.setSuccess(false);
            }else {
                result.setSuccess(true);
            }
            if(onLogoutCompleted != null) onLogoutCompleted.accept(result);
        });
        
    }
    
    public void addSecurityInfo(Map<String, String> authparams, List<Cookie> cookies){
        authparams.put("_csrf", this.csrfToken);
        if(jSessionCookie != null) {
            cookies.add(jSessionCookie);
        }
        cookies.add(new Cookie("XSRF-TOKEN", csrfToken));
    }

    
    private void _login(String username, String password, Consumer<AuthenticationResult> onLoginCompleted){
        Map<String, String> authparams =new HashMap<String, String>();
        authparams.put("username", username);
        authparams.put("password", password);
        authparams.put("ajax", "true");
       
        List<Cookie> cookies = new ArrayList<>();
       
        this.addSecurityInfo(authparams, cookies);
        

        springBootHttpClient.postAsync(getLoginUrl(), authparams, cookies, (response)->{
            AuthenticationResult result = new AuthenticationResult();
            if(response.getException() != null){
                result.setAuthenticated(false);
                result.setError(ExceptionUtils.getStackTrace(response.getException()));
            }else if(response.getContent().startsWith("APP-AJAX-LOGIN-SUCCESS")){
                
                String[] parts = response.getContent().split(";");
                this.csrfToken = parts[1];
                this.authenticated = true;
                this.readJSessionCookie(response.getCookie());
                result.setAuthenticated(true);
                result.setToken(csrfToken);

            }else {
                result.setError(response.getContent());
                result.setAuthenticated(false);
            }

            onLoginCompleted.accept(result);
        });
    }


}

