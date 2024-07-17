package com.bookWise.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class BaseController {

    @RequestMapping("/Login")
    public String userLoginPage(){
        return "Login";
    }

    @RequestMapping("/userSignUpPage")
    public String userSignUp(){
        return "UserSignUp";
    }
}
