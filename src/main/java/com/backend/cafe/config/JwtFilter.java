package com.backend.cafe.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Slf4j
@Component //converts JwtFilter into Bean to be recognized by the SecurityConfig class
public class JwtFilter extends OncePerRequestFilter {//Extend from abstract class OncePerRequestFilter because the idea is to activate the token, process it in filter chain, and execute the filter ONLY once...

    @Autowired
    private JWTService jwtService; //Create instance object of JWTService to call extractUserName() method...

    @Autowired
    ApplicationContext context; //Here we use context to get the bean of UserDetails.class

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // All three declared as LOCAL variables — scoped to this request only
        // No longer class-level fields — no sharing between requests, no stale data
        String token = null;
        String username = null;
        Claims claims = null;
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {//In case the header includes the word, "Bearer ", we need to use a substring to ignore this prefix in the token...
            token = authHeader.substring(7); // substring to start index of token where the prefix, "Bearer ", is NOT included...
            try {
                username = jwtService.extractUserName(token); //create a method in the JWTService class to extract the token for username...
                claims = jwtService.extractAllClaims(token);
            } catch (ExpiredJwtException e) {
                log.warn("JWT token has expired: {}", e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\":\"Token expired. Please log in again.\"}");
                return;
            } catch (Exception e) {
                log.warn("Invalid JWT token: {}", e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\":\"Invalid token.\"}");
                return;
            }
        }
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {//want to make sure the username is not null and is not already authenticated!
            UserDetails userDetails = context.getBean(MyUserDetailsService.class).loadUserByUsername(username); // username should be existing in table in database... We use instance object of MyUserDetailsService class to load/find specific user in database with username to be verified.
            if (jwtService.validateToken(token, userDetails)) {// We need to validate the token and if it is valid then we create authentication object.
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource() //Here the authToken object should reflect data from the request object being sent for verification.
                        .buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);//Once you have verified the authToken object, you have to set the authentication in the SecurityContextHolder.
            }
        }
        filterChain.doFilter(request, response); //Once it is verified you will pass filter with both request and response objects.
    }


    public boolean isAdmin() {
        Claims claims = extractClaimsFromContext();
        return claims != null && "admin".equals(claims.get("role"));
    }

    public boolean isUser() {
        Claims claims = extractClaimsFromContext();
        return claims != null && "user".equals(claims.get("role"));
    }

    public String getCurrentUser() {
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
    }

    private Claims extractClaimsFromContext() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes)
                    RequestContextHolder.getRequestAttributes()).getRequest();
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                return jwtService.extractAllClaims(token);
            }
        } catch (Exception e) {
            log.warn("Could not extract claims from current request: {}", e.getMessage());
        }
        return null;
    }
}
