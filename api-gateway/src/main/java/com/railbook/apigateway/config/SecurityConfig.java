package com.railbook.apigateway.config;

import com.railbook.apigateway.config.KeycloakRoleConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        // Public auth endpoints
                        .pathMatchers("/api/users/login", "/api/users/register").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/trains/**").permitAll()

                        // Protected role endpoints
                        .pathMatchers(HttpMethod.POST, "/api/trains/**").hasRole("ADMIN")
                        .pathMatchers("/api/bookings/**").hasAnyRole("USER", "ADMIN")

                        // Fallback
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(new KeycloakRoleConverter()))
                );

        return http.build();
    }
}