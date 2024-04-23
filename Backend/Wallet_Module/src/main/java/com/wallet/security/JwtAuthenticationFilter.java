//package com.wallet.security;
//
//import io.jsonwebtoken.ExpiredJwtException;
//import io.jsonwebtoken.MalformedJwtException;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.*;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.web.client.RestTemplate;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
//
//@Component
//public class JwtAuthenticationFilter extends OncePerRequestFilter {
//
//    private Logger logger = LoggerFactory.getLogger(OncePerRequestFilter.class);
//    @Autowired
//    private JwtHelper jwtHelper;
//
//    @Autowired
//    private RestTemplate restTemplate;
//    @Autowired
//    private UserDetailsService userDetailsService;
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//
////        String AuthenticationMicroserviceHeader=request.getHeader("From-Authentication");
////        if (AuthenticationMicroserviceHeader!=null && AuthenticationMicroserviceHeader.equals("true")) {
////
////        }
//
//        String requestHeader = request.getHeader("Authorization");
//        //Bearer 2352345235sdfrsfgsdfsdf
//        logger.info(" Header :  {}", requestHeader);
//        String username = null;
//        String token = null;
//        if (requestHeader != null && requestHeader.startsWith("Bearer")) {
//            //looking good
//            token = requestHeader.substring(7);
//            try {
//
//                username = this.jwtHelper.getUsernameFromToken(token);
//
//            } catch (IllegalArgumentException e) {
//                logger.info("Illegal Argument while fetching the username !!");
//                e.printStackTrace();
//            } catch (ExpiredJwtException e) {
//                logger.info("Given jwt token is expired !!");
//                e.printStackTrace();
//            } catch (MalformedJwtException e) {
//                logger.info("Some changed has done in token !! Invalid Token");
//                e.printStackTrace();
//            } catch (Exception e) {
//                e.printStackTrace();
//
//            }
//
//
//        } else {
//            logger.info("Invalid Header Value !! ");
//        }
//
//
//        //
//        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//
//
//            //fetch user detail from username
//            //UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
//
//
//
//                String url = "http://localhost:9090/getUserDetails/{userName}";
//
//                // Create HttpHeaders object and set your custom header
//                HttpHeaders headers = new HttpHeaders();
//                headers.set("From-Wallet", "true");
//
//                // Create a ResponseEntity with UserDetails as the response type
//                ResponseEntity<UserDetails> responseEntity = restTemplate.exchange(
//                        url,
//                        HttpMethod.GET,
//                        new HttpEntity<>(headers),
//                        UserDetails.class,
//                        username
//                );
//
//                // Get UserDetails from ResponseEntity
//                UserDetails userDetails = responseEntity.getBody();
//            Boolean validateToken = this.jwtHelper.validateToken(token, userDetails);
//            if (validateToken) {
//
//                //set the authentication
//                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//
//
//            } else {
//                logger.info("Validation fails !!");
//            }
//
//
//        }
//
//        filterChain.doFilter(request, response);
//
//    }
//}
//
