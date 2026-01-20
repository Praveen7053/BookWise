package com.bookWise.forgotPassword;

import com.bookWise.SecurityConfig.BookWiseSecurityConfig;
import com.bookWise.bookEmailVerify.service.EmailService;
import com.bookWise.dao.impl.BookWiseDAOImpl;
import com.bookWise.model.BookWiseUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping(value = "/api/actions")
public class ForgotPasswordController {

    @Autowired
    private BookWiseDAOImpl bookWiseDAO;

    @Autowired
    private EmailService emailService;

    @Autowired
    private BookWiseSecurityConfig bookWiseSecurityConfig;

    @PostMapping("/forgot-password/send")
    @ResponseBody
    @Transactional
    public Map<String, Object> sendResetLink(@RequestBody Map<String, String> payload) {

        Map<String, Object> res = new HashMap<>();
        String email = payload.get("email");

        List<BookWiseUser> users = bookWiseDAO.findBy(
                "from BookWiseUser where userEmail = '" + email + "'"
        );

        if (users.isEmpty()) {
            return error(res, "No account found with this email.");
        }

        BookWiseUser user = users.get(0);

        String token = UUID.randomUUID().toString();
        user.setResetPasswordToken(token);
        user.setResetTokenExpiry(Timestamp.valueOf(LocalDateTime.now().plusMinutes(30)));

        bookWiseDAO.saveOrUpdate(user);
        emailService.sendResetPasswordEmail(email, token);

        res.put("success", true);
        res.put(
                "message",
                "Password reset link sent to your email. Please check your inbox."
        );
        return res;
    }

    @GetMapping("/resetPassword")
    public String showResetPasswordPage(
            @RequestParam("token") String token,
            Model model) {

        List<BookWiseUser> users = bookWiseDAO.findBy(
                "from BookWiseUser where resetPasswordToken = '" + token + "'"
        );

        if (users.isEmpty()) {
            model.addAttribute("message", "Invalid or expired reset link.");
            return "verificationFailed";
        }

        BookWiseUser user = users.get(0);

        // ⏰ Expiry check
        if (user.getResetTokenExpiry() == null ||
                user.getResetTokenExpiry().before(Timestamp.valueOf(LocalDateTime.now()))) {

            model.addAttribute("message", "Reset link has expired.");
            return "verificationFailed";
        }

        model.addAttribute("token", token);
        return "resetPassword"; // resetPassword.jsp
    }

    @PostMapping("/reset-password/save")
    @Transactional
    public String resetPassword(
            @RequestParam("token") String token,
            @RequestParam("password") String password,
            @RequestParam("confirmPassword") String confirmPassword,
            Model model) {

        if (!password.equals(confirmPassword)) {
            model.addAttribute("message", "Passwords do not match.");
            return "verificationFailed";
        }

        List<BookWiseUser> users = bookWiseDAO.findBy(
                "from BookWiseUser where resetPasswordToken = '" + token + "'"
        );

        if (users.isEmpty()) {
            model.addAttribute("message", "Invalid or expired reset link.");
            return "verificationFailed";
        }

        BookWiseUser user = users.get(0);

        user.setUserPassword(
                bookWiseSecurityConfig.passwordEncoder().encode(password)
        );
        user.setResetPasswordToken(null);
        user.setResetTokenExpiry(null);

        bookWiseDAO.saveOrUpdate(user);

        model.addAttribute("title", "Password Reset Successful");
        model.addAttribute(
                "message",
                "Password reset successfully. You can now log in."
        );


        return "verificationSuccess";
    }

    private Map<String, Object> error(Map<String, Object> response, String msg) {
        response.put("success", false);
        response.put("message", msg);
        return response;
    }
}
