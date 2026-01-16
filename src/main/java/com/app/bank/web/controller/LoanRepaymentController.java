package com.app.bank.web.controller;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.app.bank.web.model.LoanRepayment;
import com.app.bank.web.service.LoanRepaymentService;

@Controller
@RequestMapping("/api/v1/auth/repayments")
public class LoanRepaymentController {

	private final LoanRepaymentService loanRepaymentService;
	
	public LoanRepaymentController(LoanRepaymentService loanRepaymentService) {
		this.loanRepaymentService = loanRepaymentService;
	}
	
	@GetMapping("/schedule/{loanId}") // http://localhost:8989/api/v1/auth/repayments/schedule/{loanId}
	public ResponseEntity<List<LoanRepayment>> getSchedule(@PathVariable Long loanId){
		return ResponseEntity.ok(loanRepaymentService.getRepaySchedule(loanId));
	}
	
	@GetMapping("/next-due/{loanId}") // http://localhost:8989/api/v1/auth/repayments/next-due/{loanId}
	public ResponseEntity<LoanRepayment> getNextDueRepayment(@PathVariable Long loanId){
		return ResponseEntity.ok(loanRepaymentService.getNextDueRepayment(loanId));
	}
	
	@GetMapping("/overdue/{loanId}") // http://localhost:8989/api/v1/auth/repayments/overdue/{loanId}
	public ResponseEntity<List<LoanRepayment>> getOverduePayments(@PathVariable Long loanId){
		return ResponseEntity.ok(loanRepaymentService.getOverduePayments(loanId));
	}
	
	@PostMapping("/pay-emi/{loanId}") // http://localhost:8989/api/v1/auth/repayments/pay-emi/{loanId}?amount=1000
	public ResponseEntity<Map<String, Object>> payEmi(@PathVariable Long loanId,
			@RequestParam BigDecimal amount,
			Authentication authentication){
		return ResponseEntity.ok(loanRepaymentService.payEmi(loanId, amount, authentication.getName()));
	}
	
	
	@GetMapping("total-paid/{loanId}") // http://localhost:8989/api/v1/auth/repayments/total-paid/{loanId}
	public ResponseEntity<BigDecimal> getTotalPaid(@PathVariable Long loanId){
		return ResponseEntity.ok(loanRepaymentService.getTotalPaid(loanId));
	}
	
	@GetMapping("/overdue-amount/{loanId}") // http://localhost:8989/api/v1/auth/repayments/overdue-amount/{loanId}
	public ResponseEntity<BigDecimal> getOverdueAmount(@PathVariable Long loanId){
		return ResponseEntity.ok(loanRepaymentService.getOverDueAmount(loanId));
	}
}
