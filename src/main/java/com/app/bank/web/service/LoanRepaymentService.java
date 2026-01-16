package com.app.bank.web.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.app.bank.web.enums.AccountStatus;
import com.app.bank.web.enums.LoanStatus;
import com.app.bank.web.enums.RepaymentStatus;
import com.app.bank.web.enums.TransactionStatus;
import com.app.bank.web.enums.TransactionType;
import com.app.bank.web.enums.UserRole;
import com.app.bank.web.exception.BankException;
import com.app.bank.web.model.Account;
import com.app.bank.web.model.Loan;
import com.app.bank.web.model.LoanRepayment;
import com.app.bank.web.model.Transaction;
import com.app.bank.web.model.User;
import com.app.bank.web.repository.AccountRepository;
import com.app.bank.web.repository.LoanRepaymentRepository;
import com.app.bank.web.repository.LoanRepository;
import com.app.bank.web.repository.TransactionRepository;
import com.app.bank.web.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class LoanRepaymentService {

	private final LoanRepaymentRepository loanRepaymentRepository;
	private final LoanRepository loanRepository;
	private final AccountRepository accountRepository;
	private final UserRepository userRepository;
	private final TransactionRepository transactionRepository;
	
	public LoanRepaymentService(LoanRepaymentRepository loanRepaymentRepository, LoanRepository loanRepository,
			AccountRepository accountRepository, UserRepository userRepository, TransactionRepository transactionRepository) {
			this.loanRepaymentRepository = loanRepaymentRepository;
			this.loanRepository = loanRepository;
			this.accountRepository = accountRepository;
			this.userRepository = userRepository;
			this.transactionRepository = transactionRepository;
	}	
	
	public List<LoanRepayment> getRepaySchedule(Long loanId){
		return loanRepaymentRepository.findByLoanId(loanId);
	}
	
	public LoanRepayment getNextDueRepayment(Long loanId) {
		return loanRepaymentRepository.findNextDueRepayment(loanId).orElseThrow(() -> 
		new BankException("No pending repayment found"));
	}

	
	public List<LoanRepayment> getOverduePayments(Long loanId){
		return loanRepaymentRepository.findOverdueRepayments(loanId);
	}
	
	
	@Transactional
	public void generateRepaymentSchedule(Loan loan, String createdBy) {

	    BigDecimal principal = loan.getPrincipalAmount();
	    BigDecimal emi = loan.getEmiAmount();
	    BigDecimal rate = loan.getInterestRate()
	            .divide(BigDecimal.valueOf(1200), 10, RoundingMode.HALF_UP);

	    int months = loan.getTenureMonths();
	    LocalDate startDate = loan.getDisbursementDate();

	    BigDecimal remainingPrincipal = principal;

	    for (int i = 1; i <= months; i++) {

	        BigDecimal interest = remainingPrincipal.multiply(rate);
	        BigDecimal principalPart = emi.subtract(interest);

	        remainingPrincipal = remainingPrincipal.subtract(principalPart);

	        LoanRepayment repayment = new LoanRepayment();
	        repayment.setLoan(loan);
	        repayment.setInstallmentNumber(i);
	        repayment.setDueDate(startDate.plusMonths(i));
	        repayment.setDueAmount(emi);
	        repayment.setPrincipalAmount(principalPart);
	        repayment.setInterestAmount(interest);
	        repayment.setRemainingPrincipal(remainingPrincipal.max(BigDecimal.ZERO));
	        repayment.setTotalOutstanding(remainingPrincipal.max(BigDecimal.ZERO));
	        repayment.setStatus(RepaymentStatus.PENDING);
	        repayment.setPaymentDate(null);
	        repayment.setCreatedBy(createdBy);
	        loanRepaymentRepository.save(repayment);
	    }
	}

	
	@Transactional
	public Map<String, Object> payEmi(Long loanId, BigDecimal amount, String username) {

	    Loan loan = loanRepository.findById(loanId)
	            .orElseThrow(() -> new BankException("Loan not found"));

	    User user = userRepository.findByUsername(username)
	            .orElseThrow(() -> new BankException("User not found"));

	    if (!loan.getUser().getId().equals(user.getId())) {
	        throw new BankException("This loan does not belong to you");
	    }

	    // Get next pending EMI
	    LoanRepayment repayment = loanRepaymentRepository
	            .findNextDueRepayment(loanId)
	            .orElseThrow(() -> new BankException("No pending EMI found"));

	    if (amount.compareTo(repayment.getDueAmount()) < 0) {
	        throw new BankException("Partial EMI payment not allowed");
	    }

	    Account account = loan.getLinkedAccount();

	    if (account.getBalance().compareTo(amount) < 0) {
	        throw new BankException("Insufficient balance");
	    }

	    // Deduct amount
	    account.setBalance(account.getBalance().subtract(amount));
	    account.setLastTransactionAt(LocalDateTime.now());
	    accountRepository.save(account);
	    
	    //Transaction record
	    Transaction transaction = new Transaction();
	    transaction.setFromAccount(account);
	    transaction.setAmount(amount);
	    transaction.setTransactionType(TransactionType.LOAN_PREPAYMENT);
	    transaction.setDescription("EMI payment for Loan ID " + loanId);
	    transaction.setTransactionDate(LocalDateTime.now());
	    transaction.setStatus(TransactionStatus.SUCCESS);
	    transaction.setReferenceNumber(getRefrenceNumber());
	    transaction.setTransactionId(getTransactionId());
	    transaction.setBalanceAfterTransaction(account.getBalance().subtract(amount));
	    transaction.setRemarks("Payment Done");
	    transactionRepository.save(transaction);

	    repayment.setPaidAmount(amount);
	    repayment.setStatus(RepaymentStatus.PAID);
	    repayment.setPaymentDate(LocalDate.now());
	    repayment.setPaymentMode("ONLINE");
	    repayment.setPaymentReference("TXN" + System.currentTimeMillis());
	    repayment.setRemarks("EMI paid successfully");
	    repayment.setUpdatedAt(LocalDateTime.now());
	    repayment.setUpdatedBy(username);
	    repayment.setTransaction(transaction);
	    //repayment.setTransaction("TXN" + System.currentTimeMillis());

	    loanRepaymentRepository.save(repayment);

	    // Close loan if last EMI
	    boolean allPaid = loanRepaymentRepository
	            .findByLoanIdAndStatus(loanId, RepaymentStatus.PENDING)
	            .isEmpty();

	    if (allPaid) {
	        loan.setStatus(LoanStatus.CLOSED);
	        loanRepository.save(loan);
	    }

	    Map<String, Object> response = new HashMap<>();
	    response.put("message", "EMI Payment successful");
	    response.put("installment", repayment.getInstallmentNumber());
	    response.put("paid amount", repayment.getPaidAmount());
	    response.put("remaining due", repayment.getDueAmount().subtract(amount));
	    response.put("status", repayment.getStatus());

	    return response;
	}

	
	public BigDecimal getTotalPaid(Long loanId) {
		return loanRepaymentRepository.getTotalPaidAmount(loanId);
	}
	
	public BigDecimal getOverDueAmount(Long loanId) {
		return loanRepaymentRepository.getOverdueAmount(loanId, LocalDate.now());
	}
	
	
	public String getRefrenceNumber() {
		return "REF" + System.currentTimeMillis() + (int)(Math.random() * 1000);
	}
	
	public String getTransactionId() {
		return "TXN" + System.currentTimeMillis() + (int)(Math.random() * 1000);
	}
}
