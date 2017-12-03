package com.github.chen0040.desktop.services;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.chen0040.desktop.consts.HostConstants;
import com.github.chen0040.desktop.models.LoginObj;
import com.github.chen0040.desktop.models.SpringIdentity;
import com.github.chen0040.desktop.utils.HttpClient;
import com.github.chen0040.desktop.utils.StringUtils;
import com.google.common.util.concurrent.*;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class AccountService {
    private static AccountService instance;
    private SpringIdentity account;
    private static final Logger logger = LoggerFactory.getLogger(AccountService.class);

    private ListeningExecutorService executor = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));

    public String getToken() {
        return account.getTokenInfo().get("_csrf.token");
    }

    public static synchronized AccountService getInstance(){
        if(instance == null) {
            instance = new AccountService();
        }
        return instance;
    }

    public boolean isAuthenticated() {
        return account != null;
    }


    public void authenticate(String username, String password, Consumer<SpringIdentity> callback) {

        final LoginObj loginObj = new LoginObj();
        loginObj.setUsername(username);
        loginObj.setPassword(password);

        ListenableFuture<SpringIdentity> future = executor.submit(()->{

            String response = HttpClient.get(HostConstants.getUrl("/ping-web-api"));
            Map<String, String> tokenInfo = JSON.parseObject(response, new TypeReference<Map<String, String>>() {});

            String token = tokenInfo.get("_csrf.token").trim();
            String session = tokenInfo.get("sessionId").trim();

            logger.info("_csrf.token: {}", token);

            Map<String, String> headers = new HashMap<>();
            headers.put("XSRF-TOKEN", token);
            headers.put("Content-Type", "application/json");
            headers.put("Cookie", "JSESSIONID=" + session);

            response = HttpClient.post(HostConstants.getUrl("/login-web-api"), JSON.toJSONString(loginObj, SerializerFeature.BrowserCompatible), headers);

            logger.info(response);

            SpringIdentity account = JSON.parseObject(response, SpringIdentity.class);
            if(account.isAuthenticated()){
                this.account = account;
            }
            return account;
        });

        Futures.addCallback(future, new FutureCallback<SpringIdentity>() {
            @Override
            public void onSuccess(SpringIdentity account) {
                callback.accept(account);
            }

            @Override
            public void onFailure(Throwable throwable) {
                logger.error("Failed to authenticate with remote server", throwable);
            }
        });
    }
}
