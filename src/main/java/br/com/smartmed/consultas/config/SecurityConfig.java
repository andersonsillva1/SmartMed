package br.com.smartmed.consultas.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Este Bean continua aqui, pois precisamos dele para o BCrypt.
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // ESTE É O NOVO BEAN QUE RESOLVE O SEU PROBLEMA
        return http
                // 1. Desabilita o CSRF. Essencial para que APIs REST funcionem com Postman.
                // CSRF é um tipo de proteção para aplicações web tradicionais, não para APIs.
                .csrf(AbstractHttpConfigurer::disable)

                // 2. Define as regras de autorização para as requisições.
                .authorizeHttpRequests(auth -> auth
                        // 3. Esta é a linha MÁGICA: permite TODAS as requisições (anyRequest)
                        // sem precisar de autenticação (permitAll).
                        .anyRequest().permitAll()
                )

                .build();
    }
}
