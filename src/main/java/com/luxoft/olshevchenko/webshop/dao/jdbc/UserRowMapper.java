package com.luxoft.olshevchenko.webshop.dao.jdbc;

import com.luxoft.olshevchenko.webshop.entity.Role;
import com.luxoft.olshevchenko.webshop.entity.User;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.luxoft.olshevchenko.webshop.entity.Role.USER;

/**
 * @author Oleksandr Shevchenko
 */
public class UserRowMapper {
    public User mapRow(ResultSet resultSet) throws SQLException {
        int id  = resultSet.getInt("id");
        String email = resultSet.getString("email");
        String password = resultSet.getString("password");
        String gender = resultSet.getString("gender");
        String firstName = resultSet.getString("firstName").trim();
        String lastName = resultSet.getString("lastName").trim();
        String about = resultSet.getString("about").trim();
        int age  = resultSet.getInt("age");
        User user = User.builder().
                id(id)
                .email(email)
                .password(password)
                .gender(gender)
                .firstName(firstName)
                .lastName(lastName)
                .about(about)
                .age(age)
                .role(USER)
                .build();
        return user;
    }
}
