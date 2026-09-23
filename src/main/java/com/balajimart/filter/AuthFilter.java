package com.balajimart.filter;

import com.balajimart.model.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(urlPatterns = {
        "/admin/*", "/admin",
        "/seller/*", "/seller",
        "/wishlist/*", "/wishlist",
        "/cart/*", "/cart",
        "/checkout/*", "/checkout",
        "/orders/*", "/orders",
        "/account/*", "/account"
})
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization if needed
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        HttpSession session = httpRequest.getSession(false);
        User loggedInUser = (session != null) ? (User) session.getAttribute("user") : null;

        String path = httpRequest.getServletPath();

        if (loggedInUser == null) {
            String requestURI = httpRequest.getRequestURI();
            String queryString = httpRequest.getQueryString();
            String redirectUrl = requestURI + (queryString != null ? "?" + queryString : "");

            if (session == null) {
                session = httpRequest.getSession(true);
            }
            session.setAttribute("redirectAfterLogin", redirectUrl);

            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login");
            return;
        }

        if (!loggedInUser.isActive()) {
            if (session != null) {
                session.invalidate();
            }
            session = httpRequest.getSession(true);
            session.setAttribute("errorMessage", "Your account has been deactivated. Please contact support.");
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login");
            return;
        }

        // Role-based Access Control
        if (path.startsWith("/admin")) {
            if (!loggedInUser.isAdmin()) {
                session.setAttribute("errorMessage", "Access Denied: You do not have permission to access Admin features.");
                httpResponse.sendRedirect(httpRequest.getContextPath() + "/home");
                return;
            }
        } else if (path.startsWith("/seller")) {
            if (!loggedInUser.isSeller() && !loggedInUser.isAdmin()) {
                session.setAttribute("errorMessage", "Access Denied: You do not have permission to access Seller features.");
                httpResponse.sendRedirect(httpRequest.getContextPath() + "/home");
                return;
            }
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Cleanup if needed
    }
}
