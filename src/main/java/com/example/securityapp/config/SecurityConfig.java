package com.example.securityapp.config;

import com.example.securityapp.security.JwtAuthEntryPoint;
import com.example.securityapp.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true) // Habilita a segurança baseada em métodos com @PreAuthorize
public class SecurityConfig {

    private JwtAuthEntryPoint authenticationEntryPoint;
    private JwtAuthenticationFilter authenticationFilter;

    public SecurityConfig(JwtAuthEntryPoint authenticationEntryPoint, JwtAuthenticationFilter authenticationFilter) {
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.authenticationFilter = authenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Desabilita CSRF para APIs RESTful
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint(authenticationEntryPoint) // Lida com erros de autenticação
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Aplicação stateless (sem sessões)
            )
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/api/auth/**").permitAll() // Permite acesso público a endpoints de autenticação
                .requestMatchers("/api/products/**").permitAll() // Permite acesso público a endpoints de produtos (listagem e detalhes)
                .requestMatchers("/api/clients").permitAll() // Permite acesso público ao cadastro de clientes (registerClient)
                .requestMatchers("/api/clients/{id}").permitAll() // Permite acesso público a detalhes de clientes (para o próprio cliente) - será ajustado com @PreAuthorize)
                .requestMatchers("/api/orders/**").authenticated() // Pedidos exigem autenticação
                .requestMatchers("/api/coupons/**").hasRole("ADMIN") // Cupons exigem ADMIN
                .requestMatchers("/api/discount-rules/**").hasRole("ADMIN") // Regras de desconto exigem ADMIN
                .anyRequest().authenticated() // Todas as outras requisições exigem autenticação
            );

        http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

