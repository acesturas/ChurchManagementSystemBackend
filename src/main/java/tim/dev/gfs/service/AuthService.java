package tim.dev.gfs.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import tim.dev.gfs.dao.UserDao;
import tim.dev.gfs.dto.LoginRequest;
import tim.dev.gfs.dto.LoginResponse;
import tim.dev.gfs.dto.RegisterRequest;
import tim.dev.gfs.model.User;
import tim.dev.gfs.security.CustomUserDetails;
import tim.dev.gfs.security.JwtService;

@Service
public class AuthService {

    // Used only during registration to hash the user's password.
    private final PasswordEncoder passwordEncoder;

    // Used for checking existing users and inserting new users.
    private final UserDao userDao;

    // Spring Security's authentication engine.
    // Instead of checking the password ourselves, we delegate to this.
    private final AuthenticationManager authenticationManager;

    // Responsible for generating JWT tokens after a successful login.
    private final JwtService jwtService;

    public AuthService(
            UserDao userDao,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService) {

        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public LoginResponse login(LoginRequest request) {


    	System.out.println("Inside AuthService login()");
        try {

            /*
             * Create an authentication request containing the username
             * and the plain-text password entered by the user.
             *
             * At this point NOTHING has been verified yet.
             */
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword());

            /*
             * Delegate authentication to Spring Security.
             *
             * Internally it will:
             *
             * 1. Call CustomUserDetailsService.loadUserByUsername()
             * 2. Load the user from PostgreSQL using UserDao
             * 3. Compare the entered password with the BCrypt hash
             * 4. Check whether the account is enabled
             * 5. Throw an exception if authentication fails
             */
            Authentication authentication =
                    authenticationManager.authenticate(authenticationToken);

            /*
             * Authentication succeeded.
             *
             * Spring Security returns our CustomUserDetails object.
             */
            CustomUserDetails user =
                    (CustomUserDetails) authentication.getPrincipal();

            /*
             * Generate a JWT that will be sent back to the client.
             *
             * The Angular application will store this token and send it
             * with future requests in the Authorization header.
             */
            String token = jwtService.generateToken(user);

            return new LoginResponse(
                    true,
                    "Login successful.",
                    token);

        } catch (AuthenticationException ex) {

            /*
             * Authentication failed.
             *
             * This exception is thrown for:
             * - Wrong username
             * - Wrong password
             * - Disabled account
             * - Locked account
             * - Expired account
             *
             * We intentionally return the same message to avoid
             * revealing which part of the login failed.
             */
            return new LoginResponse(
                    false,
                    "Invalid username or password.",
                    null);
        }
    }

    public LoginResponse register(RegisterRequest request) {

    	System.out.println("Inside AuthService register()");
        /*
         * Prevent duplicate usernames.
         */
        if (userDao.existsByUsername(request.getUsername())) {

            return new LoginResponse(
                    false,
                    "Username already exists.",
                    null);
        }

        User user = new User();

        user.setUsername(request.getUsername());

        /*
         * NEVER store plain-text passwords.
         *
         * BCrypt automatically:
         * - Generates a random salt
         * - Hashes the password
         * - Stores the salt inside the hash
         */
        user.setPassword(
                passwordEncoder.encode(request.getPassword()));

        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());

        // Default role for newly registered users.
        user.setRole("USER");

        // New users are enabled by default.
        user.setEnabled(true);

        userDao.insertUser(user);

        return new LoginResponse(
                true,
                "Registration successful.",
                null);
    }
}