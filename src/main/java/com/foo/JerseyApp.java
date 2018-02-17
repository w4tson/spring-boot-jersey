package com.foo;


import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.foo")
public class JerseyApp extends SpringBootServletInitializer {

    public static void main(String[] args) {
        new JerseyApp()
                .configure(new SpringApplicationBuilder(JerseyApp.class))
                .run(args);
    }

}

