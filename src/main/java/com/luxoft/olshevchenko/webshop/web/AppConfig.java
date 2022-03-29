package com.luxoft.olshevchenko.webshop.web;

import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import java.util.Properties;

/**
 * @author Oleksandr Shevchenko
 */
@Configuration
public class AppConfig {

    @Bean
    public PGSimpleDataSource pgSimpleDataSource() {
        Properties properties = PropertiesReader.getProperties();
        PGSimpleDataSource pgSimpleDataSource = new PGSimpleDataSource();
        pgSimpleDataSource.setUrl(properties.getProperty("jdbc_url"));
        pgSimpleDataSource.setUser(properties.getProperty("jdbc_user"));
        pgSimpleDataSource.setPassword(properties.getProperty("jdbc_password"));
        pgSimpleDataSource.setDatabaseName(properties.getProperty("jdbc_name"));
        return pgSimpleDataSource;
    }

    @Bean
    public FreeMarkerViewResolver freemarkerViewResolver() {
        FreeMarkerViewResolver resolver = new FreeMarkerViewResolver();
        resolver.setCache(true);
        resolver.setPrefix("");
        resolver.setSuffix(".html");
        return resolver;
    }

    @Bean
    public FreeMarkerConfigurer freemarkerConfig() {
        FreeMarkerConfigurer freeMarkerConfigurer = new FreeMarkerConfigurer();
        freeMarkerConfigurer.setTemplateLoaderPath("classpath:templates/");
        return freeMarkerConfigurer;
    }


}
