package com.bookWise.bookEmailVerify.controller;

import com.bookWise.bookEmailVerify.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api/emailVerification")
public class EmailVerificationRestController {

    @Autowired
    private EmailService emailService;

    @GetMapping("/verify-email")
    public String verifyEmail(@RequestParam("token") String token, Model model) {
        System.out.println("VERIFY EMAIL HIT: " + token);
        return emailService.verifyEmail(token, model);
    }

}
