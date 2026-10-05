package com.visualpathit.vprofile;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/** POST-only so that a stray link or image tag cannot log someone out. */
@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            SessionStore.destroy(req);
        } catch (SQLException e) {
            log("Logout failed", e);
        }
        SessionStore.setCookie(resp, "", 0);
        resp.sendRedirect(req.getContextPath() + "/login?loggedout=1");
    }
}
