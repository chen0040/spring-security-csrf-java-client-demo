/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.chen0040.springclient.service;

/**
 *
 * @author chen0469
 */
public class ScopeService {
    private static ScopeService singleton = null;

    public Exception getLastException() {
        return lastException;
    }

    public void setLastException(Exception lastException) {
        this.lastException = lastException;
    }
    private Exception lastException;
    
    public static ScopeService getSingleton(){
        if(singleton==null){
            singleton=new ScopeService();
        }
        return singleton;
    }
}
