package com.app.bank.web.util;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import com.app.bank.web.enums.LoanStatus;
import com.app.bank.web.enums.RepaymentStatus;
import com.app.bank.web.model.Loan;
import com.app.bank.web.model.LoanRepayment;

@Component
public class LoanCalculator {

	public Map<String, BigDecimal> calculateEMI(BigDecimal principal, BigDecimal annualRate, int tenureMonths){
		BigDecimal monthlyRate = annualRate.divide(BigDecimal.valueOf(1200), 10, RoundingMode.HALF_UP);
		BigDecimal onePlusR = BigDecimal.ONE.add(monthlyRate);
		BigDecimal power = onePlusR.pow(tenureMonths);
		BigDecimal numerator = principal.multiply(monthlyRate).multiply(power);
		BigDecimal denominator = power.subtract(BigDecimal.ONE);
		BigDecimal emi = numerator.divide(denominator, 2, RoundingMode.HALF_UP);
		BigDecimal totalAmount = emi.multiply(BigDecimal.valueOf(tenureMonths));
		BigDecimal totalInterest = totalAmount.subtract(principal);
		Map<String, BigDecimal> response = new HashMap<>();
		response.put("EMI", emi);
		response.put("Total", totalAmount);
		response.put("Interest", totalInterest);
		return response;
	}
	
	public List<LoanRepayment> generateRepaymentSchedule(Loan loan){
		List<LoanRepayment> schedule = new ArrayList<>();
		BigDecimal principal = loan.getPrincipalAmount();
		BigDecimal monthlyRate = loan.getInterestRate().divide(BigDecimal.valueOf(1200), 10, RoundingMode.HALF_UP);
		BigDecimal emi = loan.getEmiAmount();
		BigDecimal reminingPrincipal = principal;
		LocalDate dueDate = loan.getDisbursementDate().plusMonths(1);
		for(int i = 1; i <= loan.getTenureMonths(); i++) {
			LoanRepayment loanRepayment = new LoanRepayment();
			loanRepayment.setLoan(loan);
			loanRepayment.setInstallmentNumber(i);
			loanRepayment.setDueDate(dueDate);
			loanRepayment.setDueAmount(emi);
			BigDecimal interest = reminingPrincipal.multiply(monthlyRate).setScale(2, RoundingMode.HALF_UP);
			BigDecimal principalPortion = emi.subtract(interest);
			if(i == loan.getTenureMonths()) {
				principalPortion = reminingPrincipal;
				loanRepayment.setDueAmount(principalPortion.add(interest));
			}
			loanRepayment.setPrincipalAmount(principalPortion);
			loanRepayment.setInterestAmount(interest);
			loanRepayment.setStatus(RepaymentStatus.PENDING);
			loanRepayment.setRemainingPrincipal(reminingPrincipal.subtract(principalPortion));
			loanRepayment.setTotalOutstanding(loan.getTotalAmount().subtract(emi.multiply(BigDecimal.valueOf(i))));
			loanRepayment.setGracePeriodEndDate(dueDate.plusDays(15));
			loanRepayment.setPenaltyRate(loan.getInterestRate().add(BigDecimal.valueOf(2)));
			schedule.add(loanRepayment);
			reminingPrincipal = reminingPrincipal.subtract(principalPortion);
			dueDate = dueDate.plusMonths(1);
		}
		return schedule;
	}
	
	public BigDecimal claculatePrePaymentCharges(Loan loan, BigDecimal prePaymentAmount) {
		BigDecimal charges = BigDecimal.ZERO;
		switch(loan.getLoanType()) {
		case HOME_LOAN:
			charges = prePaymentAmount.multiply(BigDecimal.valueOf(0.02)).min(BigDecimal.valueOf(1000));
			break;
		case PERSONAL_LOAN:
			charges = loan.getPrincipalAmount().multiply(BigDecimal.valueOf(0.04));
			break;
		case CAR_LOAN:
			charges = prePaymentAmount.multiply(BigDecimal.valueOf(0.03));
			break;
		default:
			charges = prePaymentAmount.multiply(BigDecimal.valueOf(0.02));
		}
		return charges.setScale(2, RoundingMode.HALF_UP);
	}
	
	public BigDecimal calculateIntrest(BigDecimal principal, BigDecimal rate, int days) {
		BigDecimal dailyRate = rate.divide(BigDecimal.valueOf(36500), 10, RoundingMode.HALF_UP);
		return principal.multiply(dailyRate).multiply(BigDecimal.valueOf(days)).setScale(2, RoundingMode.HALF_UP);
	}
	
	public BigDecimal calculateEligibleAmount(BigDecimal monthlyIncome, BigDecimal existingEmi) {
		BigDecimal maxTotalEmi = monthlyIncome.multiply(BigDecimal.valueOf(0.6));
		BigDecimal availableEmi = maxTotalEmi.subtract(existingEmi);
		BigDecimal monthlyRate = BigDecimal.valueOf(10).divide(BigDecimal.valueOf(1200), 10, RoundingMode.HALF_UP);
		int tenureMonth = 60;
		BigDecimal onePlusR = BigDecimal.ONE.add(monthlyRate);
		BigDecimal power = onePlusR.pow(tenureMonth);
		BigDecimal denominator = monthlyRate.multiply(power).divide(power.subtract(BigDecimal.ONE), 10, RoundingMode.HALF_UP);
		return availableEmi.divide(denominator, 2, RoundingMode.HALF_UP);
	}
}
