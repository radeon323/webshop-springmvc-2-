package com.luxoft.olshevchenko.webshop.web.controller;

import com.luxoft.olshevchenko.webshop.entity.Role;
import com.luxoft.olshevchenko.webshop.entity.User;
import com.luxoft.olshevchenko.webshop.service.SecurityService;
import com.luxoft.olshevchenko.webshop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

/**
 * @author Oleksandr Shevchenko
 */
@Controller
@RequestMapping()
@RequiredArgsConstructor
public class UsersController {

    private final UserService userService;
    private final SecurityService securityService;
    private static int MAX_AGE_IN_SECONDS = 10;

    @GetMapping("/login")
    protected String getLoginPage() {
        return "login";
    }


    @PostMapping("/login")
    protected String login(@RequestParam String email, @RequestParam String password,
                           HttpSession session, HttpServletResponse response, Model model) {

        User user = userService.findByEmail(email);

        if (user != null) {
            if (user.getPassword().equals(md5(password))) {
                session.setAttribute("user", user);
                session.setAttribute("userTokens", securityService.getUserTokens());
                session.setAttribute("cart", new ArrayList<>());
                session.setMaxInactiveInterval(-1);

                String userToken = securityService.generateAndAddUserToken();
                Cookie cookie = new Cookie("user-token", userToken);
                cookie.setMaxAge(MAX_AGE_IN_SECONDS);
                response.addCookie(cookie);
                return "redirect:/products";

            } else {
                String errorMsg = "Please enter correct password. <a href='/login'> Forgot password?</a>";
                model.addAttribute("errorMsg", errorMsg);
                return "login";
            }

        } else {
            String errorMsg = "User not found. Please enter correct email or <a href='/register'>register</a>.";
            model.addAttribute("errorMsg", errorMsg);
            return "login";
        }

    }


    @GetMapping("/logout")
    protected String logout(HttpServletRequest request, HttpServletResponse response) {
        String userToken = getUserToken(request);
        Cookie cookie = new Cookie("user-token", userToken);
        cookie.setValue(null);
        cookie.setMaxAge(0);

        HttpSession session = request.getSession();
        session.removeAttribute("name");
        response.addCookie(cookie);
        return "redirect:/products";
    }


    @GetMapping("/register")
    protected String getRegisterPage() {
        return "register";
    }


    @PostMapping("/register")
    protected String register(@RequestParam String email, @RequestParam String password,
                              @RequestParam(defaultValue = "") String gender, @RequestParam(defaultValue = "") String firstName,
                              @RequestParam(defaultValue = "") String lastName, @RequestParam(defaultValue = "") String about,
                              @RequestParam(defaultValue = "0") int age, Model model) {

        if (!userService.isUserExist(email)) {
            if (email != null && email.length() > 0 && password != null) {
                User user = User.builder().
                        email(email)
                        .password(md5(password))
                        .gender(gender)
                        .firstName(firstName)
                        .lastName(lastName)
                        .about(about)
                        .age(age)
                        .role(Role.USER)
                        .build();

                userService.add(user);

                String msgSuccess = "User <i>" + email + "</i> was successfully registered!";
                model.addAttribute("msgSuccess", msgSuccess);
                return "register";

            } else {
                String errorMsg = "Please fill up all fields!";
                model.addAttribute("errorMsg", errorMsg);
                return "register";
            }

        } else {
            String errorMsg = "This user is already exist! <a href='/login'> Login page</a>";
            model.addAttribute("errorMsg", errorMsg);
            return "register";
        }
    }


    @GetMapping("/users/edit")
    protected String getEditUserPage(@RequestParam int id, Model model) {
        User user = userService.findById(id);
        model.addAttribute("user", user);
        return "edit_user";
    }

    @PostMapping("/users/edit")
    protected String editUser(@RequestParam String email, @RequestParam String password,
                              @RequestParam(defaultValue = "") String gender, @RequestParam(defaultValue = "") String firstName,
                              @RequestParam(defaultValue = "") String lastName, @RequestParam(defaultValue = "") String about,
                              @RequestParam(defaultValue = "0") int age, @RequestParam int id, Model model) {

        try {
            User user = User.builder().
                    id(id)
                    .email(email)
                    .password(md5(password))
                    .gender(gender)
                    .firstName(firstName)
                    .lastName(lastName)
                    .about(about)
                    .age(age)
                    .role(Role.USER)
                    .build();

            model.addAttribute("user", user);

            if (email != null && email.length() > 0 && password != null) {
                userService.edit(user);

                String msgSuccess = "Info was successfully updated!";
                model.addAttribute("msgSuccess", msgSuccess);
                return "edit_user";

            } else {
                String errorMsg = "Please fill up all fields";
                model.addAttribute("errorMsg", errorMsg);
                return "edit_user";
            }
        } catch (Exception e) {
            String errorMsg = "Please fill up all fields";
            model.addAttribute("errorMsg", errorMsg);
            return "edit_user";
        }
    }


    private String md5(String password) {
        String md5;
        try {
            md5 = SecurityService.md5(password);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return md5;
    }

    private static String getUserToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("user-token")) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }


}
