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
public class UrlService {
    private static UrlService singleton = null;
    private String domainName = "localhost:8080";
    private String protocolName = "http";
    
    public String getUrlAddress(String relativeAddr){
        return protocolName+":"+"//"+domainName+"/"+relativeAddr;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getProtocolName() {
        return protocolName;
    }

    public void setProtocolName(String protocolName) {
        this.protocolName = protocolName;
    }


    
  
    
  
    
    public static UrlService getSingleton(){
        if(singleton==null){
            singleton = new UrlService();
        }
        return singleton;
    }
}
