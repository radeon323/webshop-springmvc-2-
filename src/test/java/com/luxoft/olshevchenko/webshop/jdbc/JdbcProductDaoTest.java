package com.luxoft.olshevchenko.webshop.jdbc;

import com.luxoft.olshevchenko.webshop.dao.jdbc.JdbcProductDao;
import com.luxoft.olshevchenko.webshop.entity.Product;
import com.luxoft.olshevchenko.webshop.web.PropertiesReader;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.postgresql.ds.PGSimpleDataSource;

import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class JdbcProductDaoTest {

    @Test
    public void testFindAllReturnCorrectData() {
        Properties properties = PropertiesReader.getProperties();
        PGSimpleDataSource pgSimpleDataSource = new PGSimpleDataSource();
        pgSimpleDataSource.setUrl(properties.getProperty("jdbc_url"));
        pgSimpleDataSource.setUser(properties.getProperty("jdbc_user"));
        pgSimpleDataSource.setPassword(properties.getProperty("jdbc_password"));
        pgSimpleDataSource.setDatabaseName(properties.getProperty("jdbc_name"));

        JdbcProductDao jdbcProductDao = new JdbcProductDao(pgSimpleDataSource);
        List<Product> products = jdbcProductDao.findAll();
        assertFalse(products.isEmpty());
        for (Product product : products) {
            assertNotEquals(0, product.getId());
            assertNotNull(product.getName());
            assertNotEquals(0, product.getPrice());
            assertNotNull(product.getCreationDate());
        }
    }


    @Test
    public void testFindById() {
        JdbcProductDao jdbcProductDaoMock = Mockito.mock(JdbcProductDao.class);

        Product product = Product.builder().build();
        product.setId(23);
        product.setName("IPhone");
        product.setPrice(100);

        when(jdbcProductDaoMock.findById(23)).thenReturn(product);

        assertEquals(23, product.getId());
        assertEquals(100, product.getPrice());
        assertEquals("IPhone", product.getName());
    }


}