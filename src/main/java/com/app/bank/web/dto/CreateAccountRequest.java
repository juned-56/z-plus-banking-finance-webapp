package com.app.bank.web.dto;
import java.math.BigDecimal;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateAccountRequest {

	@NotBlank(message = "Account type is required")
    private String accountType;
    @NotNull(message = "Initial deposit is required")
    @DecimalMin(value = "1000.00", message = "Minimum deposit is 1000")
    private BigDecimal initialDeposit;    
    @NotBlank(message = "User ID is required")
    private String userId;
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public BigDecimal getInitialDeposit() {
		return initialDeposit;
	}
	public void setInitialDeposit(BigDecimal initialDeposit) {
		this.initialDeposit = initialDeposit;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
}
