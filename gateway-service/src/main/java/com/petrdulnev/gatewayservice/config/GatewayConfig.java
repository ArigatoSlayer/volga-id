package com.petrdulnev.gatewayservice.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    private final AuthenticationFilter filter;

    public GatewayConfig(AuthenticationFilter filter) {
        this.filter = filter;
    }

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("hospital", r -> r.path("/api/Hospitals/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://hospital"))
                .route("authentication", r -> r.path("/api/Authentication/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://authentication"))
                .build();
    }
}
