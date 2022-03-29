package com.luxoft.olshevchenko.webshop.service;

import com.luxoft.olshevchenko.webshop.dao.UserDao;
import com.luxoft.olshevchenko.webshop.entity.User;
import org.springframework.stereotype.Service;


/**
 * @author Oleksandr Shevchenko
 */
@Service
public class UserService {
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public void add(User user) {
        userDao.add(user);
    }

    public void remove(int id) {
        userDao.remove(id);
    }

    public void edit(User user) {
        userDao.edit(user);
    }

    public User findById(int id) {
        return userDao.findById(id);
    }

    public User findByEmail(String email) {
        return userDao.findByEmail(email);
    }

    public boolean isUserExist(String email) {
        return userDao.isUserExist(email);
    }
}
