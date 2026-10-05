package com.visualpathit.vprofile;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final String VIEW = "/WEB-INF/views/login.jsp";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            if (SessionStore.userFor(req) != null) {
                resp.sendRedirect(req.getContextPath() + "/dashboard");
                return;
            }
        } catch (SQLException e) {
            log("Session lookup failed", e);
        }
        if ("1".equals(req.getParameter("registered"))) {
            req.setAttribute("message", "Account created. Please log in.");
        }
        if ("1".equals(req.getParameter("loggedout"))) {
            req.setAttribute("message", "You have been logged out.");
        }
        req.getRequestDispatcher(VIEW).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username") == null ? "" : req.getParameter("username").trim();
        String password = req.getParameter("password") == null ? "" : req.getParameter("password");
        try {
            if (UserDao.authenticate(username, password)) {
                String token = SessionStore.create(username);
                SessionStore.setCookie(resp, token, SessionStore.TTL_SECONDS);
                resp.sendRedirect(req.getContextPath() + "/dashboard");
                return;
            }
            req.setAttribute("error", "Invalid username or password.");
        } catch (SQLException e) {
            log("Login failed", e);
            req.setAttribute("error", "Service temporarily unavailable. Try again shortly.");
        }
        req.setAttribute("username", Html.escape(username));
        req.getRequestDispatcher(VIEW).forward(req, resp);
    }
}
