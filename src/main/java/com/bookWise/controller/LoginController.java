// src/main/java/com/bookwise/controller/LoginController.java
package com.bookWise.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {

    @RequestMapping("/")
    public String userLoginPage(){
        return "Login";
    }

    @RequestMapping("/signUp")
    public String userSignUp(){
        return "UserSignUp";
    }
}
