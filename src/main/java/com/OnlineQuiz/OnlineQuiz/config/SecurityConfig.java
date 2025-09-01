package com.OnlineQuiz.OnlineQuiz.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.CrossOriginOpenerPolicyHeaderWriter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        @Autowired
        private JwtAuthFilter jwtAuthFilter;

        @Autowired
        private UserDetailsService userDetailsService;

        @Bean
        public AuthenticationEntryPoint jwtAuthenticationEntryPoint() {
                return (request, response, authException) -> {
                        response.setContentType("application/json");
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.getWriter().println(
                                        "{ \"status\": \"error\", \"message\": \"Unauthorized: "
                                                        + authException.getMessage() + "\" }");
                };
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                // Disable CSRF for stateless API
                                .csrf(csrf -> csrf.disable())

                                // Configure CORS
                                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                                // Configure headers security
                                .headers(headers -> headers
                                                // Disable frame options if using iframes
                                                .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)

                                                // Configure COOP policy
                                                .crossOriginOpenerPolicy(coop -> coop
                                                                .policy(CrossOriginOpenerPolicyHeaderWriter.CrossOriginOpenerPolicy.SAME_ORIGIN_ALLOW_POPUPS))

                                                // Optional: Configure CSP
                                                .contentSecurityPolicy(csp -> csp
                                                                .policyDirectives(
                                                                                "default-src 'self'; script-src 'self' 'unsafe-inline'; style-src 'self' 'unsafe-inline'; img-src 'self' data:")))

                                // Configure authorization rules
                                .authorizeHttpRequests(requests -> requests
                                                // Public endpoints
                                                .requestMatchers(
                                                                "/ws/**",
                                                                "/auth/google",
                                                                "/login/oauth2/code/**",
                                                                "/api/auth/**",
                                                                "/api/register",
                                                                "/api/login",
                                                                "/auth/github",
                                                                "/api/home",
                                                                "/home",
                                                                "/quiz/join-room",
                                                                "/quiz/questions/**",
                                                                "/api/send",
                                                                "/api/verify",
                                                                "/api/resend",
                                                                 "/api/reset-password"

                                                ).permitAll()

                                                // Authenticated endpoints
                                                .requestMatchers(
                                                                "/quiz/create",
                                                                "/quiz/exam/create",
                                                                "/quiz/leave/**",
                                                                "/api/home",
                                                                "/api/exam/**",
                                                                "/api/quizzes/**",
                                                                "/api/edit-profile",
                                                                "/api/user",
                                                                "/send",
                                                                "/profile/**")
                                                .authenticated()

                                                // All other requests require authentication
                                                .anyRequest().authenticated())

                                // Configure session management
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                                // Configure authentication provider
                                .authenticationProvider(authenticationProvider())

                                // Add JWT filter
                                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

                                // Configure logout
                                .logout(logout -> logout
                                                .logoutUrl("/api/logout")
                                                .logoutSuccessHandler(
                                                                (request, response, authentication) -> response
                                                                                .setStatus(HttpServletResponse.SC_OK))
                                                .permitAll())

                                // Configure exception handling
                                .exceptionHandling(exceptions -> exceptions
                                                .authenticationEntryPoint(jwtAuthenticationEntryPoint()));

                return http.build();
        }

        @Bean
        CorsConfigurationSource corsConfigurationSource() {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOrigins(List.of(
                                "onlinequizwin.netlify.app",
                                "http://127.0.0.1:5502",
                                "http://localhost:5502",
                                "http://127.0.0.1:5504",
                                "http://localhost:5504",
                                "http://127.0.0.1:5501",
                                "http://localhost:5501",
                                "https://heroic-sunburst-56c10d.netlify.app",
                                "http://quizwiz-frontend.s3-website.ap-south-1.amazonaws.com",
                                "https://quizwiz-frontend.s3-website.ap-south-1.amazonaws.com"));
                configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                configuration.setAllowedHeaders(Arrays.asList("*"));
                configuration.setAllowCredentials(true);
                configuration.setExposedHeaders(Arrays.asList("Authorization"));

                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);
                return source;
        }

        @Bean
        public DaoAuthenticationProvider authenticationProvider() {
                DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
                authProvider.setUserDetailsService(userDetailsService);
                authProvider.setPasswordEncoder(passwordEncoder());
                return authProvider;
        }

        @Bean
        public BCryptPasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
                return config.getAuthenticationManager();
        }
}