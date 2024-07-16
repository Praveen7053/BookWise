package com.bookWise.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {

    @RequestMapping("/Login")
    public String userLoginPage(){
        return "Login";
    }

    @RequestMapping("/registerNewUser")
    public String userSignUp(){
        return "UserSignUp";
    }
}
