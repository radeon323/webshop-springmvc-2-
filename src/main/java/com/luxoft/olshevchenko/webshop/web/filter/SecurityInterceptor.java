package com.luxoft.olshevchenko.webshop.web.filter;

import com.luxoft.olshevchenko.webshop.service.SecurityService;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class SecurityInterceptor implements HandlerInterceptor {

    private final SecurityService securityService;
    private List<String> allowedPaths = List.of("/login", "/logout", "/register");

    public SecurityInterceptor(SecurityService securityService) {
        this.securityService = securityService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        System.out.println("Pre-handle");
        String requestURI = request.getRequestURI();
        for (String allowedPath : allowedPaths) {
            if (requestURI.startsWith(allowedPath)) {
                return true;
            }
        }
        if (requestURI.equals("/products")) {
            return true;
        }
        if (securityService.isAuth(request)) {
            return true;
        } else {
            response.sendRedirect("/login");
            return false;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        System.out.println("Post-handle");
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        System.out.println("After completion handle");
    }


}


