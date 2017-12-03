package com.github.chen0040.springclient;

import com.github.chen0040.springclient.service.ScopeService;
import com.github.chen0040.springclient.service.UserService;
import org.testng.annotations.Test;

public class LoginUnitTest {
    @Test
    public void testLogin(){
        String username = "admin";
        String password = "admin";
        UserService.getSingleton().login(username, password, (success)->{
            if(success){
                System.out.println("user successfully login");
            }else{
                Exception ex = ScopeService.getSingleton().getLastException();
                if(ex != null) ex.printStackTrace();
            }
        });

    }
}
