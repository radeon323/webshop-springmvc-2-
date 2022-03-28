package com.luxoft.olshevchenko.webshop.web.filter;

import com.luxoft.olshevchenko.webshop.service.SecurityService;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author Oleksandr Shevchenko
 */
public class SecurityFilter implements Filter {

    private final SecurityService securityService;
    private final List<String> allowedPaths = List.of("/login", "/logout", "/register");

    public SecurityFilter(SecurityService securityService) {
        this.securityService = securityService;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest)  servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse)  servletResponse;

        String requestURI = httpServletRequest.getRequestURI();
        for (String allowedPath : allowedPaths) {
            if (requestURI.startsWith(allowedPath)) {
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }
        }

        if (requestURI.equals("/products")) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        if (securityService.isAuth(httpServletRequest)) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            httpServletResponse.sendRedirect("/login");
        }

    }


    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void destroy() {
    }
}
