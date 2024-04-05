package dev.rsm.gatewayService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GatewayServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayServiceApplication.class, args);
	}

	@Bean
	public RouteLocator gatewayRoutes(RouteLocatorBuilder routeLocatorBuilder) {
		return routeLocatorBuilder.routes()
				.route("auth-route", r -> r
						.path("/register", "/login", "/users", "/verify", "/forgotpassword", "/resetpassword")
						.filters(f -> f
								.addRequestHeader("From-Gateway", "true"))
						.uri("lb://AUTHENTICATION-SERVICE"))
				.build();
	}
}