package com.gateway.filter;

import com.gateway.exception.AuthenticationException;
import com.gateway.util.JwtUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;


@Component
@Slf4j
@AllArgsConstructor
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private RouteValidator routeValidator;

    @Autowired
    private JwtUtil jwtUtil;
    @Override
    public GatewayFilter apply(Config config) {

        return (((exchange, chain) -> {
            if (routeValidator.isSecured.test(exchange.getRequest())) {
                Optional<HttpCookie> jwt = Optional.ofNullable(exchange.getRequest().getCookies().getFirst("X-AuthToken"));
                if (jwt.isEmpty()) {
                    System.out.println("Error");
                    throw new AuthenticationException("Missing authorization token.", HttpStatus.UNAUTHORIZED.value());
                }
                String authToken = jwt.get().getValue();
                if(!jwtUtil.validateToken(authToken)) {
                    log.error("Invalid access with jwt token: {}", authToken);
                    throw new AuthenticationException("Your session has been expired", HttpStatus.OK.value());
                }
            }
            return chain.filter(exchange);
        }));
    }

    public static class Config {}
}
