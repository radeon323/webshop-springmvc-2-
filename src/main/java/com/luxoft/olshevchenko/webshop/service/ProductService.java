package com.luxoft.olshevchenko.webshop.service;

import com.luxoft.olshevchenko.webshop.dao.ProductDao;
import com.luxoft.olshevchenko.webshop.entity.Product;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Oleksandr Shevchenko
 */
@Service
public class ProductService {

    private final ProductDao productDao;

    public ProductService(ProductDao productDao) {
        this.productDao = productDao;
    }

    public List<Product> findAll() {
        return productDao.findAll();
    }

    public Product findById(int id) {
        return productDao.findById(id);
    }

    public void add(Product product) {
        productDao.add(product);
    }

    public void remove(int id) {
        productDao.remove(id);
    }

    public void edit(Product product) {
        productDao.edit(product);
    }

}
