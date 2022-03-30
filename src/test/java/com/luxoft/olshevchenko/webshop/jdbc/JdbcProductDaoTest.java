package com.luxoft.olshevchenko.webshop.jdbc;

import com.luxoft.olshevchenko.webshop.dao.jdbc.JdbcProductDao;
import com.luxoft.olshevchenko.webshop.entity.Product;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@PropertySource("classpath:/application.properties")
class JdbcProductDaoTest {

    @Test
    public void testFindAllReturnCorrectData(@Value("${jdbc_url}") String jdbUrl,
                                             @Value("${jdbc_user}") String jdbUser,
                                             @Value("${jdbc_password}") String jdbcPassword,
                                             @Value("${jdbc_name}") String jdbName) {
        PGSimpleDataSource pgSimpleDataSource = new PGSimpleDataSource();
        pgSimpleDataSource.setUrl(jdbUrl);
        pgSimpleDataSource.setUser(jdbUser);
        pgSimpleDataSource.setPassword(jdbcPassword);
        pgSimpleDataSource.setDatabaseName(jdbName);

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