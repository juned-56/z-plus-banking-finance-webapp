package com.app.bank.web.dto;
import java.math.BigDecimal;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class TransactionRequest {

	@NotBlank(message = "From account is required")
    private String fromAccountNumber;
    @NotBlank(message = "To account is required")
    private String toAccountNumber;    
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "1.00", message = "Minimum amount is 1")
    @DecimalMax(value = "50000.00", message = "Maximum amount per transaction is 50000")
    private BigDecimal amount;    
    @NotBlank(message = "Description is required")
    private String description;    
    private String otp;
	public String getFromAccountNumber() {
		return fromAccountNumber;
	}
	public void setFromAccountNumber(String fromAccountNumber) {
		this.fromAccountNumber = fromAccountNumber;
	}
	public String getToAccountNumber() {
		return toAccountNumber;
	}
	public void setToAccountNumber(String toAccountNumber) {
		this.toAccountNumber = toAccountNumber;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getOtp() {
		return otp;
	}
	public void setOtp(String otp) {
		this.otp = otp;
	}
	public TransactionRequest(@NotBlank(message = "From account is required") String fromAccountNumber,
			@NotBlank(message = "To account is required") String toAccountNumber,
			@NotNull(message = "Amount is required") @DecimalMin(value = "1.00", message = "Minimum amount is 1") @DecimalMax(value = "50000.00", message = "Maximum amount per transaction is 50000") BigDecimal amount,
			@NotBlank(message = "Description is required") String description, String otp) {
		super();
		this.fromAccountNumber = fromAccountNumber;
		this.toAccountNumber = toAccountNumber;
		this.amount = amount;
		this.description = description;
		this.otp = otp;
	}
    
    
    public TransactionRequest() {
		// TODO Auto-generated constructor stub
	}
}
