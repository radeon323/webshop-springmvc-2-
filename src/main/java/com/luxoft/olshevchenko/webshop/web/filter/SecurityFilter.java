package com.luxoft.olshevchenko.webshop.web.filter;

import com.luxoft.olshevchenko.webshop.entity.Session;
import com.luxoft.olshevchenko.webshop.service.SecurityService;
import org.springframework.context.ApplicationContextException;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Oleksandr Shevchenko
 */

public class SecurityFilter implements Filter {
    private final List<String> allowedPaths = List.of("/login", "/logout", "/register");

    private final SecurityService securityService = new SecurityService();

    public void setSecurityService(SecurityService securityService) {
    }

    @Override
    public void init(FilterConfig filterConfig) {
        ServletContext servletContext = filterConfig.getServletContext();
        WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        if (webApplicationContext != null) {
            setSecurityService(webApplicationContext.getBean(SecurityService.class));
        } else {
            throw new ApplicationContextException("Couldn`t get an application context");
        }
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

        Session session = securityService.getSessionByToken(getUserToken(httpServletRequest));

        if (session != null) {
            httpServletRequest.setAttribute("session", session);
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            httpServletResponse.sendRedirect("/login");
        }

    }

    public String getUserToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        return Arrays.stream(cookies)
                .filter(cookie -> "user-token".equals(cookie.getName()))
                .map(Cookie::getValue)
                .collect(Collectors.joining());
    }



    @Override
    public void destroy() {
    }



}
