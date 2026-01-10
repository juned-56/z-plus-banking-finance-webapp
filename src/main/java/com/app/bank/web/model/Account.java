package com.app.bank.web.model;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import com.app.bank.web.enums.AccountStatus;
import com.app.bank.web.enums.AccountType;
import com.app.bank.web.enums.UserStatus;
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
@Table(name = "accounts")
public class Account {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false, unique = true, length = 20)
	private String accountNumber;
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private AccountType accountType = AccountType.SAVINGS;
	@Column(nullable = false, precision = 15, scale = 2)
	private BigDecimal balance = BigDecimal.ZERO;
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private AccountStatus status = AccountStatus.ACTIVE; 
	@Column(nullable = false)
	private LocalDateTime createdAt = LocalDateTime.now();
	private LocalDateTime lastTransactionAt;
	@Column(nullable = false, precision = 15, scale = 2)
	private BigDecimal minimumBalance = BigDecimal.valueOf(1000);
	private BigDecimal interestRate = BigDecimal.valueOf(3.5);
	@OneToMany(mappedBy = "fromAccount", cascade = CascadeType.ALL)
	private List<Transaction> debitTransctions;
	@OneToMany(mappedBy = "toAccount", cascade = CascadeType.ALL)
	private List<Transaction> creditTransactions;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public AccountType getAccountType() {
		return accountType;
	}
	public void setAccountType(AccountType accountType) {
		this.accountType = accountType;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	public AccountStatus getStatus() {
		return status;
	}
	public void setStatus(AccountStatus status) {
		this.status = status;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	public LocalDateTime getLastTransactionAt() {
		return lastTransactionAt;
	}
	public void setLastTransactionAt(LocalDateTime lastTransactionAt) {
		this.lastTransactionAt = lastTransactionAt;
	}
	public BigDecimal getMinimumBalance() {
		return minimumBalance;
	}
	public void setMinimumBalance(BigDecimal minimumBalance) {
		this.minimumBalance = minimumBalance;
	}
	public BigDecimal getInterestRate() {
		return interestRate;
	}
	public void setInterestRate(BigDecimal interestRate) {
		this.interestRate = interestRate;
	}
	public List<Transaction> getDebitTransctions() {
		return debitTransctions;
	}
	public void setDebitTransctions(List<Transaction> debitTransctions) {
		this.debitTransctions = debitTransctions;
	}
	public List<Transaction> getCreditTransactions() {
		return creditTransactions;
	}
	public void setCreditTransactions(List<Transaction> creditTransactions) {
		this.creditTransactions = creditTransactions;
	}
	public Account(Long id, String accountNumber, User user, AccountType accountType, BigDecimal balance,
			AccountStatus status, LocalDateTime createdAt, LocalDateTime lastTransactionAt, BigDecimal minimumBalance,
			BigDecimal interestRate, List<Transaction> debitTransctions, List<Transaction> creditTransactions) {
		super();
		this.id = id;
		this.accountNumber = accountNumber;
		this.user = user;
		this.accountType = accountType;
		this.balance = balance;
		this.status = status;
		this.createdAt = createdAt;
		this.lastTransactionAt = lastTransactionAt;
		this.minimumBalance = minimumBalance;
		this.interestRate = interestRate;
		this.debitTransctions = debitTransctions;
		this.creditTransactions = creditTransactions;
	}
	
	public Account() {
		// TODO Auto-generated constructor stub
	}
}
