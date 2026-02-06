package com.example.first_app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    /**
     * Définition du bean PasswordEncoder avec BCrypt
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Authentification en mémoire
     */
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin123"))
                .roles("ADMIN", "EMPLOYEE") // L'admin a tous les rôles
                .build();
        
        UserDetails employee = User.builder()
                .username("employee")
                .password(passwordEncoder.encode("employee123"))
                .roles("EMPLOYEE")
                .build();
        
        return new InMemoryUserDetailsManager(admin, employee);
    }

    /**
     * Configuration de la sécurité HTTP
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                // Les ressources statiques sont accessibles à tous
                .requestMatchers("/", "/auth.html", "/style.css", "/auth.css", "/employes.css", 
                               "/employes.js", "/js/**", "/css/**", "/images/**", "/fonts/**").permitAll()
                // H2 console pour le développement
                .requestMatchers("/h2-console/**").permitAll()
                // L'API pour les employés nécessite une authentification
                .requestMatchers("/api/employes/**").authenticated()
                // Toutes les autres requêtes
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/auth.html")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/employes.html", true)
                .failureUrl("/auth.html?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/auth.html")
                .permitAll()
            )
            .csrf(csrf -> csrf
                .disable()
            )
            .headers(headers -> headers
                .frameOptions(frame -> frame.sameOrigin()) // Pour H2 console
            );

        return http.build();
    }
}