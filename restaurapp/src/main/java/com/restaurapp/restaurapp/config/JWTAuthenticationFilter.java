package com.restaurapp.restaurapp.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restaurapp.restaurapp.domain.model.User;
import com.restaurapp.restaurapp.domain.model.UserDetailsImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collections;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException{
        User user = new User();
        try {
            user = new ObjectMapper().readValue(request.getReader(), User.class);
        } catch (IOException e){}

        // Validar que email y contraseña no estén vacíos
        String email = user.getEmail() != null ? user.getEmail().trim() : "";
        String password = user.getPassword() != null ? user.getPassword().trim() : "";
        
        if (email.isEmpty() || password.isEmpty()) {
            throw new RuntimeException("Email y contraseña son requeridos");
        }

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                email,
                password,
                Collections.emptyList()
        );

        return getAuthenticationManager().authenticate(usernamePasswordAuthenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authentication) throws IOException, ServletException {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String role = userDetails.getUser().getRole().getName();
        String token = TokenUtil.createToken(userDetails.getUsername(), role);

        response.addHeader("Authorization", "Bearer "+token);
        response.getWriter().flush();
        super.successfulAuthentication(request,response,chain,authentication);
    }
}
