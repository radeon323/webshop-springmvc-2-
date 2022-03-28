package com.luxoft.olshevchenko.webshop.service;

import com.luxoft.olshevchenko.webshop.dao.ProductDao;
import com.luxoft.olshevchenko.webshop.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductDao productDaoMock;

    private ProductService productService;

    @BeforeEach
    void init() {
        productService = new ProductService(productDaoMock);
    }

    @Test
    void testFindAll() {
        List<Product> products = new ArrayList<>();
        Product productNokia = Product.builder()
                .id(1)
                .name("Nokia")
                .price(100.0)
                .creationDate(LocalDateTime.of(2022, 2,16,16,53,10))
                .build();
        products.add(productNokia);
        Product productSiemens = Product.builder()
                .id(2)
                .name("Siemens")
                .price(120.0)
                .creationDate(LocalDateTime.of(2022, 2,16,16,54,10))
                .build();
        products.add(productSiemens);

        when(productDaoMock.findAll()).thenReturn(products);
        List<Product> actualProducts = productService.findAll();
        assertNotNull(actualProducts);
        assertEquals(2, actualProducts.size());
        assertEquals("Siemens", actualProducts.get(1).getName());
        assertEquals(productNokia, actualProducts.get(0));
        verify(productDaoMock, times(1)).findAll();
    }

    @Test
    void testFindById() {
        List<Product> products = new ArrayList<>();
        Product productNokia = Product.builder()
                .id(1)
                .name("Nokia")
                .price(100.0)
                .creationDate(LocalDateTime.of(2022, 2,16,16,53,10))
                .build();
        products.add(productNokia);
        Product productSiemens = Product.builder()
                .id(2)
                .name("Siemens")
                .price(120.0)
                .creationDate(LocalDateTime.of(2022, 2,16,16,54,10))
                .build();
        products.add(productSiemens);

        when(productDaoMock.findById(1)).thenReturn(productNokia);
        Product actualProduct = productService.findById(1);
        assertEquals(1, actualProduct.getId());
        assertEquals("Nokia", actualProduct.getName());
        verify(productDaoMock, times(1)).findById(1);
    }

    @Test
    void testAdd() {
        Product productNokia = Product.builder()
                .id(1)
                .name("Nokia")
                .price(100.0)
                .creationDate(LocalDateTime.of(2022, 2,16,16,53,10))
                .build();
        doNothing().when(productDaoMock).add(isA(Product.class));
        productDaoMock.add(productNokia);
        verify(productDaoMock, times(1)).add(productNokia);
    }

    @Test
    void testRemove() {
        doNothing().when(productDaoMock).remove(isA(Integer.class));
        productDaoMock.remove(1);
        verify(productDaoMock, times(1)).remove(1);
    }

    @Test
    void testEdit() {
        Product productNokia = Product.builder()
                .id(1)
                .name("Nokia")
                .price(100.0)
                .creationDate(LocalDateTime.of(2022, 2,16,16,53,10))
                .build();
        doNothing().when(productDaoMock).edit(isA(Product.class));
        productDaoMock.edit(productNokia);
        verify(productDaoMock, times(1)).edit(productNokia);
    }


}