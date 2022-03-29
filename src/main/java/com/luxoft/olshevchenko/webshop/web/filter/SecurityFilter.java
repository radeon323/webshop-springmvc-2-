package com.luxoft.olshevchenko.webshop.web.filter;

import com.luxoft.olshevchenko.webshop.service.SecurityService;
import com.luxoft.olshevchenko.webshop.web.PropertiesReader;
import org.springframework.context.ApplicationContextException;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * @author Oleksandr Shevchenko
 */
public class SecurityFilter implements Filter {
    private final List<String> allowedPaths = List.of("/login", "/logout", "/register", "/favicon.ico");
    private static final int MAX_AGE_IN_SECONDS = Integer.parseInt(PropertiesReader.getProperties().getProperty("cookie_max_age"));

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

        if (isAuth(httpServletRequest)) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            httpServletResponse.sendRedirect("/login");
        }

    }

    private boolean isAuth(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        HttpSession session = request.getSession();
        Object userTokens = session.getAttribute("userTokens");
        if(cookies != null && userTokens != null) {
            for (Cookie cookie : cookies) {
                if(cookie.getName().equals("user-token") &&
                    userTokens.toString().contains(cookie.getValue()) &&
                        cookie.getMaxAge() < MAX_AGE_IN_SECONDS) {
                    return true;
                }
            }
        }
        return false;
    }


    @Override
    public void destroy() {
    }



}
