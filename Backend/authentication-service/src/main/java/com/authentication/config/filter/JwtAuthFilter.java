package com.authentication.config.filter;

import com.authentication.config.jpaUserDetailsService.JpaUserDetailsService;
import com.authentication.dto.ApiResponse;
import com.authentication.exceptions.UnAuthorizedAccessException;
import com.authentication.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private JwtUtil jwtUtil;

    private static final Logger logger= LoggerFactory.getLogger(JwtAuthFilter.class);

    private JpaUserDetailsService userDetailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        logger.info("************inside doFilterInternal*************");

        String gatewayHeader = request.getHeader("From-Gateway");
        String microserviceHeader=request.getHeader("From-Wallet");

        //System.out.println(request.getHeaderNames());
        logger.info("header :"+gatewayHeader);
        logger.info("header name :"+request.getHeaderNames());

        if ((gatewayHeader == null || !(gatewayHeader.equals("true"))) && (microserviceHeader==null || !(microserviceHeader.equals("true")))) {
            throw new UnAuthorizedAccessException("You are not authorized for this service", HttpStatus.UNAUTHORIZED);
        }

        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        if (authHeader != null && authHeader.startsWith("Bearer")) {
            token = authHeader.substring(7);
            username = jwtUtil.claimsExtractUsername(token);
        }


        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (jwtUtil.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
