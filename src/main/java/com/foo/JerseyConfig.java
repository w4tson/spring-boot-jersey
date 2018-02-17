package com.foo;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletProperties;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;


@Component
public class JerseyConfig extends ResourceConfig {
    

    public JerseyConfig() {
        property(ServletProperties.FILTER_FORWARD_ON_404, "true");
    }
}