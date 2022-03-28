package com.luxoft.olshevchenko.webshop.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Oleksandr Shevchenko
 */
@Getter
@Setter
@ToString
@Builder
public class User {
    private int id;
    private String email;
    private String password;
    private String gender;
    private String firstName;
    private String lastName;
    private String about;
    private int age;
    private Role role;
}
