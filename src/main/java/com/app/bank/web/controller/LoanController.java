package com.app.bank.web.controller;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.app.bank.web.dto.LoanRequest;
import com.app.bank.web.dto.LoanResponse;
import com.app.bank.web.enums.LoanStatus;
import com.app.bank.web.model.Loan;
import com.app.bank.web.service.LoanService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class LoanController {

		private final LoanService loanService;
		
		public LoanController(LoanService loanService) {
			this.loanService = loanService;
		}
		
		@PostMapping("/apply-loan") // http://localhost:8989/api/v1/auth/apply-loan
		public ResponseEntity<LoanResponse> applyForLoan(@Valid @RequestBody LoanRequest request, Authentication authentication){
			return ResponseEntity.ok(loanService.applyForLoan(request, authentication.getName()));
		}
		
		@PostMapping("/{loanId}/approve-loan") // http://localhost:8989/api/v1/auth/{loanId}/approve-loan
		public ResponseEntity<LoanResponse> approveLoan(@PathVariable Long loanId, Authentication authentication){
			return ResponseEntity.ok(loanService.approveLoan(loanId, authentication.getName()));
		}
		
		@PostMapping("/{loanAccountNumber}/repay-loan") // http://localhost:8989/api/v1/auth/{loanAccountNumber}/repay-loan	
		public ResponseEntity<Map<String, Object>> repayLoan(@PathVariable String loanAccountNumber, 
				@RequestParam BigDecimal amount,
				Authentication authentication){
			return ResponseEntity.ok(loanService.repayLoan(loanAccountNumber, amount, authentication.getName()));
		}
		
		
		@GetMapping("/my-loans") // http://localhost:8989/api/v1/auth/my-loans
		public ResponseEntity<List<LoanResponse>> getMyLoans(Authentication authentication){
			return ResponseEntity.ok(loanService.getUserLoans(authentication.getName()));
		}
		
		@GetMapping("/all-loans") // http://localhost:8989/api/v1/auth/all-loans
		public ResponseEntity<Page<Loan>> getAllLoans(@RequestParam(required = false) LoanStatus status,
				Pageable pageable){
			return ResponseEntity.ok(loanService.getAllLoans(status, pageable));
		}
}

