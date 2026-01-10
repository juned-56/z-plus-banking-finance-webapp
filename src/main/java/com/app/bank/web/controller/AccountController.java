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
import com.app.bank.web.dto.AccountResponse;
import com.app.bank.web.dto.CreateAccountRequest;
import com.app.bank.web.dto.TransactionRequest;
import com.app.bank.web.model.Transaction;
import com.app.bank.web.service.AccountService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class AccountController {

	private final AccountService accountService;
	
		public AccountController(AccountService accountService) {
		this.accountService = accountService;
	}
		
		@PostMapping("/create-account") // http://localhost:8989/api/v1/auth/create-account
		public ResponseEntity<AccountResponse> createAccount(@Valid @RequestBody CreateAccountRequest accountRequest){
			return ResponseEntity.ok(accountService.createAccount(accountRequest));
		}
		
		@PostMapping("/transfer") // http://localhost:8989/api/v1/auth/transfer
		public ResponseEntity<Map<String, Object>> transferFund(@Valid @RequestBody TransactionRequest request, Authentication authentication){
			return ResponseEntity.ok(accountService.transferFund(request, authentication.getName()));
			
		}
		
		@PostMapping("/{accountNumber}/deposit") // http://localhost:8989/api/v1/auth/{accountNumber}/deposit
		public ResponseEntity<Map<String, Object>> deposit(@PathVariable String accountNumber, @RequestParam BigDecimal amount,
				 @RequestParam String description){
			return ResponseEntity.ok(accountService.deposit(accountNumber, amount, description));
		}
		
		@PostMapping("/{accountNumber}/withdraw") // http://localhost:8989/api/v1/auth/{accountNumber}/withdraw
		public ResponseEntity<Map<String, Object>> withdraw(@PathVariable String accountNumber, 
				@RequestParam BigDecimal amount,
				 @RequestParam String description,
				 Authentication authentication){
			return ResponseEntity.ok(accountService.withdraw(accountNumber, amount, description, authentication.getName()));
		}
		
		@GetMapping("/{accountNumber}") // http://localhost:8989/api/v1/auth/{accountNumber}
		public ResponseEntity<AccountResponse> getAcconut(@PathVariable String accountNumber, Authentication authentication){
			return ResponseEntity.ok(accountService.getAccount(accountNumber, authentication.getName()));
		}
		
		
		@GetMapping("/{accountNumber}/transactions") // http://localhost:8989/api/v1/auth/{accountNumber}/transactions
		public ResponseEntity<Page<Transaction>> getTransaction(@PathVariable String accountNumber, Pageable pageable){
			return ResponseEntity.ok(accountService.getAccountTransaction(accountNumber, pageable));
		}
		
		@GetMapping("/my-accounts") // http://localhost:8989/api/v1/auth/my-accounts
		public ResponseEntity<List<AccountResponse>> getMyAccounts(Authentication authentication){
			return ResponseEntity.ok(accountService.getUserAccount(authentication.getName()));
		}
		
		@GetMapping("/{accountNumber}/balance") // http://localhost:8989/api/v1/auth/{accountNumber}/balance
		public ResponseEntity<Map<String, Object>> getBalance(@PathVariable String accountNumber, Authentication authentication){
			return ResponseEntity.ok(accountService.getAccountBalance(accountNumber, authentication.getName()));
		}
}
