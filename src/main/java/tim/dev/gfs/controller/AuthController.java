package tim.dev.gfs.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tim.dev.gfs.dto.LoginRequest;
import tim.dev.gfs.dto.LoginResponse;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@PostMapping("/login")
	public LoginResponse login(@RequestBody LoginRequest request) {
		
        if ("admin".equals(request.getUsername())
                && "admin".equals(request.getPassword())) {

            return new LoginResponse(
                    true,
                    "Login Successful",
                    "dummy-token");
        }

        return new LoginResponse(
                false,
                "Invalid username or password",
                null);
	}
}
