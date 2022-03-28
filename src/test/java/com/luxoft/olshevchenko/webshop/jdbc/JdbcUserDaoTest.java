package com.luxoft.olshevchenko.webshop.jdbc;

import com.luxoft.olshevchenko.webshop.dao.jdbc.JdbcUserDao;
import com.luxoft.olshevchenko.webshop.entity.User;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class JdbcUserDaoTest {

    private User buildUser() {
        User user = User.builder().build();
        user.setId(1);
        user.setEmail("darthvader@gmail.com");
        user.setPassword("12345");
        user.setGender("male");
        user.setFirstName("Darth");
        user.setLastName("Vader");
        user.setAge(100);
        user.setAbout("DeathStar");
        return user;
    }


    @Test
    public void testIsUserExist() {
        JdbcUserDao jdbcUserDaoMock = Mockito.mock(JdbcUserDao.class);
        User user = buildUser();

        when(jdbcUserDaoMock.isUserExist(user.getEmail())).thenReturn(true);
        when(jdbcUserDaoMock.isUserExist("obivankenobi@gmail.com")).thenReturn(false);

        assertTrue(jdbcUserDaoMock.isUserExist(user.getEmail()));
        assertFalse(jdbcUserDaoMock.isUserExist("obivankenobi@gmail.com"));
    }


    @Test
    public void testFindByEmail() {
        JdbcUserDao jdbcUserDaoMock = Mockito.mock(JdbcUserDao.class);
        User user = buildUser();

        when(jdbcUserDaoMock.findByEmail(user.getEmail())).thenReturn(user);

        assertEquals(1, user.getId());
        assertEquals("darthvader@gmail.com", user.getEmail());
        assertNotEquals("obivankenobi@gmail.com", user.getEmail());
        assertEquals("male", user.getGender());
        assertEquals("Darth", user.getFirstName());
        assertEquals("Vader", user.getLastName());
        assertEquals(100, user.getAge());
        assertNotEquals(99, user.getAge());
    }


}
