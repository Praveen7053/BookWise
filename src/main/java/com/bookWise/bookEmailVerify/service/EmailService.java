package com.bookWise.bookEmailVerify.service;

import com.bookWise.dao.impl.BookWiseDAOImpl;
import com.bookWise.model.BookWiseUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private BookWiseDAOImpl bookWiseDAO;

    @Transactional
    public String verifyEmail(String token, Model model) {
        List<BookWiseUser> users = bookWiseDAO.findBy("from BookWiseUser where verificationToken = '" + token + "'");

        if (users == null || users.isEmpty()) {
            model.addAttribute("message", "Invalid or expired verification link.");
            return "verificationFailed";
        }

        BookWiseUser user = users.get(0);
        user.setEmailVerified(true);
        user.setVerificationToken(null);

        bookWiseDAO.saveOrUpdate(user);

        model.addAttribute("message", "Email verified successfully. You can now log in.");
        return "verificationSuccess";
    }

    public void sendVerificationEmail(String toEmail, String token) {

        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();

        String verificationLink = baseUrl + "/api/emailVerification/verify-email?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Verify your BookWise account");
        message.setText(
                "Welcome to BookWise!\n\n" +
                        "Please click the link below to verify your email:\n" +
                        verificationLink + "\n\n" +
                        "If you did not register, ignore this email."
        );

        mailSender.send(message);
    }
}
