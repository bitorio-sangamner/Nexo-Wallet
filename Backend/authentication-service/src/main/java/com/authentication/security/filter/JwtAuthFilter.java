package com.authentication.security.filter;

import com.authentication.security.jpaUserDetailsService.JpaUserDetailsService;
import com.authentication.exceptions.UnAuthorizedAccessException;
import com.authentication.security.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@AllArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private JwtUtil jwtUtil;

    private JpaUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String gatewayHeader = request.getHeader("From-Gateway");
        if (gatewayHeader == null || !gatewayHeader.equals("true")) {
            System.out.println(gatewayHeader + " - " + request.getHeaderNames());
            throw new UnAuthorizedAccessException("You are not authorized for this service", HttpStatus.UNAUTHORIZED.value());
        }

        Cookie[] cookies = request.getCookies();
        String token = null;
        String email = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("X-AuthToken".equals(cookie.getName())) {
                    token = cookie.getValue();
                    if (token != null) {
                        email = jwtUtil.claimsExtractEmail(token);
                    }
                    break;
                }
            }
        }

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            if (jwtUtil.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            } else {
                System.out.println();
            }
        }
        filterChain.doFilter(request, response);
    }
}