package com.github.chen0040.desktop.consts;

public class HostConstants {
    public static final String host = "http://localhost:8080";

    public static String getUrl(String path) {
        return host + path;
    }
}
