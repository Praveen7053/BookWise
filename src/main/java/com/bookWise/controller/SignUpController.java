package com.bookWise.controller;

import com.bookWise.model.User;
import com.bookWise.dao.BookWiseDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/register")
public class SignUpController {

    @Autowired
    private BookWiseDAO bookWiseDAO;

    @RequestMapping(method = RequestMethod.POST)
    public String registerUser(User user) {
        try {
            // Your logic to save the user using bookWiseDAO
            bookWiseDAO.save(user);
            System.out.println("User registered successfully");
        } catch (Exception e) {
            e.printStackTrace();
            // Handle the exception or redirect to an error page
            return "error";
        }

        // Redirect to a success page or login page
        return "redirect:/login"; // Change the URL based on your application
    }
}
