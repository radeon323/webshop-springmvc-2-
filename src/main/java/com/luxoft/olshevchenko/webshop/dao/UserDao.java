package com.luxoft.olshevchenko.webshop.dao;

import com.luxoft.olshevchenko.webshop.entity.User;

/**
 * @author Oleksandr Shevchenko
 */
public interface UserDao {
    void remove(int id);

    void edit(User user);

    void add(User user);

    User findById(int id);

    User findByEmail (String email);

    boolean isUserExist(String email);

}
