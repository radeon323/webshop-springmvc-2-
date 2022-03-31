package com.luxoft.olshevchenko.webshop.entity;

import com.luxoft.olshevchenko.webshop.dto.ProductForCart;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Oleksandr Shevchenko
 */
@Data
@NoArgsConstructor
public class Session {
    private User user;
    private String token;
    private LocalDateTime maxAge;
    private List<Product> cart;

    public Session(User user, String token, LocalDateTime maxAge) {
        this.user = user;
        this.token = token;
        this.maxAge = maxAge;
        this.cart = new ArrayList<>();
    }
}
