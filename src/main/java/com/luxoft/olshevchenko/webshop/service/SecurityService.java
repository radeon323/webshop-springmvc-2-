package com.luxoft.olshevchenko.webshop.service;

import com.luxoft.olshevchenko.webshop.entity.Session;
import com.luxoft.olshevchenko.webshop.entity.User;
import lombok.SneakyThrows;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author Oleksandr Shevchenko
 */
@Service
@PropertySource("classpath:/application.properties")
public class SecurityService {

    private static Map<String, Session> sessions;
    private static MessageDigest messageDigest;

    @Value("${cookie_max_age}")
    private int maxAgeInSeconds;

    @SneakyThrows
    public SecurityService() {
        messageDigest = MessageDigest.getInstance("MD5");
        sessions = new ConcurrentHashMap<>();
    }

    public Map<String, Session> getSessions() {
        return sessions;
    }

    public static String md5(String text) {
        List<Object> chars = Collections.synchronizedList(new ArrayList<>());
        for (char c : text.toCharArray()) {
            chars.add(c);
        }
        chars.sort(Collections.reverseOrder());

        String txt = text + chars;
        byte[] bytes = messageDigest.digest(txt.getBytes());
        return Hex.encodeHexString(bytes);
    }

    public String loginAndGenerateUserToken(User user) {
        String token = UUID.randomUUID().toString();
        sessions.put(token, new Session(user, token, LocalDateTime.now().plusMinutes(maxAgeInSeconds/60)));
        return token;
    }

    public boolean isTokenValid(String token) {
        boolean maxAge;
        if (sessions.get(token) == null) {
            maxAge = false;
        } else {
            maxAge = sessions.get(token).getMaxAge().isAfter(LocalDateTime.now());
        }
        return sessions.containsKey(token) && maxAge;
    }

    public Session getSessionByToken(String token) {
        if (isTokenValid(token)) {
            return sessions.get(token);
        } else {
            return null;
        }
    }

}
