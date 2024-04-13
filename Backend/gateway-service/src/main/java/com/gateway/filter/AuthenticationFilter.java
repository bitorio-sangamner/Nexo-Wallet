package com.gateway.filter;

import com.gateway.exception.AuthenticationException;
import com.gateway.util.JwtUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Objects;


@Component
@Slf4j
@AllArgsConstructor
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private RouteValidator routeValidator;

    private JwtUtil jwtUtil;
    public AuthenticationFilter() {
        super();
    }

    @Override
    public GatewayFilter apply(Config config) {

        return (((exchange, chain) -> {
            if (routeValidator.isSecured.test(exchange.getRequest())) {
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new AuthenticationException("Missing authorization header.", HttpStatus.UNAUTHORIZED);
                }

                String authHeader = Objects.requireNonNull(exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION)).get(0);
                if (!(authHeader != null && authHeader.startsWith("Bearer "))) {
                    log.error("Invalid access with no jwt token");
                    throw new AuthenticationException("Unauthorized access to with no jwt token.", HttpStatus.UNAUTHORIZED);
                }
                authHeader = authHeader.substring(7);

                if(!jwtUtil.validateToken(authHeader)) {
                    log.error("Invalid access with jwt token: {}", authHeader);
                    throw new AuthenticationException("Unauthorized access to with wrong jwt token.", HttpStatus.UNAUTHORIZED);
                }
            }
            return chain.filter(exchange);
        }));
    }

    public static class Config {

    }
}