package com.app.bank.web.config;
import java.io.IOException;
import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import com.app.bank.web.exception.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint{

	
	 private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class); 
	private final ObjectMapper objectMapper;
	public JwtAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
	
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		logger.error("Unothorized error: {}", authException.getMessage());
		logger.error("Request URI: {}", request.getRequestURI());
		response.setContentType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE);
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		ErrorResponse errorResponse = new ErrorResponse(
				LocalDateTime.now(),
				HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized",
				authException.getMessage(), "AUTH_001",
				null,
				request.getRequestURI(),
				null);
		response.addHeader("WWW-Authenticate", "Bearer realm=\"Banking API\"");
		response.addHeader("X-Auth-Error", authException.getClass().getSimpleName());
		objectMapper.writeValue(response.getOutputStream(), errorResponse);
	}
}
