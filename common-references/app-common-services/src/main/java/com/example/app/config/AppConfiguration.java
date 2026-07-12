package com.example.app.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@PropertySources(value = {@PropertySource("classpath:application.properties")})
@ComponentScan({ "com.fastcollab.config", "com.fastcollab.service", "com.fastcollab.handler.service", "com.fastcollab.flight.service" })
public class AppConfiguration {

}
