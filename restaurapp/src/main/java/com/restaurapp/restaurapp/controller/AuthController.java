package com.restaurapp.restaurapp.controller;

import com.restaurapp.restaurapp.domain.model.User;
import com.restaurapp.restaurapp.domain.model.UserDetailsImpl;
import com.restaurapp.restaurapp.config.TokenUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    public AuthController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        try {
            // Validar que email y contraseña no estén vacíos
            if (user.getEmail() == null || user.getEmail().trim().isEmpty() ||
                user.getPassword() == null || user.getPassword().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Email y contraseña son requeridos");
            }

            // Crear token de autenticación
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    user.getEmail().trim(),
                    user.getPassword().trim(),
                    Collections.emptyList()
            );

            // Autenticar
            Authentication authentication = authenticationManager.authenticate(authToken);
            
            // Obtener detalles del usuario
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            User authenticatedUser = userDetails.getUser();
            
            // Generar token JWT con el rol del usuario
            String role = authenticatedUser.getRole().getName();
            String token = TokenUtil.createToken(userDetails.getUsername(), role);
            
            // Crear respuesta con token en el body
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("email", authenticatedUser.getEmail());
            response.put("name", authenticatedUser.getName());
            response.put("role", role);
            response.put("userId", authenticatedUser.getUserId());
            
            return ResponseEntity.ok(response);
                    
        } catch (AuthenticationException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Credenciales incorrectas");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error interno del servidor: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}

