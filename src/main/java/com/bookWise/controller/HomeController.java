package com.bookWise.controller;

import com.bookWise.SecurityConfig.loginUserConfig.BookWiseLoginUser;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/home")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String home(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        BookWiseLoginUser user = (BookWiseLoginUser) authentication.getPrincipal();
        model.addAttribute("userName", user.getUsername()); // Add the username to the model
        model.addAttribute("userEmail", user.getUserEmail()); // Add the username to the model
        model.addAttribute("userId", user.getUserId()); // Add the username to the model
        return "Home"; // Return the home view
    }

    @GetMapping("/sellerHome")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String sellerHome(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        BookWiseLoginUser user = (BookWiseLoginUser) authentication.getPrincipal();
        model.addAttribute("userEmail", user.getUserEmail()); // Add the username to the model
        model.addAttribute("userName", user.getUsername()); // Add the username to the model
        model.addAttribute("userId", user.getUserId()); // Add the username to the model
        return "sellerHome";
    }

}