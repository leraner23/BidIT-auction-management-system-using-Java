package com.project.BidIT.Compontents;

import com.project.BidIT.entity.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.project.BidIT.Service.User.CustomUserService;

import java.io.IOException;
import java.util.Arrays;


public class JwtAuthenticationFilter extends OncePerRequestFilter {


    private final JwtUtil jwtUtil;


    private final CustomUserService customUserService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil,CustomUserService customUserService){
        this.jwtUtil = jwtUtil;
        this.customUserService = customUserService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String jwt = null;

        // 1️⃣ Read JWT from Cookie named "jwt"
        if (request.getCookies() != null) {
            Cookie jwtCookie = Arrays.stream(request.getCookies())
                    .filter(c -> c.getName().equals("jwt_token"))
                    .findFirst()
                    .orElse(null);
            if (request.getCookies() != null) {
                System.out.println("Cookies in request: ");
                for (Cookie c : request.getCookies()) {
                    System.out.println(c.getName() + " = " + c.getValue());
                }
            } else {
                System.out.println("request.getCookies() is null");
            }


            if (jwtCookie != null) {
                jwt = jwtCookie.getValue();
                System.out.println("JWT found in cookie: " + jwt);
            }else {
                System.out.println("No JWT cookie found");
            }
        } else {
            System.out.println("No cookies in request");
        }


        if (jwt != null) {
            try {
                // 2️⃣ Extract email from token
                String email = jwtUtil.extractEmail(jwt);

                // 3️⃣ Load user details
                UserDetails userDetails = customUserService.loadUserByUsername(email);

                // 4️⃣ Validate token
                if (jwtUtil.isTokenValid(jwt,userDetails)) {

                    // 5️⃣ Create Authentication object
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    // 6️⃣ MOST IMPORTANT — set authentication
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }

            } catch (Exception e) {
                System.out.println("JWT Error: " + e.getMessage());
            }
        }

        // Continue filter chain
        filterChain.doFilter(request, response);
    }
}
