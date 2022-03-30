package com.luxoft.olshevchenko.webshop.web;

import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;


/**
 * @author Oleksandr Shevchenko
 */
@Configuration
@PropertySource("classpath:/application.properties")
public class AppConfig {

    @Bean
    public PGSimpleDataSource pgSimpleDataSource(@Value("${jdbc_url}") String jdbUrl,
                                                 @Value("${jdbc_user}") String jdbUser,
                                                 @Value("${jdbc_password}") String jdbcPassword,
                                                 @Value("${jdbc_name}") String jdbName) {
        PGSimpleDataSource pgSimpleDataSource = new PGSimpleDataSource();
        pgSimpleDataSource.setUrl(jdbUrl);
        pgSimpleDataSource.setUser(jdbUser);
        pgSimpleDataSource.setPassword(jdbcPassword);
        pgSimpleDataSource.setDatabaseName(jdbName);
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
