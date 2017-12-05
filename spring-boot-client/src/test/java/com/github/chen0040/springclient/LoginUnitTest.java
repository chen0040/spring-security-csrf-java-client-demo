package com.github.chen0040.springclient;

import com.github.chen0040.springclient.service.SpringBootClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class LoginUnitTest {

    private static final Logger logger = LoggerFactory.getLogger(LoginUnitTest.class);

    @Test
    public void testLoginSuccess(){
        String username = "admin";
        String password = "admin";
        SpringBootClient.getSingleton().setBaseUrl("http://localhost:8080");
        SpringBootClient.getSingleton().login(username, password, (authenticationResult)->{

            if(authenticationResult.isAuthenticated()){
                System.out.println("user successfully login");
            }
            assertThat(authenticationResult.isAuthenticated()).isTrue();
        });

    }

    @Test
    public void testLoginFailure(){
        String username = "admin";
        String password = "admin-wrong-password";
        SpringBootClient.getSingleton().setBaseUrl("http://localhost:8080");
        SpringBootClient.getSingleton().login(username, password, (authenticationResult)->{

            if(authenticationResult.isAuthenticated()){
                System.out.println("user successfully login");
            } else {
                System.out.println("Error: " + authenticationResult.getError());
            }
            assertThat(authenticationResult.isAuthenticated()).isFalse();
        });

    }
}
