package com.example.business_management;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtTokenFilter jwtTokenFilter;

    public SecurityConfig(JwtTokenFilter jwtTokenFilter) {
        this.jwtTokenFilter = jwtTokenFilter;
    }

    @Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .cors()  // Enable CORS
        .and()
        .csrf().disable()  // Disable CSRF as JWT is stateless
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)  // Ensure session is stateless
        .and()
        .authorizeHttpRequests((authz) -> authz
            // Allow public access to login and register endpoints
            .requestMatchers("/api/login", "/api/register").permitAll()

            // Allow only employees to access the applications list
            .requestMatchers("/api/applications/all").hasRole("EMPLOYEE")

            // Allow only admins to access admin-specific endpoints
            .requestMatchers("/api/admin/**").hasRole("ADMIN")

            // Any other request needs authentication
            .anyRequest().authenticated()
        )
        .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);  // Add JWT filter before default authentication filter

    return http.build();
}
}
