package config;

import lombok.AllArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;


@Service
public class RoutesConfig {
//    @Bean
//    public RouteLocator gatewayRoutes(RouteLocatorBuilder routeLocatorBuilder) {
//        return routeLocatorBuilder.routes()
//                .route("auth-route", r -> r
//                        .path("/register")
//                        .filters(f -> f
//                                .addRequestHeader("From-Gateway", "true"))
//                        .uri("lb://AUTHENTICATION-SERVICE"))
//                .route("auth-route", r -> r
//                        .path("/vile")
//                        .filters(f -> f
//                                .addRequestHeader("From-Gateway", "true"))
//                        .uri("lb://AUTHENTICATION-SERVICE"))
//                .build();
//    }
}
