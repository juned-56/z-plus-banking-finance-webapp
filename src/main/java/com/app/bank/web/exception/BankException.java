package com.app.bank.web.exception;
import java.math.BigDecimal;

import org.springframework.http.HttpStatus;

public class BankException extends RuntimeException{
	
	
	
//	public BankException(String message) {
//		super(message);
//	}
	
	private final HttpStatus httpStatus;
    private final String errorCode;
    private final String details;
	
    
	public BankException(String message) {
        super(message);
        this.httpStatus = HttpStatus.BAD_REQUEST;
        this.errorCode = "BANK_001";
        this.details = null;
    }
    
    /**
     * Constructor with message and HTTP status
     */
    public BankException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
        this.errorCode = generateErrorCode(httpStatus);
        this.details = null;
    }
    
    /**
     * Constructor with message, HTTP status, and error code
     */
    public BankException(String message, HttpStatus httpStatus, String errorCode) {
        super(message);
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.details = null;
    }
    
    /**
     * Constructor with message, HTTP status, error code, and details
     */
    public BankException(String message, HttpStatus httpStatus, String errorCode, String details) {
        super(message);
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.details = details;
    }
    
    /**
     * Constructor with cause
     */
    public BankException(String message, Throwable cause) {
        super(message, cause);
        this.httpStatus = HttpStatus.BAD_REQUEST;
        this.errorCode = "BANK_001";
        this.details = cause.getMessage();
    }
    
    /**
     * Constructor with message, cause, and HTTP status
     */
    public BankException(String message, Throwable cause, HttpStatus httpStatus) {
        super(message, cause);
        this.httpStatus = httpStatus;
        this.errorCode = generateErrorCode(httpStatus);
        this.details = cause.getMessage();
    }
    
    /**
     * Static factory methods for common exceptions
     */
    public static BankException notFound(String resource, String identifier) {
        String message = String.format("%s not found with identifier: %s", resource, identifier);
        return new BankException(message, HttpStatus.NOT_FOUND, "BANK_404");
    }
    
    public static BankException alreadyExists(String resource, String identifier) {
        String message = String.format("%s already exists: %s", resource, identifier);
        return new BankException(message, HttpStatus.CONFLICT, "BANK_409");
    }
    
    public static BankException validationFailed(String field, String reason) {
        String message = String.format("Validation failed for field '%s': %s", field, reason);
        return new BankException(message, HttpStatus.BAD_REQUEST, "BANK_400");
    }
    
    public static BankException unauthorized(String message) {
        return new BankException(message, HttpStatus.UNAUTHORIZED, "BANK_401");
    }
    
    public static BankException forbidden(String message) {
        return new BankException(message, HttpStatus.FORBIDDEN, "BANK_403");
    }
    
    public static BankException insufficientFunds(String accountNumber, BigDecimal available, BigDecimal required) {
        String message = String.format("Insufficient funds in account %s. Available: %s, Required: %s", 
            accountNumber, available, required);
        return new BankException(message, HttpStatus.BAD_REQUEST, "BANK_402");
    }
    
    public static BankException transactionFailed(String transactionId, String reason) {
        String message = String.format("Transaction %s failed: %s", transactionId, reason);
        return new BankException(message, HttpStatus.BAD_REQUEST, "BANK_450");
    }
    
    public static BankException otpInvalid(String purpose) {
        String message = String.format("Invalid or expired OTP for %s", purpose);
        return new BankException(message, HttpStatus.BAD_REQUEST, "BANK_460");
    }
    
    public static BankException rateLimitExceeded(String resource, int limit, String period) {
        String message = String.format("Rate limit exceeded for %s. Limit: %d per %s", resource, limit, period);
        return new BankException(message, HttpStatus.TOO_MANY_REQUESTS, "BANK_429");
    }
    
    public static BankException serviceUnavailable(String service) {
        String message = String.format("%s service is currently unavailable", service);
        return new BankException(message, HttpStatus.SERVICE_UNAVAILABLE, "BANK_503");
    }
    
    /**
     * Generate error code based on HTTP status
     */
    private String generateErrorCode(HttpStatus httpStatus) {
        return String.format("BANK_%d", httpStatus.value());
    }
    
    /**
     * Get formatted error message
     */
    public String getFormattedMessage() {
        return String.format("[%s] %s", errorCode, getMessage());
    }
    
    /**
     * Check if exception is client error (4xx)
     */
    public boolean isClientError() {
        return httpStatus.is4xxClientError();
    }
    
    /**
     * Check if exception is server error (5xx)
     */
    public boolean isServerError() {
        return httpStatus.is5xxServerError();
    }
    
    /**
     * Convert to ErrorResponse DTO
     */
    public ErrorResponse toErrorResponse() {
        return new ErrorResponse(
            java.time.LocalDateTime.now(),
            httpStatus.value(),
            httpStatus.getReasonPhrase(),
            getMessage(),
            errorCode,
            details
        );
    }
    
    
	
}
