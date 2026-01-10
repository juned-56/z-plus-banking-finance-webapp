package com.app.bank.web.model;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import com.app.bank.web.enums.LoanStatus;
import com.app.bank.web.enums.LoanType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "loans")
public class Loan {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    
    @Column(unique = true, nullable = false, length = 20)
    private String loanAccountNumber;    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;    
    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account linkedAccount;    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoanType loanType;    
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal principalAmount;    
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal interestRate;    
    @Column(nullable = false)
    private Integer tenureMonths;    
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal emiAmount;    
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal totalAmount;    
    @Column(nullable = false)
    private LocalDate disbursementDate;    
    @Column(nullable = false)
    private LocalDate maturityDate;    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoanStatus status = LoanStatus.PENDING;    
    @Column(nullable = false)
    private LocalDateTime appliedDate = LocalDateTime.now();    
    private LocalDateTime approvedDate;    
    private String approvedBy;   
    @OneToMany(mappedBy = "loan", cascade = CascadeType.ALL)
    private List<LoanRepayment> repayments;
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
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Account getLinkedAccount() {
		return linkedAccount;
	}
	public void setLinkedAccount(Account linkedAccount) {
		this.linkedAccount = linkedAccount;
	}
	public LoanType getLoanType() {
		return loanType;
	}
	public void setLoanType(LoanType loanType) {
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
	public LoanStatus getStatus() {
		return status;
	}
	public void setStatus(LoanStatus status) {
		this.status = status;
	}
	public LocalDateTime getAppliedDate() {
		return appliedDate;
	}
	public void setAppliedDate(LocalDateTime appliedDate) {
		this.appliedDate = appliedDate;
	}
	public LocalDateTime getApprovedDate() {
		return approvedDate;
	}
	public void setApprovedDate(LocalDateTime approvedDate) {
		this.approvedDate = approvedDate;
	}
	public String getApprovedBy() {
		return approvedBy;
	}
	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}
	public List<LoanRepayment> getRepayments() {
		return repayments;
	}
	public void setRepayments(List<LoanRepayment> repayments) {
		this.repayments = repayments;
	}
	public Loan(Long id, String loanAccountNumber, User user, Account linkedAccount, LoanType loanType,
			BigDecimal principalAmount, BigDecimal interestRate, Integer tenureMonths, BigDecimal emiAmount,
			BigDecimal totalAmount, LocalDate disbursementDate, LocalDate maturityDate, LoanStatus status,
			LocalDateTime appliedDate, LocalDateTime approvedDate, String approvedBy, List<LoanRepayment> repayments) {
		super();
		this.id = id;
		this.loanAccountNumber = loanAccountNumber;
		this.user = user;
		this.linkedAccount = linkedAccount;
		this.loanType = loanType;
		this.principalAmount = principalAmount;
		this.interestRate = interestRate;
		this.tenureMonths = tenureMonths;
		this.emiAmount = emiAmount;
		this.totalAmount = totalAmount;
		this.disbursementDate = disbursementDate;
		this.maturityDate = maturityDate;
		this.status = status;
		this.appliedDate = appliedDate;
		this.approvedDate = approvedDate;
		this.approvedBy = approvedBy;
		this.repayments = repayments;
	}
    
    public Loan() {
		// TODO Auto-generated constructor stub
	}
}
