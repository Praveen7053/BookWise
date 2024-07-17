package com.bookWise.controller;

import com.bookWise.SecurityConfig.BoolWiseSecurityConfig;
import com.bookWise.dao.impl.BookWiseDAOImpl;
import com.bookWise.model.BookWiseUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/userSignupNLogin")
public class LoginSignUpController {

    @Autowired
    private BookWiseDAOImpl bookWiseDAO;

    @Autowired
    private BoolWiseSecurityConfig boolWiseSecurityConfig;

    @Transactional
    @RequestMapping(value = "/registerNewUser", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> registerUser(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            String name = request.getParameter("name");
            String email = request.getParameter("email");
            String password = request.getParameter("pass");
            String rePassword = request.getParameter("re_pass");
            String phone = request.getParameter("phone");

            if (StringUtils.isBlank(name) || StringUtils.isBlank(email) || StringUtils.isBlank(password) || StringUtils.isBlank(rePassword) || StringUtils.isBlank(phone)) {
                response.put("success", false);
                response.put("message", "All fields are required.");
                return response;
            }

            if (!password.equals(rePassword)) {
                response.put("success", false);
                response.put("message", "Passwords do not match.");
                return response;
            }

            // Check if the user already exists
            List<BookWiseUser> bookWiseUsersList = bookWiseDAO.findBy("from BookWiseUser obj where obj.userPhoneNumber = '" + phone + "' and obj.userEmail='" + email + "'");
            if (bookWiseUsersList != null && !bookWiseUsersList.isEmpty()) {
                response.put("success", false);
                response.put("message", "You are already registered. Please log in.");
                return response;
            }

            // Encode the password
            String encodedPassword = boolWiseSecurityConfig.passwordEncoder().encode(password);

            // Create and save the new user
            BookWiseUser bookWiseUser = new BookWiseUser();
            bookWiseUser.setUserName(name);
            bookWiseUser.setUserEmail(email);
            bookWiseUser.setUserPassword(encodedPassword);
            bookWiseUser.setUserPhoneNumber(phone);
            bookWiseDAO.saveOrUpdate(bookWiseUser);

            response.put("success", true);
            response.put("message", "Registration successful. Please log in.");
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "An error occurred. Please try again.");
        }
        return response;
    }

    @Transactional
    @RequestMapping(value = "/loginRegisteredUser", method = RequestMethod.POST)
    public String loginRegisteredUser(){
        System.out.println("called login");
        return "";
    }
}
