package com.project.BidIT.Compontents;


import com.project.BidIT.Service.DeliveryMan.DeliveryManService;
import com.project.BidIT.entity.DeliveryMan;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class DeliveryManJwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final DeliveryManService deliveryManService;

    public DeliveryManJwtAuthenticationFilter(JwtUtil jwtUtil, DeliveryManService deliveryManService) {
        this.jwtUtil = jwtUtil;
        this.deliveryManService = deliveryManService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String jwt = null;

        if (request.getCookies() != null) {
            Cookie jwtCookie = Arrays.stream(request.getCookies())
                    .filter(c -> c.getName().equals("jwt_token"))
                    .findFirst().orElse(null);

            if (jwtCookie != null) jwt = jwtCookie.getValue();
        }

        if (jwt != null) {
            try {
                String email = jwtUtil.extractEmail(jwt);
                if (email != null) {
                    DeliveryMan dm = deliveryManService.findByDEmail(email);
                    if (dm != null && jwtUtil.isTokenValidDeliveryMan(jwt, dm)) {
                        UsernamePasswordAuthenticationToken authToken =
                                new UsernamePasswordAuthenticationToken(dm, null, new ArrayList<>());
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
            } catch (Exception e) {
                System.out.println("DeliveryMan JWT Error: " + e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }
}

