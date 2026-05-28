package com.training.trainingcenterboot.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Данный класс используется для АВТОРИЗАЦИИ
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                .csrf(AbstractHttpConfigurer::disable)

                //Так как MVC включили в проекте, чтобы браузер сам всегда имел токен в запросах,
                // нам нужно убрать этот кусок - он не дает, чтобы браузер хранил кууки - токен
//                .sessionManagement(session ->
//                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                )

                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(
                                "/",
                                "/mvc/login",
                                "/mvc/student",
                                "/mvc/teacher",
                                "/mvc/admin",
                                "/mvc/register/**",
                                "/css/**",
                                "/images/**",
                                "/js/**",
                                "/auth/**",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()

                        .requestMatchers("/mvc/admin/**").hasRole("ADMIN")
                        .requestMatchers("/mvc/student/**").hasRole("STUDENT")
                        .requestMatchers("/mvc/teacher/**").hasRole("TEACHER")
                        .requestMatchers("/mvc/students/**").hasAnyRole("ADMIN", "TEACHER")
                        .requestMatchers("/mvc/teachers/**").hasRole("ADMIN")
                        .requestMatchers("/mvc/courses/create").hasRole("ADMIN")
                        .requestMatchers("/mvc/courses/edit/**").hasRole("ADMIN")
                        .requestMatchers("/mvc/courses/delete/**").hasRole("ADMIN")
                        .requestMatchers("/mvc/courses/**").hasAnyRole("ADMIN", "TEACHER", "STUDENT")
                        .requestMatchers("/mvc/enrollments/**").hasAnyRole("ADMIN", "TEACHER", "STUDENT")
                        .requestMatchers("/mvc/dashboard").authenticated()

                        .requestMatchers("/api/students/**").hasAnyRole("STUDENT", "ADMIN")
                        .requestMatchers("/api/teachers/**").hasAnyRole("TEACHER", "ADMIN")
                        .requestMatchers("/api/courses/**").hasAnyRole("STUDENT", "TEACHER", "ADMIN")

                        .requestMatchers("/api/enrollments/**").hasAnyRole("STUDENT", "ADMIN", "TEACHER")
                        .requestMatchers("/api/admins/**").hasRole("ADMIN")
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )

                .formLogin(form -> form
                        .loginPage("/mvc/login")
                        .loginProcessingUrl("/mvc/login")
                        .defaultSuccessUrl("/mvc/dashboard", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/mvc/login")
                )

//                .httpBasic(Customizer.withDefaults())

                // Добавляем наш JWT filter в Security Filter Chain
                // Перед стандартным filter логина
                // запусти наш JwtAuthenticationFilter
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                )
                .build();
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