/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.chen0040.springclient.utils;

/**
 *
 * @author chen0469
 */
public class HttpResult {

    private final Exception exception;
    private final String content;
    private final int responseCode;

    public CookieContainer getCookie() {
        return cookie;
    }
    
    public int getResponseCode(){
        return responseCode;
    }
    
    private CookieContainer cookie = new CookieContainer();
    
    public HttpResult(String content, String cookies, int responseCode, Exception exception){
        this.content = content;
        this.exception = exception;
        this.responseCode = responseCode;
        this.cookie=new CookieContainer(cookies);
    }

    public Exception getException() {
        return exception;
    }

    public String getContent() {
        return content;
    }
}
