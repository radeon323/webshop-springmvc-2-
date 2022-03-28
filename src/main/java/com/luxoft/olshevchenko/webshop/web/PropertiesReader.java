package com.luxoft.olshevchenko.webshop.web;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Oleksandr Shevchenko
 */
public class PropertiesReader {
    private static final Properties properties = new Properties();

    public static Properties getProperties()  {
        try (InputStream resource = PropertiesReader.class.getClassLoader()
                .getResourceAsStream("application.properties")) {
            properties.load(resource);
            return properties;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


}
