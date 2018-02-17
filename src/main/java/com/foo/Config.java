package com.foo;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@ConditionalOnClass(name = "org.webjars.WebJarAssetLocator" )
@Configuration
public class Config {

    @Bean
    FilterRegistrationBean webJarsFilterRegistrationBean(WebJarsFilter webJarsFilter) {
        FilterRegistrationBean registration = new FilterRegistrationBean(webJarsFilter);
        registration.setUrlPatterns(Collections.singletonList("/webjars/*"));
        registration.setEnabled(true);
        return registration;
    }
}
