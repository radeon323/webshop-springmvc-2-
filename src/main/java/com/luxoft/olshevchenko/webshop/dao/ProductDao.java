package com.luxoft.olshevchenko.webshop.dao;

import com.luxoft.olshevchenko.webshop.entity.Product;

import java.util.List;

/**
 * @author Oleksandr Shevchenko
 */
public interface ProductDao {

    List<Product> findAll();

    Product findById(int id);

    void remove(int id);

    void edit(Product product);

    void add(Product product);

}
