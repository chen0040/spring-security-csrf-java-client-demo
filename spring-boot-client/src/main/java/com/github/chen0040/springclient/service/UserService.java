/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.chen0040.springclient.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.github.chen0040.springclient.utils.Cookie;
import com.github.chen0040.springclient.utils.CookieContainer;
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
public class UserService {
    private static UserService singleton = null;
    private String csrfToken;
    private String csrfParameterName;
    private String csrfHeader;
    private Cookie jSessionCookie;
    private final static Logger logger = LoggerFactory.getLogger(UserService.class);
    
    private String getPingAjaxUrl(){
        return UrlService.getSingleton().getUrlAddress("ping-web-api");
    }
    
    private String getLoginUrl(){
        return UrlService.getSingleton().getUrlAddress("login");
    }
    
     private String getLogoutUrl(){
        return UrlService.getSingleton().getUrlAddress("logout");
    }
    
    public String getToken(){
        return csrfToken;
    }
    
    public Cookie getJSessionCookie(){
        return jSessionCookie;
    }
    
    public static UserService getSingleton(){
        if(singleton == null){
            singleton = new UserService();
        }
        return singleton;
    }
    
    private void readCsrfToken(String content){
        Map<String, String> obj = JSON.parseObject(content, new TypeReference<Map<String, String>>(){});
        this.csrfToken = obj.get("_csrf.token");
        this.csrfHeader = obj.get("_csrf.header");
        this.csrfParameterName = obj.get("_csrf.parameterName");

        logger.info("csrfToken: {}", csrfToken);
    }
    
    private void readJSessionCookie(CookieContainer cc){
        if(cc.containsKey("JSESSIONID")){
            jSessionCookie = cc.get("JSESSIONID");
        }
        if(cc.containsKey("XSRF-TOKEN")) {
            csrfToken = cc.get("XSRF-TOKEN").getValue();
        }
    }
    
    private void ping(Consumer<Boolean> onPingCompleted){
         HttpService.getSingleton().getAsync(getLoginUrl(), null,
                        response -> {
                            
                    if(response.getException()==null){
                        //readCsrfToken(response.getContent());
                        logger.info("cookie: {}", response.getCookie());
                        readJSessionCookie(response.getCookie());
                        onPingCompleted.accept(Boolean.TRUE);
                    }
                    else{
                        response.getException().printStackTrace();
                        ScopeService.getSingleton().setLastException(response.getException());
                        onPingCompleted.accept(Boolean.FALSE);
                    }
                });
    }
    
    public void login(String username, String password, Consumer<Boolean> onLoginCompleted){
        ping((success)->{
            if(success){
                _login(username, password, onLoginCompleted);
            }
            else{
                onLoginCompleted.accept(Boolean.FALSE);
            }
        });
    }
    
    public void logout(Consumer<Boolean> onLogoutCompleted){
        Map<String, String> authparams =new HashMap<String, String>();
        List<Cookie> cookies = new ArrayList<Cookie>();
       
        this.addSecurityInfo(authparams, cookies);
        HttpService.getSingleton().postAsync(getLogoutUrl(), authparams, cookies, (response)->{
            if(response.getException() != null){
                if(onLogoutCompleted != null) onLogoutCompleted.accept(Boolean.FALSE);
            }else {
                if(onLogoutCompleted != null) onLogoutCompleted.accept(Boolean.TRUE);
            }
        });
        
    }
    
    public void addSecurityInfo(Map<String, String> authparams, List<Cookie> cookies){
        authparams.put("_csrf", this.csrfToken);
        if(jSessionCookie != null) {
            cookies.add(jSessionCookie);
        }
        cookies.add(new Cookie("XSRF-TOKEN", csrfToken));
    }
    
    private void _login(String username, String password, Consumer<Boolean> onLoginCompleted){
        Map<String, String> authparams =new HashMap<String, String>();
        authparams.put("username", username);
        authparams.put("password", password);
        authparams.put("ajax", "true");
       
        List<Cookie> cookies = new ArrayList<Cookie>();
       
        this.addSecurityInfo(authparams, cookies);
        

        HttpService.getSingleton().postAsync(getLoginUrl(), authparams, cookies, (response)->{
            if(response.getException() != null){
                onLoginCompleted.accept(Boolean.FALSE);
            }else if(response.getContent().startsWith("SAVVY-TRANSCRIBER-AJAX-LOGIN-SUCCESS")){
                
                String[] parts = response.getContent().split(";");
                this.csrfToken = parts[1];
                this.readJSessionCookie(response.getCookie());
                onLoginCompleted.accept(Boolean.TRUE);
            }else {
                ScopeService.getSingleton().setLastException(new Exception(response.getContent()));
                onLoginCompleted.accept(Boolean.FALSE);
            }
        });
    }
}

