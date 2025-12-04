package com.project.BidIT.Config;

import com.project.BidIT.Compontents.AdminJwtAuthenticationFilter;
import com.project.BidIT.Compontents.DeliveryManJwtAuthenticationFilter;
import com.project.BidIT.Compontents.JwtAuthenticationFilter;
import com.project.BidIT.Compontents.JwtUtil;
import com.project.BidIT.Service.Admin.AdminService;
import com.project.BidIT.Service.DeliveryMan.DeliveryManService;
import com.project.BidIT.Service.User.CustomUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final CustomUserService customUserService;
    private final JwtUtil jwtUtil;


    public SecurityConfig(CustomUserService customUserService, JwtUtil jwtUtil) {
        this.customUserService = customUserService;
        this.jwtUtil = jwtUtil;

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DeliveryManJwtAuthenticationFilter deliveryManJwtAuthenticationFilter(DeliveryManService dmService) {
        return new DeliveryManJwtAuthenticationFilter(jwtUtil, dmService);
    }
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtUtil, customUserService);
    }
    @Bean
    public AdminJwtAuthenticationFilter adminJwtAuthenticationFilter(AdminService adminService) {
        return new AdminJwtAuthenticationFilter(jwtUtil, adminService);
    }
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF (JWT is stateless)
                .csrf(csrf -> csrf.disable())

                // Authorization rules
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/","/favicon.ico", "/Styles/**", "/js/**", "/images/**",
                                "/user/register", "/user/login","/admin/login","/admin/register","/delivery/login","/delivery/register").permitAll()
                        .anyRequest().authenticated() // all other endpoints require login
                )

                // Add JWT filter before Spring's UsernamePasswordAuthenticationFilter
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(adminJwtAuthenticationFilter(null), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(deliveryManJwtAuthenticationFilter(null), UsernamePasswordAuthenticationFilter.class)
        // Disable Spring form login
                .formLogin(form -> form.disable())

                // Logout configuration
                .logout(logout -> logout.disable());

        return http.build();
    }
}
