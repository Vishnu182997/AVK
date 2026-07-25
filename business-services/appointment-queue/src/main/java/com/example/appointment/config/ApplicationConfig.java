package com.example.appointment.config;
import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.components.Components;
import io.swagger.v3.oas.models.security.*;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.socket.config.annotation.*;
@Configuration
@EnableJpaAuditing
@EnableAsync
public class ApplicationConfig {
  @Bean
  OpenAPI api() {
    return new OpenAPI()
        .info(new Info()
                .title("Smart Appointment & Queue API")
                .version("1.0")
                .description("Appointment lifecycle, waitlist and live queue management"))
        .components(new Components().addSecuritySchemes("bearerAuth",
            new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")))
        .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
  }
  @Configuration
  @EnableWebSocketMessageBroker
  static class Ws implements WebSocketMessageBrokerConfigurer {
    public void configureMessageBroker(MessageBrokerRegistry r) {
      r.enableSimpleBroker("/topic", "/user");
      r.setApplicationDestinationPrefixes("/app");
    }
    public void registerStompEndpoints(StompEndpointRegistry r) {
      r.addEndpoint("/ws").setAllowedOriginPatterns("*");
    }
  }
}
