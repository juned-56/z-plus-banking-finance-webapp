package com.app.bank.web.dto;
import java.math.BigDecimal;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class LoanRequest {

	@NotBlank(message = "Account number is required")
    private String accountNumber;
    
    @NotBlank(message = "Loan type is required")
    private String loanType;
    
    @NotNull(message = "Principal amount is required")
    @DecimalMin(value = "10000.00", message = "Minimum loan amount is 10000")
    @DecimalMax(value = "50000000.00", message = "Maximum loan amount is 50000000")
    private BigDecimal principalAmount;
    
    @NotNull(message = "Interest rate is required")
    @DecimalMin(value = "5.00", message = "Minimum interest rate is 5%")
    @DecimalMax(value = "20.00", message = "Maximum interest rate is 20%")
    private BigDecimal interestRate;
    
    @NotNull(message = "Tenure is required")
    @Min(value = 12, message = "Minimum tenure is 12 months")
    @Max(value = 360, message = "Maximum tenure is 360 months")
    private Integer tenureMonths;
    
    public LoanRequest() {
		// TODO Auto-generated constructor stub
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getLoanType() {
		return loanType;
	}

	public void setLoanType(String loanType) {
		this.loanType = loanType;
	}

	public BigDecimal getPrincipalAmount() {
		return principalAmount;
	}

	public void setPrincipalAmount(BigDecimal principalAmount) {
		this.principalAmount = principalAmount;
	}

	public BigDecimal getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(BigDecimal interestRate) {
		this.interestRate = interestRate;
	}

	public Integer getTenureMonths() {
		return tenureMonths;
	}

	public void setTenureMonths(Integer tenureMonths) {
		this.tenureMonths = tenureMonths;
	}

	public LoanRequest(@NotBlank(message = "Account number is required") String accountNumber,
			@NotBlank(message = "Loan type is required") String loanType,
			@NotNull(message = "Principal amount is required") @DecimalMin(value = "10000.00", message = "Minimum loan amount is 10000") @DecimalMax(value = "50000000.00", message = "Maximum loan amount is 50000000") BigDecimal principalAmount,
			@NotNull(message = "Interest rate is required") @DecimalMin(value = "5.00", message = "Minimum interest rate is 5%") @DecimalMax(value = "20.00", message = "Maximum interest rate is 20%") BigDecimal interestRate,
			@NotNull(message = "Tenure is required") @Min(value = 12, message = "Minimum tenure is 12 months") @Max(value = 360, message = "Maximum tenure is 360 months") Integer tenureMonths) {
		super();
		this.accountNumber = accountNumber;
		this.loanType = loanType;
		this.principalAmount = principalAmount;
		this.interestRate = interestRate;
		this.tenureMonths = tenureMonths;
	}
}
