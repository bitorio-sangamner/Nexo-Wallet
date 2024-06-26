package com.gateway.config;

import com.gateway.filter.AuthenticationFilter;
import lombok.AllArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
@AllArgsConstructor
public class RoutesConfig {

    private final AuthenticationFilter authenticationFilter;
    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder routeLocatorBuilder) {
        return routeLocatorBuilder.routes()
                .route("auth-route", r -> r
                        .path("/register", "/login", "/verify", "/verify-email", "/forgotpassword", "/resetpassword")
                        .filters(f -> f
                                .addRequestHeader("From-Gateway", "true"))
                        .uri("lb://AUTHENTICATION-SERVICE"))
                .route( "jwt-auth-route", r -> r
                        .path("/users", "/logoff")
                        .filters(f -> f
								.filter(authenticationFilter.apply(new AuthenticationFilter.Config()))
                                .addRequestHeader("From-Gateway", "true"))
                        .uri("lb://AUTHENTICATION-SERVICE"))
                .build();
    }
}
