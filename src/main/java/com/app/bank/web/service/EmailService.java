package com.app.bank.web.service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import com.app.bank.web.model.Account;
import com.app.bank.web.model.Loan;
import com.app.bank.web.model.LoanRepayment;
import com.app.bank.web.model.Transaction;
import com.app.bank.web.model.User;

@Service
public interface EmailService {

	
    void sendWelcomeEmail(User user);
    void sendAccountVerificationEmail(User user, String verificationLink);
    void sendPasswordResetEmail(User user, String resetLink);
    void sendAccountLockedEmail(User user);
    void sendAccountUnlockedEmail(User user);
    void sendProfileUpdatedEmail(User user);
    void sendKycVerifiedEmail(User user);
    void sendKycRejectedEmail(User user, String reason);
    void sendOtpEmail(String email, String otp, String purpose);
    void sendOtpEmail(String email, String otp, String purpose, int expiryMinutes);
    void sendAccountCreationEmail(User user, Account account);
    void sendAccountClosedEmail(User user, Account account);
    void sendAccountStatusChangeEmail(User user, Account account, String oldStatus, String newStatus);
    void sendAccountBalanceAlert(User user, Account account, BigDecimal threshold);
    void sendLowBalanceAlert(User user, Account account);
    void sendAccountStatementEmail(User user, Account account, byte[] statementPdf);
    void sendTransactionEmail(User user, Transaction transaction);
    void sendFundTransferEmail(User sender, User receiver, Transaction transaction);
    void sendDepositConfirmationEmail(User user, Transaction transaction);
    void sendWithdrawalConfirmationEmail(User user, Transaction transaction);
    void sendFailedTransactionEmail(User user, Transaction transaction, String reason);
    void sendSuspiciousActivityEmail(User user, Transaction transaction);
    void sendLoanApplicationEmail(User user, Loan loan);
    void sendLoanApprovalEmail(User user, Loan loan);
    void sendLoanRejectionEmail(User user, Loan loan, String reason);
    void sendLoanDisbursementEmail(User user, Loan loan);
    void sendLoanRepaymentReminderEmail(User user, LoanRepayment repayment);
    void sendLoanPaymentConfirmation(User user, LoanRepayment repayment, Transaction transaction);
    void sendOverdueNotification(User user, LoanRepayment repayment);
    void sendNPANotification(Loan loan);
    void sendLoanClosureEmail(User user, Loan loan);
    void sendLoginAlertEmail(User user, String ipAddress, String deviceInfo);
    void sendPasswordChangedEmail(User user);
    void sendContactInfoChangedEmail(User user, String changedField);
    void sendBeneficiaryAddedEmail(User user, String beneficiaryName);
    void sendBeneficiaryDeletedEmail(User user, String beneficiaryName);
    void sendServiceRequestConfirmation(User user, String serviceType, String requestId);
    void sendServiceRequestUpdate(User user, String serviceType, String requestId, String status);
    void sendMaintenanceNotification(User user, String maintenanceSchedule);
    void sendHolidayNotification(User user, List<LocalDate> holidays);
    void sendNewProductOfferEmail(User user, String productName);
    void sendInvestmentOpportunityEmail(User user);
    void sendLoanOfferEmail(User user, String loanType, BigDecimal eligibleAmount);
    void sendMonthlyStatementEmail(User user, List<Account> accounts, byte[] statement);
    void sendTaxStatementEmail(User user, int financialYear, byte[] taxStatement);
    void sendInterestCertificateEmail(User user, int financialYear, byte[] certificate);
    boolean sendEmail(String to, String subject, String body);
    boolean sendEmailWithAttachment(String to, String subject, String body, 
                                   String attachmentName, byte[] attachment);
    boolean sendBulkEmail(List<String> recipients, String subject, String body);

}
