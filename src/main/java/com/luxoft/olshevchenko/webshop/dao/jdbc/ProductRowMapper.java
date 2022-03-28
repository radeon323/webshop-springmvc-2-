package com.luxoft.olshevchenko.webshop.dao.jdbc;

import com.luxoft.olshevchenko.webshop.entity.Product;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * @author Oleksandr Shevchenko
 */
public class ProductRowMapper {
    public Product mapRow(ResultSet resultSet) throws SQLException {
        int id  = resultSet.getInt("id");
        String name = resultSet.getString("name");
        double price  = resultSet.getDouble("price");
        Timestamp creationDate = resultSet.getTimestamp("creation_date");
        Product product = Product.builder().
                id(id)
                .name(name)
                .price(price)
                .creationDate(creationDate.toLocalDateTime())
                .build();
        return product;
    }
}
