package ilogin.demo_jwt.Config;

import ilogin.demo_jwt.Jwt.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


import lombok.RequiredArgsConstructor;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * Habilita la configuración de seguridad web de Spring.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    // Inyectamos nuestro filtro personalizado de autenticación JWT.
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    // Inyectamos nuestro proveedor de autenticación (definido en ApplicationConfig).
    private final AuthenticationProvider authProvider;

    /**
     * Define la cadena de filtros de seguridad.
     * Esta es la configuración central de Spring Security, donde definimos qué
     * rutas están protegidas y cómo se maneja la autenticación.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
    {
        return http
                // Deshabilitamos la protección CSRF (Cross-Site Request Forgery)
                // Es común en APIs REST stateless donde no se usan cookies de sesión.
                .csrf(csrf ->
                        csrf
                                .disable())

                .cors(withDefaults())

                // Configuramos las reglas de autorización para las solicitudes HTTP.
                .authorizeHttpRequests(authRequest ->
                        authRequest
                                // Permite el acceso público a todas las rutas que comiencen con "/auth/**"
                                // (login, register, etc.)
                                .requestMatchers("/auth/**").permitAll()
                                // Exige que cualquier otra solicitud (como "/api/v1/demo")
                                // deba estar autenticada.
                                .anyRequest().authenticated()
                )

                // Configura la gestión de sesiones.
                .sessionManagement(sessionManager->
                        sessionManager
                                // Establece la política de creación de sesiones como STATELESS (sin estado).
                                // Esto es crucial para JWT: le decimos a Spring que no cree
                                // sesiones en el servidor, cada solicitud debe traer su token.
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Establece el proveedor de autenticación que Spring Security debe usar.
                .authenticationProvider(authProvider)

                // Añade nuestro filtro JWT *antes* del filtro de autenticación estándar.
                // Esto asegura que nuestro filtro intercepte y valide el token
                // antes de que Spring intente manejar la autenticación.
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

                // Construye el objeto SecurityFilterChain.
                .build();



    }
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Permite peticiones desde tu frontend de Angular
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Aplica a todas las rutas
        return source;
    }

}