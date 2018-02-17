package com.foo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.servlet.FilterRegistration;
import javax.servlet.Registration;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;
import java.util.Map;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;

@Component
public class ServletContextDiagnostic {

    @Autowired
    ServletContext servletContext;

    @EventListener
    public void handleContextRefresh(ContextRefreshedEvent event) {
        Map<String, ? extends ServletRegistration> servletRegistrations = servletContext.getServletRegistrations();
        Map<String, ? extends FilterRegistration> filterRegistrations = servletContext.getFilterRegistrations();

        servletRegistrations.forEach((name, servletRegistration) -> {
            System.out.println(format("%s -> %s", name, toString(servletRegistration)));
        });
        
        System.out.println(format("%s filters :", filterRegistrations.size()));

        filterRegistrations.forEach((name, filterRegistration) -> {
            System.out.println(format("%s -> %s", name, toString(filterRegistration)));
        });

        System.out.println("\n");
    }

    public String toString(ServletRegistration servletRegistration) {
        String className = servletRegistration.getClassName();
        Map<String, String> initParameters = servletRegistration.getInitParameters();
        String params = initParameters.entrySet().stream().map(e -> format("%s -> %s", e.getKey(), e.getValue())).collect(joining());
        String mappings = servletRegistration.getMappings().stream().collect(joining());

        return format("class: %s     mapping: %s      params: %s",  className, mappings, params);
    }

    public String toString(Registration filterRegistration) {
        return filterRegistration.getClassName();
    }

   
}