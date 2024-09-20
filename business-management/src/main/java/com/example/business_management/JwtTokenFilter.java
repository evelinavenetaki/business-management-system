package com.example.business_management;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;


@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;  // Utility to handle JWT parsing and validation
    private final UserDetailsService userDetailsService;  // Your user details service to load user info

    public JwtTokenFilter(JwtTokenUtil jwtTokenUtil, UserDetailsService userDetailsService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Get Authorization header and validate it
        String authorizationHeader = request.getHeader("Authorization");

        String token = null;
        String username = null;

        // Check if the Authorization header starts with "Bearer "
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);  // Extract JWT
            try {
                // Extract the username from the token
                username = jwtTokenUtil.extractUsername(token);
                logger.info("JWT Token extracted successfully. Username: " + username);
            } catch (Exception e) {
                logger.error("Error parsing JWT token", e);  // Log any errors
            }
        } else {
            logger.warn("Authorization header is missing or does not start with 'Bearer '");
        }

        // Proceed with authentication and authorization based on the JWT
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            logger.info("User details loaded for: " + username);

            // Validate token
            if (jwtTokenUtil.validateToken(token, userDetails)) {
                logger.info("Token validated successfully for user: " + username);
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            } else {
                logger.warn("Token validation failed for user: " + username);
            }
        }

        // Continue the filter chain
        filterChain.doFilter(request, response);
    }
}
