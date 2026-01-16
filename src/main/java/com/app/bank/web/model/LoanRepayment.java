package com.app.bank.web.model;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.app.bank.web.enums.RepaymentStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "loan_repayments", 
       indexes = {
           @Index(name = "idx_loan_repayment_date", columnList = "paymentDate"),
           @Index(name = "idx_loan_repayment_status", columnList = "status"),
           @Index(name = "idx_loan_installment_no", columnList = "installmentNumber")
       })
public class LoanRepayment {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_id", nullable = false)
    @JsonIgnore
    private Loan loan;
    
    @Column(nullable = false)
    private Integer installmentNumber; // EMI number (1, 2, 3, ...)
    
    @Column(nullable = false)
    private LocalDate dueDate; // When payment is due
    
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal dueAmount; // EMI amount due
    
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal principalAmount; // Principal portion of EMI
    
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal interestAmount; // Interest portion of EMI
    
    @Column(precision = 15, scale = 2)
    private BigDecimal latePaymentFee = BigDecimal.ZERO; // Late payment charges
    
    @Column(precision = 15, scale = 2)
    private BigDecimal paidAmount = BigDecimal.ZERO; // Amount actually paid
    
    @Column(nullable = true, precision = 15, scale = 2)
    private LocalDate paymentDate; // Actual payment date (if paid)
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RepaymentStatus status = RepaymentStatus.PENDING;
    
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal remainingPrincipal; // Principal outstanding after this payment
    
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal totalOutstanding; // Total outstanding after this payment
    
    @Column(length = 50)
    private String paymentReference; // Transaction reference number
    
    @Column(length = 20)
    private String paymentMode; // CASH, ONLINE, CHEQUE, AUTO_DEBIT
    
    @Column(length = 500)
    private String remarks; // Any remarks/notes
    
    @Column
    private LocalDate gracePeriodEndDate; // End of grace period for late payment
    
    @Column(precision = 5, scale = 2)
    private BigDecimal penaltyRate; // Penalty interest rate for late payment
    
    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    
    private LocalDateTime updatedAt;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id")
    private Transaction transaction; // Linked transaction record
    
    @Column(length = 100)
    private String createdBy; // System or user who created the record
    
    @Column(length = 100)
    private String updatedBy; // System or user who updated the record
    
    // Pre-persist and pre-update callbacks
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    // Business Logic Methods
    
    /**
     * Check if payment is overdue
     */
    public boolean isOverdue() {
        if (status == RepaymentStatus.PAID || status == RepaymentStatus.ADVANCED_PAID) {
            return false;
        }
        LocalDate currentDate = LocalDate.now();
        return currentDate.isAfter(dueDate);
    }
    
    /**
     * Check if payment is in grace period
     */
    public boolean isInGracePeriod() {
        if (gracePeriodEndDate == null || isPaid()) {
            return false;
        }
        LocalDate currentDate = LocalDate.now();
        return currentDate.isAfter(dueDate) && 
               currentDate.isBefore(gracePeriodEndDate.plusDays(1));
    }
    
    /**
     * Calculate late payment charges
     */
    public BigDecimal calculateLatePaymentFee() {
        if (!isOverdue() || isPaid()) {
            return BigDecimal.ZERO;
        }
        
        LocalDate currentDate = LocalDate.now();
        long daysOverdue = Math.max(0, currentDate.toEpochDay() - dueDate.toEpochDay());
        
        if (penaltyRate != null) {
            // Calculate penalty based on overdue amount and days
            BigDecimal dailyPenaltyRate = penaltyRate.divide(BigDecimal.valueOf(36500), 10, BigDecimal.ROUND_HALF_UP);
            BigDecimal penalty = dueAmount.multiply(dailyPenaltyRate)
                                          .multiply(BigDecimal.valueOf(daysOverdue));
            
            // Add flat fee if applicable
            if (daysOverdue > 0) {
                penalty = penalty.add(BigDecimal.valueOf(100)); // ₹100 flat fee
            }
            
            return penalty.max(BigDecimal.ZERO);
        }
        
        return BigDecimal.ZERO;
    }
    
    /**
     * Calculate total amount due including penalties
     */
    public BigDecimal calculateTotalDue() {
        BigDecimal total = dueAmount;
        
        if (isOverdue() && !isPaid()) {
            total = total.add(calculateLatePaymentFee());
        }
        
        return total;
    }
    
    /**
     * Check if repayment is fully paid
     */
    public boolean isPaid() {
        return status == RepaymentStatus.PAID || 
               status == RepaymentStatus.ADVANCED_PAID;
    }
    
    /**
     * Check if payment is partial
     */
    public boolean isPartialPayment() {
        return status == RepaymentStatus.PARTIALLY_PAID;
    }
    
    /**
     * Get overdue days
     */
    public long getOverdueDays() {
        if (!isOverdue() || isPaid()) {
            return 0;
        }
        LocalDate currentDate = LocalDate.now();
        return Math.max(0, currentDate.toEpochDay() - dueDate.toEpochDay());
    }
    
    /**
     * Process a payment
     */
    public void processPayment(BigDecimal amount, String paymentMode, 
                               String reference, LocalDate paymentDate) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Payment amount must be positive");
        }
        
        this.paidAmount = this.paidAmount.add(amount);
        this.paymentDate = paymentDate;
        this.paymentMode = paymentMode;
        this.paymentReference = reference;
        
        BigDecimal totalDue = calculateTotalDue();
        
        if (amount.compareTo(totalDue) >= 0) {
            // Full payment
            this.status = RepaymentStatus.PAID;
            // Calculate any excess payment
            if (amount.compareTo(totalDue) > 0) {
                this.remarks = "Excess payment of " + amount.subtract(totalDue);
            }
        } else if (amount.compareTo(BigDecimal.ZERO) > 0) {
            // Partial payment
            this.status = RepaymentStatus.PARTIALLY_PAID;
            this.remarks = "Partial payment received";
        }
        
        // Update outstanding amounts
        updateOutstandingAmounts();
    }
    
    /**
     * Update outstanding amounts after payment
     */
    private void updateOutstandingAmounts() {
        if (loan != null) {
            // This should be called from service layer with proper calculations
            // For now, we'll update based on payment
            BigDecimal paidPrincipal = calculatePaidPrincipal();
            this.remainingPrincipal = this.remainingPrincipal.subtract(paidPrincipal);
            this.totalOutstanding = this.totalOutstanding.subtract(paidAmount);
        }
    }
    
    /**
     * Calculate principal portion of paid amount
     */
    private BigDecimal calculatePaidPrincipal() {
        if (paidAmount.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal totalDue = principalAmount.add(interestAmount);
        if (totalDue.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        
        // Proportional allocation based on EMI structure
        BigDecimal principalRatio = principalAmount.divide(totalDue, 10, BigDecimal.ROUND_HALF_UP);
        return paidAmount.multiply(principalRatio);
    }
    
    /**
     * Calculate interest portion of paid amount
     */
    public BigDecimal calculatePaidInterest() {
        if (paidAmount.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal totalDue = principalAmount.add(interestAmount);
        if (totalDue.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal interestRatio = interestAmount.divide(totalDue, 10, BigDecimal.ROUND_HALF_UP);
        return paidAmount.multiply(interestRatio);
    }
    
    /**
     * Get amount still due (excluding penalties)
     */
    public BigDecimal getOutstandingAmount() {
        if (isPaid()) {
            return BigDecimal.ZERO;
        }
        return dueAmount.subtract(paidAmount).max(BigDecimal.ZERO);
    }
    
    /**
     * Mark as defaulted
     */
    public void markAsDefaulted() {
        if (!isPaid() && isOverdue() && getOverdueDays() > 90) { // 90+ days overdue
            this.status = RepaymentStatus.DEFAULTED;
        }
    }
    
    /**
     * Get next due date (for next installment)
     */
    public LocalDate getNextDueDate() {
        return dueDate.plusMonths(1);
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Loan getLoan() {
		return loan;
	}

	public void setLoan(Loan loan) {
		this.loan = loan;
	}

	public Integer getInstallmentNumber() {
		return installmentNumber;
	}

	public void setInstallmentNumber(Integer installmentNumber) {
		this.installmentNumber = installmentNumber;
	}

	public LocalDate getDueDate() {
		return dueDate;
	}

	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}

	public BigDecimal getDueAmount() {
		return dueAmount;
	}

	public void setDueAmount(BigDecimal dueAmount) {
		this.dueAmount = dueAmount;
	}

	public BigDecimal getPrincipalAmount() {
		return principalAmount;
	}

	public void setPrincipalAmount(BigDecimal principalAmount) {
		this.principalAmount = principalAmount;
	}

	public BigDecimal getInterestAmount() {
		return interestAmount;
	}

	public void setInterestAmount(BigDecimal interestAmount) {
		this.interestAmount = interestAmount;
	}

	public BigDecimal getLatePaymentFee() {
		return latePaymentFee;
	}

	public void setLatePaymentFee(BigDecimal latePaymentFee) {
		this.latePaymentFee = latePaymentFee;
	}

	public BigDecimal getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(BigDecimal paidAmount) {
		this.paidAmount = paidAmount;
	}

	public LocalDate getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(LocalDate paymentDate) {
		this.paymentDate = paymentDate;
	}

	public RepaymentStatus getStatus() {
		return status;
	}

	public void setStatus(RepaymentStatus status) {
		this.status = status;
	}

	public BigDecimal getRemainingPrincipal() {
		return remainingPrincipal;
	}

	public void setRemainingPrincipal(BigDecimal remainingPrincipal) {
		this.remainingPrincipal = remainingPrincipal;
	}

	public BigDecimal getTotalOutstanding() {
		return totalOutstanding;
	}

	public void setTotalOutstanding(BigDecimal totalOutstanding) {
		this.totalOutstanding = totalOutstanding;
	}

	public String getPaymentReference() {
		return paymentReference;
	}

	public void setPaymentReference(String paymentReference) {
		this.paymentReference = paymentReference;
	}

	public String getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public LocalDate getGracePeriodEndDate() {
		return gracePeriodEndDate;
	}

	public void setGracePeriodEndDate(LocalDate gracePeriodEndDate) {
		this.gracePeriodEndDate = gracePeriodEndDate;
	}

	public BigDecimal getPenaltyRate() {
		return penaltyRate;
	}

	public void setPenaltyRate(BigDecimal penaltyRate) {
		this.penaltyRate = penaltyRate;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Transaction getTransaction() {
		return transaction;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public LoanRepayment(Long id, Loan loan, Integer installmentNumber, LocalDate dueDate, BigDecimal dueAmount,
			BigDecimal principalAmount, BigDecimal interestAmount, BigDecimal latePaymentFee, BigDecimal paidAmount,
			LocalDate paymentDate, RepaymentStatus status, BigDecimal remainingPrincipal, BigDecimal totalOutstanding,
			String paymentReference, String paymentMode, String remarks, LocalDate gracePeriodEndDate,
			BigDecimal penaltyRate, LocalDateTime createdAt, LocalDateTime updatedAt, Transaction transaction,
			String createdBy, String updatedBy) {
		super();
		this.id = id;
		this.loan = loan;
		this.installmentNumber = installmentNumber;
		this.dueDate = dueDate;
		this.dueAmount = dueAmount;
		this.principalAmount = principalAmount;
		this.interestAmount = interestAmount;
		this.latePaymentFee = latePaymentFee;
		this.paidAmount = paidAmount;
		this.paymentDate = paymentDate;
		this.status = status;
		this.remainingPrincipal = remainingPrincipal;
		this.totalOutstanding = totalOutstanding;
		this.paymentReference = paymentReference;
		this.paymentMode = paymentMode;
		this.remarks = remarks;
		this.gracePeriodEndDate = gracePeriodEndDate;
		this.penaltyRate = penaltyRate;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.transaction = transaction;
		this.createdBy = createdBy;
		this.updatedBy = updatedBy;
	}
    
    public LoanRepayment() {
		// TODO Auto-generated constructor stub
	}
}
