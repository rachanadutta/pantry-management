package com.example.pantry.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{
    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;
    public JwtAuthenticationFilter(JwtService jwtService, CustomUserDetailsService customUserDetailsService) {
        this.jwtService = jwtService;
        this.customUserDetailsService = customUserDetailsService;
    }
    @Override
protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
) throws ServletException, IOException {
    String authHeader= request.getHeader("Authorization");
    if(authHeader!=null && authHeader.startsWith("Bearer ")){
        String token=authHeader.substring(7);
        try{
        String email=jwtService.extractUsername(token);
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
        //email is the email exttracted from token in the request and userDetails.getUsername() is the email of the user fetched from the database. If they match, it means the token is valid for that user.
        if(email!=null && jwtService.validateToken(email, userDetails.getUsername())){
            // Token is valid, you can set the authentication in the security context if needed
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
    }
    }
    catch (JwtException e){
        // Handle invalid token
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("Invalid or expired JWT token");
        return;

    }
    
}
filterChain.doFilter(request, response);
}
@Override
protected boolean shouldNotFilter(HttpServletRequest request) {
    String path = request.getServletPath();

    return path.equals("/api/auth/login")
        || path.equals("/api/auth/register")
        || path.equals("/api/auth/refresh")
        || path.equals("/api/auth/logout")
        || path.startsWith("/oauth2/")
        || path.startsWith("/login/oauth2/")
        || path.equals("/api/auth/google/exchange");
}
}
