package tim.dev.gfs.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            UserDetailsService userDetailsService) {

        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(

            HttpServletRequest request,

            HttpServletResponse response,

            FilterChain filterChain)

            throws ServletException, IOException {

        final String authHeader =
                request.getHeader("Authorization");

        if (authHeader == null ||
                !authHeader.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);
            return;
        }

        String token =
                authHeader.substring(7);
        System.out.println("JWT: " + token);

        String username =
                jwtService.extractUsername(token);
        System.out.println("Username: " + username);

        if (username != null &&
                SecurityContextHolder
                        .getContext()
                        .getAuthentication() == null) {

            CustomUserDetails user =
                    (CustomUserDetails)
                            userDetailsService
                                    .loadUserByUsername(username);

            System.out.println("Loading user...");
            
            if (jwtService.isTokenValid(token, user)) {
            	System.out.println("Authenticated: " + user.getUsername());
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(

                                user,

                                null,

                                user.getAuthorities());

                auth.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request));

                SecurityContextHolder

                        .getContext()

                        .setAuthentication(auth);
            } else {

                System.out.println("Token is INVALID");

            }
            
            System.out.println("Authentication in SecurityContext: "
                    + SecurityContextHolder.getContext().getAuthentication());
        }

        filterChain.doFilter(request, response);
    }
}