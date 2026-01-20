package com.bookWise.controller;

import com.bookWise.SecurityConfig.BookWiseSecurityConfig;
import com.bookWise.bookEmailVerify.service.EmailService;
import com.bookWise.dao.impl.BookWiseDAOImpl;
import com.bookWise.model.Authority;
import com.bookWise.model.BookWiseUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
@RequestMapping("/userSignupNLogin")
public class LoginSignUpController {

    @Autowired
    private BookWiseDAOImpl bookWiseDAO;

    @Autowired
    private BookWiseSecurityConfig bookWiseSecurityConfig;

    @Autowired
    private EmailService emailService;

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
            String phone = request.getParameter("phone");
            String loginUserId = request.getParameter("loginUserId");
            String password = request.getParameter("pass");
            String rePassword = request.getParameter("re_pass");

            // ✅ Basic validation
            if (StringUtils.isAnyBlank(name, email, phone, loginUserId, password, rePassword)) {
                return error(response, "All fields are required.");
            }

            // ✅ Validate phone (server-side safety)
            if (!phone.matches("^[6-9]\\d{9}$")) {
                return error(response, "Please enter a valid 10-digit Indian mobile number.");
            }

            if (!password.equals(rePassword)) {
                return error(response, "Passwords do not match.");
            }

            if (bookWiseDAO.exists(
                    "from BookWiseUser where userEmail = '" + email + "'")) {
                return error(response, "Email already registered, please use another email.");
            }

            // ✅ Normalize phone number
            String normalizedPhone = "+91" + phone;
            if (bookWiseDAO.exists(
                    "from BookWiseUser where userPhoneNumber = '" + normalizedPhone + "'")) {
                return error(response, "This phone number is already registered, please use another.");
            }

            if (bookWiseDAO.exists(
                    "from BookWiseUser where loginUserId = '" + loginUserId + "'")) {
                return error(response, "Login ID already taken, please choose another.");
            }

            // ✅ Create user
            BookWiseUser user = new BookWiseUser();
            user.setUserName(name);
            user.setUserEmail(email);
            user.setUserPhoneNumber(normalizedPhone);
            user.setLoginUserId(loginUserId);
            user.setUserPassword(bookWiseSecurityConfig.passwordEncoder().encode(password));

            Authority userRole = (Authority) bookWiseDAO.find(Authority.class, 1);
            user.setAuthorities(Collections.singleton(userRole));

            // ✅ Email verification
            user.setEmailVerified(false);
            String token = UUID.randomUUID().toString();
            user.setVerificationToken(token);

            bookWiseDAO.saveOrUpdate(user);
            emailService.sendVerificationEmail(email, token);

            response.put("success", true);
            response.put("message", "Registration successful! A verification link has been sent to your email. Please verify to log in.");

        } catch (Exception e) {
            e.printStackTrace();
            return error(response, "An error occurred. Please try again.");
        }

        return response;
    }

    private Map<String, Object> error(Map<String, Object> response, String msg) {
        response.put("success", false);
        response.put("message", msg);
        return response;
    }

    @PostMapping("/loginRegisteredUser")
    @ResponseBody
    public Map<String, Object> loginRegisteredUser(
            @RequestParam("userLoginId") String inputLogin,
            @RequestParam("your_pass") String password,
            HttpServletRequest request) {

        Map<String, Object> response = new HashMap<>();

        try {
            if (StringUtils.isAnyBlank(inputLogin, password)) {
                return error(response, "Login ID and password are required.");
            }

            // ✅ Clear old session
            HttpSession oldSession = request.getSession(false);
            if (oldSession != null) {
                oldSession.invalidate();
            }
            SecurityContextHolder.clearContext();
            HttpSession newSession = request.getSession(true);
            String input = inputLogin.trim();

            // normalize email for consistency
            String normalizedEmail = input.toLowerCase();

            List<BookWiseUser> users = bookWiseDAO.findBy(
                "from BookWiseUser where " +
                     "userEmail = '" + normalizedEmail + "' " +
                     "or userPhoneNumber = '" + input + "' " +
                     "or loginUserId = '" + input + "'"
            );

            BookWiseUser user = (users != null && !users.isEmpty()) ? users.get(0) : null;

            if (user == null) {
                return error(response, "User not found.");
            }

            // ✅ Email verification check
            if (!Boolean.TRUE.equals(user.getEmailVerified())) {
                return error(response, "Your email is not verified. We have sent a verification link to your email. Please verify to continue.");
            }

            // ✅ Password check (fast fail)
            if (!bookWiseSecurityConfig.passwordEncoder()
                    .matches(password, user.getUserPassword())) {
                return error(response, "Invalid password.");
            }

            System.out.println("Authenticating user: " + user.getLoginUserId());
            Authentication authentication =
                authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                        user.getLoginUserId(),
                        password
                    )
                );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            newSession.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext() );

            response.put("success", true);
            response.put("message", "Login successful.");
            response.put("redirectUrl",request.getContextPath() + determineRedirectUrlBasedOnRole(authentication));

        } catch (Exception e) {
            e.printStackTrace();
            return error(response, "An error occurred. Please try again.");
        }

        return response;
    }

    public String determineRedirectUrlBasedOnRole(Authentication authentication) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        boolean hasUserRole = authorities.stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER"));
        boolean hasAdminRole = authorities.stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        boolean hasSuperAdminRole = authorities.stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_SUPERADMIN"));

        if (hasSuperAdminRole) {
            return "/superadmin/dashboard";
        } else if (hasUserRole && hasAdminRole) {
            return "/sellerHome";
        } else if (hasUserRole) {
            return "/home";
        } else if (hasAdminRole) {
            return "/sellerHome";
        }

        return "/default";
    }
}
