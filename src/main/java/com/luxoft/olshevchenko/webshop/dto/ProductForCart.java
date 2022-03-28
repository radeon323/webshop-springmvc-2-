package com.luxoft.olshevchenko.webshop.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

/**
 * @author Oleksandr Shevchenko
 */
@Getter
@Setter
@ToString
public class ProductForCart{
    private int id;
    private String name;
    private double price;
    private int quantity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductForCart)) return false;
        ProductForCart that = (ProductForCart) o;
        return getId() == that.getId() && Double.compare(that.getPrice(), getPrice()) == 0 && getQuantity() == that.getQuantity() && Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getPrice(), getQuantity());
    }
}
