package com.bookWise.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DefaultController {

    @RequestMapping("/")
    public String defaultPage() {
        return "redirect:/login";
    }

    @RequestMapping("/login")
    public String showLoginPage() {
        return "Login"; // This should match the name of your JSP file without the .jsp suffix
    }

    @GetMapping("/access-denied")
    public String accessDeniedPage() {
        return "Login";  // return a JSP/Thymeleaf/HTML page
    }

}