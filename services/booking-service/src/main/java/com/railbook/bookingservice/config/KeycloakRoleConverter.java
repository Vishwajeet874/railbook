package com.railbook.bookingservice.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class KeycloakRoleConverter
        implements Converter<Jwt, AbstractAuthenticationToken> {

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {

        Collection<SimpleGrantedAuthority> authorities =
                new ArrayList<>();

        Map<String, Object> realmAccess =
                jwt.getClaim("realm_access");

        if (realmAccess != null) {

            Object rolesObject =
                    realmAccess.get("roles");

            if (rolesObject instanceof Collection<?> roles) {

                roles.forEach(role -> {

                    authorities.add(
                            new SimpleGrantedAuthority(
                                    "ROLE_" + role.toString()
                            )
                    );
                });
            }
        }

        return new JwtAuthenticationToken(
                jwt,
                authorities
        );
    }
}