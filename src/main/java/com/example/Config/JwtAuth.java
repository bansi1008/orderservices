package com.example.Config;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;


import com.example.utility.JWT;
@Component
public class JwtAuth extends OncePerRequestFilter {
    private final JWT jwt;

    public JwtAuth(JWT jwt) {
        this.jwt = jwt;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
                                    throws ServletException, IOException {
        String token = extractToken(request);

        System.out.println("Extracted Token: " + token);

        if (token != null && jwt.validateToken(token)) {
            String email = jwt.getEmailFromToken(token);
            String role = jwt.getRoleFromToken(token);
            Long userId = jwt.getUserIdFromToken(token);
            String username=jwt.getUsernameFromToken(token);
            

           
            List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));

            UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userId, null, authorities);
                
            authentication.setDetails(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            System.out.println("Authentication set for user ID: " + userId + " with role: " + role);
        }  

        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (jakarta.servlet.http.Cookie cookie : request.getCookies()) {
                if ("token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    
    
    
}
