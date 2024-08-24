package com.bookWise.SecurityConfig;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class WebServiceAuthenticationFilter implements Filter {

    private FilterConfig fc;

    public void init(FilterConfig filterConfig) throws ServletException {
        this.fc = filterConfig;
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        filterChain.doFilter(request, response);
    }

    public void destroy() {

    }
}
