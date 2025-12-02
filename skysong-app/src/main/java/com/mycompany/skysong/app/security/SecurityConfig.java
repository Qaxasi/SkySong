package com.mycompany.skysong.app.security.exception;

import com.mycompany.skysong.app.security.filter.JwtAuthenticationFilter;
import com.mycompany.skysong.app.security.filter.SessionVersionValidationFilter;
import com.mycompany.skysong.app.security.handler.CustomAccessDeniedHandler;
import com.mycompany.skysong.app.security.handler.CustomAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final SessionVersionValidationFilter sessionVersionValidationFilter;
    private final SecurityProperties securityProperties;

    public SecurityConfig(final CustomAuthenticationEntryPoint authenticationEntryPoint,
                          final CustomAccessDeniedHandler accessDeniedHandler,
                          final JwtAuthenticationFilter jwtAuthenticationFilter,
                          final SessionVersionValidationFilter sessionVersionValidationFilter,
                          final SecurityProperties securityProperties) {

        this.authenticationEntryPoint = authenticationEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.sessionVersionValidationFilter = sessionVersionValidationFilter;
        this.securityProperties = securityProperties;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(securityProperties.getExcludePaths().toArray(String[]::new))
                        .permitAll()
                        .requestMatchers(HttpMethod.DELETE,
                                "/api/v1/users/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler)
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(sessionVersionValidationFilter, JwtAuthenticationFilter.class);

        return http.build();
    }
}
