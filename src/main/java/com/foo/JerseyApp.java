package com.foo;


import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
public class JerseyApp extends SpringBootServletInitializer {

    public static void main(String[] args) {
        new JerseyApp()
                .configure(new SpringApplicationBuilder(JerseyApp.class))
                .run(args);
    }

}

