package com.jwt.securitytutorial.filter.Config;

import com.jwt.securitytutorial.filter.JwtAuthentificationFilter;
import com.jwt.securitytutorial.service.UserDetailsServiceImp;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
 private final UserDetailsServiceImp userDetailsServiceImp;
 private final JwtAuthentificationFilter jwtAuthentificationFilter;

    public SecurityConfig(UserDetailsServiceImp userDetailsServiceImp, JwtAuthentificationFilter jwtAuthentificationFilter) {
        this.userDetailsServiceImp = userDetailsServiceImp;
        this.jwtAuthentificationFilter = jwtAuthentificationFilter;

    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws
    Exception{
        return http.
                csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        req->req.requestMatchers("/login/**,/register/**")
                                .permitAll()
                                .requestMatchers("/admin_only/**")
                                .hasAuthority("ADMIN")
                        .anyRequest()
                        .authenticated()
                ).userDetailsService(userDetailsServiceImp)
                .sessionManagement(session ->session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthentificationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
        return configuration.getAuthenticationManager();
    }



}