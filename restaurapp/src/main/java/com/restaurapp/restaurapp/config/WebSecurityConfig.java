package com.restaurapp.restaurapp.config;

import com.restaurapp.restaurapp.service.user.LoginUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final LoginUserService loginUserService;
    private final JWTAuthorizationFilter jwtAuthorizationFilter;


    public WebSecurityConfig(LoginUserService loginUserService, JWTAuthorizationFilter jwtAuthorizationFilter) {
        this.loginUserService = loginUserService;
        this.jwtAuthorizationFilter = jwtAuthorizationFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authManager) throws Exception {
        JWTAuthenticationFilter jwtAuthenticationFilter = new JWTAuthenticationFilter();
        jwtAuthenticationFilter.setAuthenticationManager(authManager);
        jwtAuthenticationFilter.setFilterProcessesUrl("/login");
        return http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/login").permitAll()
                        
                        // Swagger UI
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                      
                        .requestMatchers("/ws/**").permitAll()
                        .requestMatchers("/websocket-client-example.html").permitAll()
                        .requestMatchers("/static/**").permitAll()
                        
                        // Archivos estáticos de Angular
                        .requestMatchers("/", "/index.html", "/favicon.ico", "/*.js", "/*.css", "/chunk-*.js", "/polyfills-*.js", "/main-*.js", "/styles-*.css").permitAll()
                        
                        .requestMatchers(HttpMethod.POST, "/role", "/user").permitAll()
                        
                      
                        .requestMatchers("/user/**", "/role/**").hasRole("ADMIN")
                        
                    
                        .requestMatchers(HttpMethod.GET, "/category/**").authenticated()
                        .requestMatchers("/category/**").hasRole("ADMIN")
                        
            
                        .requestMatchers(HttpMethod.GET, "/product/**").authenticated()
                        .requestMatchers("/product/**").hasRole("ADMIN")
                        
                        
                        .requestMatchers(HttpMethod.GET, "/status/**").authenticated()
                        .requestMatchers("/status/**").hasRole("ADMIN")
                        
                      
                        .requestMatchers("/table/**").hasAnyRole("ADMIN", "WAITER")
                        
                    
                        .requestMatchers(HttpMethod.GET, "/order-ticket/**").hasAnyRole("ADMIN", "WAITER", "CHEF")
                        .requestMatchers(HttpMethod.PATCH, "/order-ticket/**").hasAnyRole("ADMIN", "WAITER", "CHEF")
                        .requestMatchers("/order-ticket/**").hasAnyRole("ADMIN", "WAITER")
                        
                    
                        .requestMatchers(HttpMethod.GET, "/order-item/**").hasAnyRole("ADMIN", "WAITER", "CHEF")
                        .requestMatchers("/order-item/**").hasAnyRole("ADMIN", "WAITER")
                        
                 
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilter(jwtAuthenticationFilter)
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authManager() throws Exception {
        return new ProviderManager(authenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(loginUserService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Permitir peticiones desde Angular en localhost:4200
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
        
        // Permitir todos los métodos HTTP
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        
        // Permitir todos los headers
        configuration.setAllowedHeaders(Arrays.asList("*"));
        
        // Permitir credenciales (cookies, authorization headers)
        configuration.setAllowCredentials(true);
        
        // Exponer headers en la respuesta (importante para el token)
        configuration.setExposedHeaders(Arrays.asList("Authorization", "Content-Type"));
        
        // Aplicar configuración a todas las rutas
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }
}
