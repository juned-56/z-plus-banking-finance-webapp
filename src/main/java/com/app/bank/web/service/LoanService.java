package com.app.bank.web.service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.app.bank.web.dto.LoanRequest;
import com.app.bank.web.dto.LoanResponse;
import com.app.bank.web.enums.LoanStatus;
import com.app.bank.web.enums.LoanType;
import com.app.bank.web.exception.BankException;
import com.app.bank.web.model.Account;
import com.app.bank.web.model.Loan;
import com.app.bank.web.model.LoanRepayment;
import com.app.bank.web.model.User;
import com.app.bank.web.processor.CoreEngineProcessor;
import com.app.bank.web.repository.AccountRepository;
import com.app.bank.web.repository.LoanRepository;
import com.app.bank.web.repository.UserRepository;
import com.app.bank.web.util.LoanCalculator;
import jakarta.transaction.Transactional;

@Service
public class LoanService {

	private final LoanRepository loanRepository;
	private final UserRepository userRepository;
	private final AccountRepository accountRepository;
	private final LoanRepaymentService loanRepaymentService;
	private final LoanCalculator loanCalculator;
	
	public LoanService(LoanRepository loanRepository, UserRepository userRepository,
			AccountRepository accountRepository, LoanRepaymentService loanRepaymentService, LoanCalculator loanCalculator) {
		this.loanRepository = loanRepository;
		this.userRepository = userRepository;
		this.accountRepository = accountRepository;
		this.loanRepaymentService = loanRepaymentService;
		this.loanCalculator = loanCalculator;
	}
	
	@Transactional
	public LoanResponse applyForLoan(LoanRequest request, String username) {
		User user = userRepository.findByUsername(username).orElseThrow(() -> new BankException("User not Found"));
		Account account = accountRepository.findByAccountNumber(request.getAccountNumber()).orElseThrow(() -> new BankException("Account Not Found"));
		if(!account.getUser().getId().equals(user.getId())) {
			throw new BankException("Account not belong's to you");
		}
		
		if(!isEligibleForLoan(user, account, request)) {
			throw new BankException("Loan application is not eligible");
		}
		
		Loan loan = new Loan();
		loan.setLoanAccountNumber(generateLoanAccountNumber());
		loan.setUser(user);
		loan.setLinkedAccount(account);
		loan.setLoanType(LoanType.valueOf(request.getLoanType()));
		loan.setPrincipalAmount(request.getPrincipalAmount());
		loan.setInterestRate(request.getInterestRate());
		loan.setTenureMonths(request.getTenureMonths());
		Map<String, BigDecimal> calculations = loanCalculator.calculateEMI(request.getPrincipalAmount(), 
				request.getInterestRate(), 
				request.getTenureMonths());
		loan.setEmiAmount(calculations.get("emi"));
		loan.setTotalAmount(calculations.get("total"));
		loan.setDisbursementDate(LocalDate.now());
		loan.setMaturityDate(LocalDate.now().plusMonths(request.getTenureMonths()));
		loan.setStatus(LoanStatus.PENDING);
		loanRepository.save(loan);
		return mapToLoanResponse(loan);
	}
	
	@Transactional
	public LoanResponse approveLoan(Long loanId, String approvedBy) {
		Loan loan = loanRepository.findById(loanId).orElseThrow(() -> new BankException("Loan not found"));
		if(!loan.getStatus().equals(LoanStatus.PENDING)) {
			throw new BankException("Loan cannot be approved in current status");
		}
		Account account = loan.getLinkedAccount();
		account.setBalance(account.getBalance().add(loan.getPrincipalAmount()));
		accountRepository.save(account);
		loan.setStatus(LoanStatus.APPROVED);
		loan.setApprovedDate(LocalDateTime.now());
		loan.setApprovedBy(approvedBy);
		loanRepository.save(loan);
		loanRepaymentService.generateRepaymentSchedule(loan, approvedBy);
		return mapToLoanResponse(loan);
	}
	
	@Transactional
	public Map<String, Object> repayLoan(String loanAccountNumber, BigDecimal amount, String username){
		Loan loan = loanRepository.findByLoanAccountNumber(loanAccountNumber).orElseThrow(() -> new BankException("Loan Account not found"));
		User user = userRepository.findByUsername(username).orElseThrow(() -> new BankException("User not found"));
		if(!loan.getUser().getId().equals(user.getId())) {
			throw new BankException("This loan doesn't blngs to you");
		}
		Account account = loan.getLinkedAccount();
		if(account.getBalance().compareTo(amount) < 0) {
			throw new BankException("Insufficent balance for repayment");
		}
		account.setBalance(account.getBalance().subtract(amount));
		accountRepository.save(account);
		LoanRepayment loanRepayment = new LoanRepayment();
		loanRepayment.setLoan(loan);
		loanRepayment.setDueAmount(amount);
		loanRepayment.setPaymentDate(LocalDate.now());
		loanRepayment.setRemainingPrincipal(calculateRemainingBalance(loan, amount));
		if(isLoanFullyRepaid(loan)) {
			loan.setStatus(LoanStatus.CLOSED);
			loanRepository.save(loan);
		}
		Map<String, Object> response = new HashMap<>();
		response.put("message", "Loan repayment successful");
		response.put("remining balance", calculateRemainingBalance(loan, amount));
		return response;
	}
	
	
	public List<LoanResponse> getUserLoans(String username){
		User user = userRepository.findByUsername(username).orElseThrow(() -> new BankException("User not found"));
		return loanRepository.findByUserId(user.getId()).stream()
				.map(this::mapToLoanResponse)
				.collect(Collectors.toList());
	}
	
//	public Page<Loan> getAllLoans(LoanStatus status, Pageable pageable){
//		if(status != null) {
//			return loanRepository.findByStatus(status, pageable);
//		}
//		return loanRepository.findAll(pageable);
//	}
	
	public Page<LoanResponse> getAllLoans(LoanStatus status, Pageable pageable){
	    Page<Loan> loans;

	    if(status != null) {
	        loans = loanRepository.findByStatus(status, pageable);
	    } else {
	        loans = loanRepository.findAll(pageable);
	    }

	    return loans.map(this::mapToLoanResponse);
	}


	private boolean isLoanFullyRepaid(Loan loan) {
		BigDecimal totalPaid = loan.getRepayments().stream()
				.map(LoanRepayment::getDueAmount)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		return totalPaid.compareTo(loan.getTotalAmount()) >= 0;
	}

	private BigDecimal calculateRemainingBalance(Loan loan, BigDecimal payment) {
		BigDecimal totalPaid = loan.getRepayments().stream()
				.map(LoanRepayment::getDueAmount)
				.reduce(BigDecimal.ZERO, BigDecimal::add)
				.add(payment);
		return loan.getTotalAmount().subtract(totalPaid);
	}

	private LoanResponse mapToLoanResponse(Loan loan) {
		LoanResponse loanResponse = new LoanResponse();
		loanResponse.setId(loan.getId());
		loanResponse.setLoanAccountNumber(loan.getLoanAccountNumber());
		loanResponse.setLoanType(loan.getLoanType().name());
		loanResponse.setPrincipalAmount(loan.getPrincipalAmount());
		loanResponse.setInterestRate(loan.getInterestRate());
		loanResponse.setTenureMonths(loan.getTenureMonths());
		loanResponse.setEmiAmount(loan.getEmiAmount());
		loanResponse.setTotalAmount(loan.getTotalAmount());
		loanResponse.setStatus(loan.getStatus().name());
		loanResponse.setAppliedDate(loan.getAppliedDate().toString());
		loanResponse.setDisbursementDate(loan.getDisbursementDate());
		loanResponse.setMaturityDate(loan.getMaturityDate());
		loanResponse.setCustomerName(loan.getUser().getFirstName() + " " + loan.getUser().getLastName());
		loanResponse.setCustomerEmail(loan.getUser().getEmail());
		return loanResponse;
	}

	private String generateLoanAccountNumber() {
		String loanNumber = "LOAN" + System.currentTimeMillis();
		return loanNumber;
	}

	private boolean isEligibleForLoan(User user, Account account, LoanRequest request) {
		if(account.getBalance().compareTo(BigDecimal.valueOf(10000)) < 0) {
			return false;
		}
		long activeLoans = loanRepository.countByUserIdAndStatus(user.getId(), LoanStatus.APPROVED);
		if(activeLoans >= 2) {
			return false;
		}
		BigDecimal maxLoanAmount = account.getBalance().multiply(BigDecimal.valueOf(10));
		if(request.getPrincipalAmount().compareTo(maxLoanAmount) > 0) {
			return false;
		}
		return true;
	}
}
