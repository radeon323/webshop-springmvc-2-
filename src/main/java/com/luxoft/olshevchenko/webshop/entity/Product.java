package com.luxoft.olshevchenko.webshop.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author Oleksandr Shevchenko
 */
@Getter
@Setter
@ToString
@Builder
public class Product {
    private int id;
    private String name;
    private double price;
    private LocalDateTime creationDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product product = (Product) o;
        return getId() == product.getId() && Double.compare(product.getPrice(), getPrice()) == 0 && Objects.equals(getName(), product.getName()) && Objects.equals(getCreationDate(), product.getCreationDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getPrice(), getCreationDate());
    }
}
