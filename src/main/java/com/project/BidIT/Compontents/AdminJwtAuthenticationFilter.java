package com.project.BidIT.Compontents;

import com.project.BidIT.Service.Admin.AdminService;
import com.project.BidIT.Service.Admin.CustomAdminService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class AdminJwtAuthenticationFilter extends OncePerRequestFilter {


    private  final JwtUtil jwtUtil;


    private final AdminService adminService;

    public AdminJwtAuthenticationFilter(JwtUtil jwtUtil, AdminService adminService) {
        this.jwtUtil = jwtUtil;
        this.adminService = adminService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String jwt = null;

        // 1️⃣ Read JWT from cookie
        if (request.getCookies() != null) {
            Cookie jwtCookie = Arrays.stream(request.getCookies())
                    .filter(c -> c.getName().equals("admin_jwt_token"))
                    .findFirst()
                    .orElse(null);

            if (jwtCookie != null) {
                jwt = jwtCookie.getValue();
                System.out.println("Admin JWT found in cookie: " + jwt);
            } else {
                System.out.println("No JWT cookie found for Admin");
            }
        }

        if (jwt != null) {
            try {
                // 2️⃣ Extract email from token
                String email = jwtUtil.extractEmail(jwt);

                if (email != null) {

                    // 3️⃣ Load Admin (NOT UserDetails)
                    var admin = adminService.findAdminByEmail(email); // <-- YOU MUST HAVE THIS
                    if (admin != null && jwtUtil.isTokenValidAdmin(jwt, admin)) {

                        // 4️⃣ Create Auth object manually
                        UsernamePasswordAuthenticationToken authToken =
                                new UsernamePasswordAuthenticationToken(
                                        admin,
                                        null,
                                        new ArrayList<>() // no roles needed
                                );

                        authToken.setDetails(
                                new WebAuthenticationDetailsSource().buildDetails(request)
                        );

                        // 5️⃣ Set Authentication
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }

            } catch (Exception e) {
                System.out.println("Admin JWT Error: " + e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }
}

