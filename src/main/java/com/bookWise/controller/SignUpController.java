package com.bookWise.controller;

import com.bookWise.dao.impl.BookWiseDAOImpl;
import com.bookWise.model.BookWiseUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/register")
public class SignUpController {

    @Autowired
    private BookWiseDAOImpl bookWiseDAO;

    @Transactional
    @RequestMapping(value = "/signUpUser", method = RequestMethod.POST)
    public String registerUser(HttpServletRequest request) {
        try {
            String name = request.getParameter("name");
            String email = request.getParameter("email");
            String password = request.getParameter("pass");
            String rePassword = request.getParameter("re_pass");
            BookWiseUser bookWiseUser = new BookWiseUser();
            bookWiseUser.setUserName(name);
            bookWiseUser.setUserEmail(email);
            bookWiseUser.setUserPassword(password);
            bookWiseDAO.saveOrUpdate(bookWiseUser);
        } catch (Exception e) {
            e.printStackTrace();
            // Handle the exception or redirect to an error page
            return "error";
        }

        // Redirect to a success page or login page
        return "redirect:/Login"; // Change the URL based on your application
    }
}
