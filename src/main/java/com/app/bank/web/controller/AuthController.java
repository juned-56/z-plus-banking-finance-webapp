package com.app.bank.web.controller;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.app.bank.web.dto.LoginRequest;
import com.app.bank.web.dto.LoginResponse;
import com.app.bank.web.dto.RegisterRequest;
import com.app.bank.web.security.JwtTokenProvider;
import com.app.bank.web.service.AuthService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

	private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
	
    public AuthController(AuthService authService, AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider) {
		this.authService = authService;
		this.authenticationManager = authenticationManager;
		this.tokenProvider = tokenProvider;
	}
    
    @PostMapping("/login") // http://localhost:8989/api/v1/auth/login
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
//		LoginResponse loginResponse = authService.login(loginRequest);
//		return ResponseEntity.ok(loginResponse);
    	Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(),
                    loginRequest.getPassword()
                )
            );

            String token = tokenProvider.generateToken(authentication);

            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("tokenType", "Bearer");

            return ResponseEntity.ok(response);
        }
    
    @PostMapping("/register")   // http://localhost:8989/api/v1/auth/register
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
    	Map<String, String> response = authService.register(registerRequest);
    			return ResponseEntity.ok(response);
    }
    
    @PostMapping("/refresh-token") // http://localhost:8989/api/v1/auth/refresh-token?token=xxxx
    public ResponseEntity<?> refreshToken(@RequestParam String token) {
		String newToken = tokenProvider.refreshToken(token);
		if(newToken != null) {
			LoginResponse loginResponse = new LoginResponse();
			loginResponse.setToken(newToken);
			loginResponse.setType("Bearer");
			return ResponseEntity.ok(loginResponse);
		}
		return ResponseEntity.badRequest().body("Invalid Refresh Token");
	}
    
    @PostMapping("/logout") // http://localhost:8989/api/v1/auth/logout
    public ResponseEntity<?> logoutUser() {
	return ResponseEntity.ok("Logout Successful");
	}
    
    @GetMapping("/validate-token") // http://localhost:8989/api/v1/auth/validate-token?token=xxxx
    public ResponseEntity<?> validateToken(@RequestParam String token) {
    	boolean isValid = tokenProvider.validateToken(token);
    	if(isValid) {
    		Map<String, Object> tokenInfo = tokenProvider.getTokenInfo(token);
			return ResponseEntity.ok("Token is valid");
		} else {
			return ResponseEntity.badRequest().body("Invalid Token");
		}
    }
}