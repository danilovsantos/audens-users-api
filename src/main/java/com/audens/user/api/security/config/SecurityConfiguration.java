package com.audens.user.api.security.config;

import com.audens.user.api.security.authentication.UserAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private UserAuthenticationFilter userAuthenticationFilter;

    private static final String ROLE_ADMINISTRATOR = "ADMINISTRATOR";

    public static final String [] ENDPOINTS_WITH_AUTHENTICATION_NOT_REQUIRED = {
            "/users/login"
    };

    public static final String [] ENDPOINTS_WITH_AUTHENTICATION_REQUIRED_TO_GET_STATUS = {
            "/users"
    };

    private static final String [] ENDPOINTS_WITH_AUTHENTICATION_REQUIRED_TO_POST_STATUS = {
            "/users"
    };

    private static final String [] ENDPOINTS_AVAILABLE_FOR_ADMIN_ONLY_TO_GET_STATUS = {
            "/users",
            "/users/{userId}"
    };

    private static final String [] ENDPOINTS_AVAILABLE_FOR_ADMIN_ONLY_TO_POST_STATUS = {
            "/users",
            "/users/{userId}"
    };

    private static final String [] ENDPOINTS_AVAILABLE_FOR_ADMIN_ONLY_TO_PUT_STATUS = {
            "/users",
            "/users/{userId}"
    };

    private static final String [] ENDPOINTS_AVAILABLE_FOR_ADMIN_ONLY_TO_PATCH_STATUS = {
            "/users"
    };

    private static final String [] ENDPOINTS_AVAILABLE_FOR_ADMIN_ONLY_TO_DELETE_STATUS = {
            "/users/{userId}"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.csrf().disable() // Desativa a proteção contra CSRF
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Configura a política de criação de sessão como stateless
                .and().authorizeHttpRequests() // Habilita a autorização para as requisições HTTP
                .requestMatchers(ENDPOINTS_WITH_AUTHENTICATION_NOT_REQUIRED).permitAll()
                .requestMatchers(HttpMethod.GET, ENDPOINTS_WITH_AUTHENTICATION_REQUIRED_TO_GET_STATUS).authenticated()
                .requestMatchers(HttpMethod.POST, ENDPOINTS_WITH_AUTHENTICATION_REQUIRED_TO_POST_STATUS).authenticated()
                .requestMatchers(HttpMethod.GET, ENDPOINTS_AVAILABLE_FOR_ADMIN_ONLY_TO_GET_STATUS).hasAnyRole(ROLE_ADMINISTRATOR)
                .requestMatchers(HttpMethod.POST, ENDPOINTS_AVAILABLE_FOR_ADMIN_ONLY_TO_POST_STATUS).hasAnyRole(ROLE_ADMINISTRATOR)
                .requestMatchers(HttpMethod.PUT, ENDPOINTS_AVAILABLE_FOR_ADMIN_ONLY_TO_PUT_STATUS).hasAnyRole(ROLE_ADMINISTRATOR)
                .requestMatchers(HttpMethod.PATCH, ENDPOINTS_AVAILABLE_FOR_ADMIN_ONLY_TO_PATCH_STATUS).hasAnyRole(ROLE_ADMINISTRATOR)
                .requestMatchers(HttpMethod.DELETE, ENDPOINTS_AVAILABLE_FOR_ADMIN_ONLY_TO_DELETE_STATUS).hasAnyRole(ROLE_ADMINISTRATOR)
                .anyRequest().denyAll()
                // Adiciona o filtro de autenticação de usuário que criamos antes do filtro de segurança padrão do Spring Security
                .and().addFilterBefore(userAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}