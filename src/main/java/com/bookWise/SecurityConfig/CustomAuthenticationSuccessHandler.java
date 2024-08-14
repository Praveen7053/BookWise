package com.bookWise.SecurityConfig;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        boolean isUser = false;
        boolean isAdmin = false;

        for (GrantedAuthority authority : authentication.getAuthorities()) {
            if (authority.getAuthority().equals("ROLE_USER")) {
                isUser = true;
            } else if (authority.getAuthority().equals("ROLE_ADMIN")) {
                isAdmin = true;
            }
        }

        if(isUser && isAdmin){
            response.sendRedirect(request.getContextPath() + "/home");
        }else if (isUser) {
            response.sendRedirect(request.getContextPath() + "/home");
        } else if (isAdmin) {
            response.sendRedirect(request.getContextPath() + "/sellerHome");
        } else {
            response.sendRedirect(request.getContextPath() + "/default");
        }
    }
}
