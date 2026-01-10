package com.app.bank.web.dto;
import java.math.BigDecimal;
import java.time.LocalDate;

public class LoanResponse {

	private Long id;
    private String loanAccountNumber;
    private String loanType;
    private BigDecimal principalAmount;
    private BigDecimal interestRate;
    private Integer tenureMonths;
    private BigDecimal emiAmount;
    private BigDecimal totalAmount;
    private String status;
    private LocalDate disbursementDate;
    private LocalDate maturityDate;
    private String appliedDate;
    private String customerName;
    private String customerEmail;
    
    public LoanResponse() {
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLoanAccountNumber() {
		return loanAccountNumber;
	}

	public void setLoanAccountNumber(String loanAccountNumber) {
		this.loanAccountNumber = loanAccountNumber;
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

	public BigDecimal getEmiAmount() {
		return emiAmount;
	}

	public void setEmiAmount(BigDecimal emiAmount) {
		this.emiAmount = emiAmount;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LocalDate getDisbursementDate() {
        return disbursementDate;
    }

    public void setDisbursementDate(LocalDate disbursementDate) {
        this.disbursementDate = disbursementDate;
    }

    public LocalDate getMaturityDate() {
        return maturityDate;
    }

    public void setMaturityDate(LocalDate maturityDate) {
        this.maturityDate = maturityDate;
    }

	public String getAppliedDate() {
		return appliedDate;
	}

	public void setAppliedDate(String appliedDate) {
		this.appliedDate = appliedDate;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerEmail() {
		return customerEmail;
	}

	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}

	public LoanResponse(Long id, String loanAccountNumber, String loanType, BigDecimal principalAmount,
			BigDecimal interestRate, Integer tenureMonths, BigDecimal emiAmount, BigDecimal totalAmount, String status,
			LocalDate disbursementDate, LocalDate maturityDate, String appliedDate, String customerName,
			String customerEmail) {
		super();
		this.id = id;
		this.loanAccountNumber = loanAccountNumber;
		this.loanType = loanType;
		this.principalAmount = principalAmount;
		this.interestRate = interestRate;
		this.tenureMonths = tenureMonths;
		this.emiAmount = emiAmount;
		this.totalAmount = totalAmount;
		this.status = status;
		this.disbursementDate = disbursementDate;
		this.maturityDate = maturityDate;
		this.appliedDate = appliedDate;
		this.customerName = customerName;
		this.customerEmail = customerEmail;
	}
}
