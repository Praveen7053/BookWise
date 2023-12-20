// src/main/java/com/bookwise/controller/LoginController.java
package com.bookWise.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/login")
public class LoginController extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Add your authentication logic here
        // Example: Check username and password against a database

        // For demonstration purposes, assume a successful login and redirect to a welcome page
        response.sendRedirect("welcome.jsp");
    }
}
