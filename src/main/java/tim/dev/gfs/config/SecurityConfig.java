package tim.dev.gfs.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import jakarta.servlet.http.HttpServletResponse;
import tim.dev.gfs.handler.CustomLogoutHandler;
import tim.dev.gfs.security.JwtAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final CustomLogoutHandler customLogoutHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(
            CustomLogoutHandler customLogoutHandler,
            JwtAuthenticationFilter jwtAuthenticationFilter) {

        this.customLogoutHandler = customLogoutHandler;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }
    
    @Bean
    AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .cors(Customizer.withDefaults())

            .csrf(csrf -> csrf.disable())

            .formLogin(form -> form.disable())

            .httpBasic(basic -> basic.disable())

            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            
            .addFilterBefore(
                    jwtAuthenticationFilter,
                    UsernamePasswordAuthenticationFilter.class)

            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers("/api/auth/login").permitAll()
                .requestMatchers("/api/auth/register").permitAll()
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            
            .exceptionHandling(exception -> exception
            	    .authenticationEntryPoint((request, response, authException) -> {
            	        System.out.println("Authentication failed: " + authException.getMessage());
            	        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            	    })
            	    .accessDeniedHandler((request, response, accessDeniedException) -> {
            	        System.out.println("Access denied: " + accessDeniedException.getMessage());
            	        response.sendError(HttpServletResponse.SC_FORBIDDEN);
            	    })
            	)

            .logout(logout -> logout
                .logoutUrl("/api/auth/logout")
                .addLogoutHandler(customLogoutHandler)
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID")
                .logoutSuccessHandler((request, response, authentication) -> {
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().write("Logout successful");
                })
            );

        return http.build();
    }
}