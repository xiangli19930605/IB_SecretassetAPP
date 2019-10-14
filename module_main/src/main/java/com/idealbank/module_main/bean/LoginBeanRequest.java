package com.idealbank.module_main.bean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginBeanRequest {

    private String username;
    private String password;

    public LoginBeanRequest(String username,String password) {
        this.username = username;
        this.password = password;
    }



}
