package com.app.bank.web.model;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.app.bank.web.enums.TransactionStatus;
import com.app.bank.web.enums.TransactionType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "transactions")
public class Transaction {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    
    @Column(unique = true, nullable = false, length = 30)
    private String transactionId;    
    @ManyToOne
    @JoinColumn(name = "from_account_id")
    private Account fromAccount;    
    @ManyToOne
    @JoinColumn(name = "to_account_id")
    private Account toAccount;    
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType transactionType;    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus status = TransactionStatus.PENDING;    
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private LocalDateTime transactionDate = LocalDateTime.now();    
    private String referenceNumber;    
    private String remarks;    
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal balanceAfterTransaction;

    public Transaction() {
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public Account getFromAccount() {
		return fromAccount;
	}

	public void setFromAccount(Account fromAccount) {
		this.fromAccount = fromAccount;
	}

	public Account getToAccount() {
		return toAccount;
	}

	public void setToAccount(Account toAccount) {
		this.toAccount = toAccount;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public TransactionType getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(TransactionType transactionType) {
		this.transactionType = transactionType;
	}

	public TransactionStatus getStatus() {
		return status;
	}

	public void setStatus(TransactionStatus status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDateTime getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(LocalDateTime transactionDate) {
		this.transactionDate = transactionDate;
	}

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public BigDecimal getBalanceAfterTransaction() {
		return balanceAfterTransaction;
	}

	public void setBalanceAfterTransaction(BigDecimal balanceAfterTransaction) {
		this.balanceAfterTransaction = balanceAfterTransaction;
	}

	public Transaction(Long id, String transactionId, Account fromAccount, Account toAccount, BigDecimal amount,
			TransactionType transactionType, TransactionStatus status, String description,
			LocalDateTime transactionDate, String referenceNumber, String remarks, BigDecimal balanceAfterTransaction) {
		super();
		this.id = id;
		this.transactionId = transactionId;
		this.fromAccount = fromAccount;
		this.toAccount = toAccount;
		this.amount = amount;
		this.transactionType = transactionType;
		this.status = status;
		this.description = description;
		this.transactionDate = transactionDate;
		this.referenceNumber = referenceNumber;
		this.remarks = remarks;
		this.balanceAfterTransaction = balanceAfterTransaction;
	}
}
