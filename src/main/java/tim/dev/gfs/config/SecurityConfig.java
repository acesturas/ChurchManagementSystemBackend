package tim.dev.gfs.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.servlet.http.HttpServletResponse;
import tim.dev.gfs.handler.CustomLogoutHandler;

@Configuration
public class SecurityConfig {

    private final CustomLogoutHandler customLogoutHandler;

	SecurityConfig(CustomLogoutHandler customLogoutHandler) {
		this.customLogoutHandler = customLogoutHandler;
	}

	@Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .cors(Customizer.withDefaults())

            .csrf(csrf -> csrf.disable())

            .authorizeHttpRequests(auth -> auth
                    .requestMatchers(HttpMethod.OPTIONS, "/**")
                    .permitAll()

                    .requestMatchers("/api/auth/**")
                    .permitAll()

                    .anyRequest()
                    .authenticated()
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