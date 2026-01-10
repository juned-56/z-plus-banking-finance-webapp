package com.app.bank.web.exception;
import java.time.LocalDateTime;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
    
    private int status;
    private String error;
    private String message;
    private String errorCode;
    private String details;
    private String path;
    private Map<String, String> fieldErrors;
    
    public ErrorResponse(LocalDateTime timestamp, int status, String error, String message) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
    }
    
    public ErrorResponse(LocalDateTime timestamp, int status, String error, 
                        String message, String errorCode) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
        this.errorCode = errorCode;
    }
    
    public ErrorResponse(LocalDateTime timestamp, int status, String error, 
                        String message, String errorCode, String details) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
        this.errorCode = errorCode;
        this.details = details;
    }
    
    public ErrorResponse(LocalDateTime timestamp, int status, String error, 
                        String message, Map<String, String> fieldErrors) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
        this.fieldErrors = fieldErrors;
    }
    
    public static ErrorResponse of(BankException ex, String path) {
        ErrorResponse response = ex.toErrorResponse();
        response.setPath(path);
        return response;
    }
    
    
    
    public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Map<String, String> getFieldErrors() {
		return fieldErrors;
	}

	public void setFieldErrors(Map<String, String> fieldErrors) {
		this.fieldErrors = fieldErrors;
	}

	public ErrorResponse(LocalDateTime timestamp, int status, String error, String message, String errorCode,
			String details, String path, Map<String, String> fieldErrors) {
		super();
		this.timestamp = timestamp;
		this.status = status;
		this.error = error;
		this.message = message;
		this.errorCode = errorCode;
		this.details = details;
		this.path = path;
		this.fieldErrors = fieldErrors;
	}

	public static ErrorResponse of(int status, String error, String message, String path) {
        return new ErrorResponse(
            LocalDateTime.now(),
            status,
            error,
            message,
            null,
            null,
            path,
            null
        );
    }
}
