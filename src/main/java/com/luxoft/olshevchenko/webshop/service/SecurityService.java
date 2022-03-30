package com.luxoft.olshevchenko.webshop.service;

import lombok.SneakyThrows;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * @author Oleksandr Shevchenko
 */
@Service
@PropertySource("classpath:/application.properties")
public class SecurityService {

    List<String> userTokens = Collections.synchronizedList(new ArrayList<>());
    static MessageDigest messageDigest;

    @Value("${cookie_max_age}")
    private int MAX_AGE_IN_SECONDS;



    @SneakyThrows
    public SecurityService() {
        messageDigest = MessageDigest.getInstance("MD5");
    }

    public List<String> getUserTokens() {
        return userTokens;
    }

    public static String md5(String text) throws NoSuchAlgorithmException {
        List<Object> chars = Collections.synchronizedList(new ArrayList<>());
        for (char c : text.toCharArray()) {
            chars.add(c);
        }
        chars.sort(Collections.reverseOrder());

        String txt = text + chars;
        byte[] bytes = messageDigest.digest(txt.getBytes());
        return Hex.encodeHexString(bytes);
    }

    public String generateAndAddUserToken() {
        String userToken = UUID.randomUUID().toString();
        userTokens.add(userToken);
        return userToken;
    }

    public boolean isTokenValid(Cookie[] cookies) {
            if (cookies != null) {
            for (Cookie cookie : cookies) {
                if(cookie.getName().equals("user-token")) {
                    if(userTokens.contains(cookie.getValue()) && cookie.getMaxAge() < MAX_AGE_IN_SECONDS) {
                        return true;
                    }
                    break;
                }
            }
        }
        return false;
    }


}
