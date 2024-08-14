package com.bookWise.controller;

import com.bookWise.SecurityConfig.BookWiseSecurityConfig;
import com.bookWise.dao.impl.BookWiseDAOImpl;
import com.bookWise.model.Authority;
import com.bookWise.model.BookWiseUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping("/userSignupNLogin")
public class LoginSignUpController {

    @Autowired
    private BookWiseDAOImpl bookWiseDAO;

    @Autowired
    private BookWiseSecurityConfig bookWiseSecurityConfig;

    @Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;

    @Transactional
    @PostMapping("/registerNewUser")
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
            List<BookWiseUser> bookWiseUsersList = bookWiseDAO.findBy("from BookWiseUser obj where obj.userEmail='" + email + "' and obj.userPhoneNumber = '" + phone + "' ");
            if (bookWiseUsersList != null && !bookWiseUsersList.isEmpty()) {
                response.put("success", false);
                response.put("message", "You are already registered. Please log in.");
                return response;
            }

            // Encode the password
            String encodedPassword = bookWiseSecurityConfig.passwordEncoder().encode(password);
            // Assigning the default role USER_ROLE
            Authority userRole = (Authority) bookWiseDAO.find(Authority.class, 1);

            // Create and save the new user
            BookWiseUser bookWiseUser = new BookWiseUser();
            bookWiseUser.setAuthorities(Collections.singleton(userRole));
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

    @PostMapping("/loginRegisteredUser")
    @ResponseBody
    public Map<String, Object> loginRegisteredUser(
            @RequestParam("userLoginId") String userLoginId,
            @RequestParam("your_pass") String password, HttpServletRequest request) {

        Map<String, Object> response = new HashMap<>();

        try {

            if(StringUtils.isBlank(userLoginId)){
                response.put("success", false);
                response.put("message", "Login id cannot be blank.");
                return response;
            }

            if(StringUtils.isBlank(password)){
                response.put("success", false);
                response.put("message", "Password cannot be blank.");
                return response;
            }

            List<BookWiseUser> userList = bookWiseDAO.findBy("from BookWiseUser where userEmail = '"+userLoginId+"' or userPhoneNumber = '"+userLoginId+"' ");
            BookWiseUser user = null;
            if(userList != null && userList.size() > 0){
                user = userList.get(0);
            }

            if (user == null) {
                response.put("success", false);
                response.put("message", "User not found.");
                return response;
            }

            // Check if the password matches
            if (!bookWiseSecurityConfig.passwordEncoder().matches(password, user.getUserPassword())) {
                response.put("success", false);
                response.put("message", "Invalid password.");
                return response;
            }

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userLoginId, password));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            response.put("success", true);
            response.put("message", "Login successful.");
            response.put("redirectUrl", request.getContextPath() + determineRedirectUrlBasedOnRole(authentication)); // Implement this method to get URL based on role
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "An error occurred. Please try again.");
        }

        return response;
    }


    // Example method to determine redirect URL based on user role
    public String determineRedirectUrlBasedOnRole(Authentication authentication) {
        String redirectUrl = "/default"; // Default URL if no roles match

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        boolean hasUserRole = authorities.stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER"));

        boolean hasAdminRole = authorities.stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        if (hasUserRole && hasAdminRole) {
            redirectUrl = "/sellerHome";
        } else if (hasUserRole) {
            redirectUrl = "/home";
        } else if (hasAdminRole) {
            redirectUrl = "/sellerHome";
        }

        return redirectUrl;
    }
}
