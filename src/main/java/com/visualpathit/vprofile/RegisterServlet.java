package com.visualpathit.vprofile;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.regex.Pattern;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private static final String VIEW = "/WEB-INF/views/register.jsp";
    private static final Pattern USERNAME = Pattern.compile("^[A-Za-z0-9_]{3,30}$");
    private static final Pattern EMAIL = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher(VIEW).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = trim(req.getParameter("username"));
        String email = trim(req.getParameter("email"));
        String password = req.getParameter("password") == null ? "" : req.getParameter("password");
        String confirm = req.getParameter("confirm") == null ? "" : req.getParameter("confirm");

        String error = null;
        if (!USERNAME.matcher(username).matches()) {
            error = "Username must be 3-30 characters: letters, numbers, underscore.";
        } else if (!EMAIL.matcher(email).matches() || email.length() > 100) {
            error = "Please enter a valid email address.";
        } else if (password.length() < 8 || password.length() > 100) {
            error = "Password must be 8-100 characters.";
        } else if (!password.equals(confirm)) {
            error = "Passwords do not match.";
        }

        if (error == null) {
            try {
                if (UserDao.create(username, email, password)) {
                    resp.sendRedirect(req.getContextPath() + "/login?registered=1");
                    return;
                }
                error = "That username is already taken.";
            } catch (SQLException e) {
                log("Registration failed", e);
                error = "Service temporarily unavailable. Try again shortly.";
            }
        }
        req.setAttribute("error", error);
        req.setAttribute("username", Html.escape(username));
        req.setAttribute("email", Html.escape(email));
        req.getRequestDispatcher(VIEW).forward(req, resp);
    }

    private static String trim(String s) {
        return s == null ? "" : s.trim();
    }
}
