package com.restaurapp.restaurapp.controller;

import com.restaurapp.restaurapp.domain.model.User;
import com.restaurapp.restaurapp.domain.model.UserDetailsImpl;
import com.restaurapp.restaurapp.config.TokenUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Autenticación", description = "Endpoints para autenticación de usuarios")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    public AuthController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Operation(
            summary = "Iniciar sesión",
            description = "Autentica un usuario y retorna un token JWT junto con información del usuario"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Autenticación exitosa",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos de entrada inválidos"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Credenciales incorrectas"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor"
            )
    })
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

