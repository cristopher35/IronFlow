package com.ironflow.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http,
                                            @Value("${app.security.enabled:true}") boolean securityEnabled)
            throws Exception {
        http.csrf(csrf -> csrf.disable());

        if (!securityEnabled) {
            return http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll()).build();
        }

        return http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/actuator/health", "/doc/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/branch-app/**", "/equipment-app/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/member-app/**").hasAnyRole("ADMIN", "RECEPCIONISTA")
                        .requestMatchers(HttpMethod.PUT, "/member-app/**").hasAnyRole("ADMIN", "RECEPCIONISTA")
                        .requestMatchers(HttpMethod.DELETE, "/member-app/**").hasRole("ADMIN")
                        .requestMatchers("/payment-app/**").hasAnyRole("ADMIN", "RECEPCIONISTA")
                        .requestMatchers("/access-app/**").hasAnyRole("ADMIN", "RECEPCIONISTA")
                        .requestMatchers("/booking-app/**", "/class-app/**").hasAnyRole("ADMIN", "RECEPCIONISTA", "SOCIO")
                        .requestMatchers("/membership-app/**", "/trainer-app/**", "/notification-app/**")
                        .hasAnyRole("ADMIN", "RECEPCIONISTA")
                        .requestMatchers("/member-app/**").authenticated()
                        .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    @Bean
    UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        return new InMemoryUserDetailsManager(
                User.withUsername("admin")
                        .password(passwordEncoder.encode("admin123"))
                        .roles("ADMIN")
                        .build(),
                User.withUsername("socio")
                        .password(passwordEncoder.encode("socio123"))
                        .roles("SOCIO")
                        .build(),
                User.withUsername("recepcion")
                        .password(passwordEncoder.encode("recepcion123"))
                        .roles("RECEPCIONISTA")
                        .build()
        );
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
