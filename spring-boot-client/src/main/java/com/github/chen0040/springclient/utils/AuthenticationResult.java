package com.github.chen0040.springclient.utils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticationResult {
    private boolean authenticated;
    private String token;
    private String error;
}
