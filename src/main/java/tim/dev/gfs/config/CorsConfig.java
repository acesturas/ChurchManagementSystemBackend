package tim.dev.gfs.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


@Configuration
public class CorsConfig {


    /*
     * Creates a global CORS configuration.
     *
     * This allows frontend applications from other origins
     * to access your Spring Boot REST APIs.
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {


        CorsConfiguration config = new CorsConfiguration();



        /*
         * Defines which frontend URLs are allowed.
         *
         * Angular development server:
         * http://localhost:4200
         *
         * Without this, the browser will block requests
         * coming from Angular.
         */
        config.setAllowedOrigins(List.of(
                "http://localhost:4200",
        	    "https://church-management-system-ruby.vercel.app"
        ));




        /*
         * Defines which HTTP methods are allowed.
         *
         * GET    -> Retrieve data
         * POST   -> Create data
         * PUT    -> Update data
         * DELETE -> Delete data
         * OPTIONS -> Browser preflight request
         */
        config.setAllowedMethods(List.of(
                "GET",
                "POST",
                "PUT",
                "DELETE",
                "OPTIONS"
        ));





        /*
         * Allows all request headers.
         *
         * Examples:
         * Content-Type
         * Authorization
         * X-Requested-With
         *
         * Useful when sending JSON requests
         * from Angular.
         */
        config.setAllowedHeaders(List.of("*"));





        /*
         * Allows cookies and authentication information
         * to be sent with requests.
         *
         * Example:
         * - Login session cookies
         * - JWT cookies
         *
         * Important if your application uses authentication.
         */
        config.setAllowCredentials(true);






        /*
         * Applies this CORS configuration to all endpoints.
         *
         * Example:
         *
         * /api/events
         * /api/auth/login
         * /api/users
         *
         * all use this configuration.
         */
        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", config);



        return source;
    }
}