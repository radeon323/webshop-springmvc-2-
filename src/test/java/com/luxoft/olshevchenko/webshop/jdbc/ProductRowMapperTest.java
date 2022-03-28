package com.luxoft.olshevchenko.webshop.jdbc;

import com.luxoft.olshevchenko.webshop.dao.jdbc.ProductRowMapper;
import com.luxoft.olshevchenko.webshop.entity.Product;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProductRowMapperTest {

    @Test
    void testMapRow() throws SQLException {
        ProductRowMapper productRowMapper = new ProductRowMapper();
        LocalDateTime localDateTime = LocalDateTime.of(2022, 2,16,16,53,10);
        Timestamp timestamp = Timestamp.valueOf(localDateTime);

        ResultSet resultSetMock = mock(ResultSet.class);
        when(resultSetMock.getInt("id")).thenReturn(1);
        when(resultSetMock.getString("name")).thenReturn("Xiaomi Redmi Note 9 Pro");
        when(resultSetMock.getDouble("price")).thenReturn(7999.0);
        when(resultSetMock.getTimestamp("creation_date")).thenReturn(timestamp);

        Product actualProduct = productRowMapper.mapRow(resultSetMock);

        assertEquals(1, actualProduct.getId());
        assertEquals("Xiaomi Redmi Note 9 Pro", actualProduct.getName());
        assertEquals(7999.0, actualProduct.getPrice());
    }
}