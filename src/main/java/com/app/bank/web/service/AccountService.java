package com.app.bank.web.service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.app.bank.web.dto.AccountResponse;
import com.app.bank.web.dto.CreateAccountRequest;
import com.app.bank.web.dto.TransactionRequest;
import com.app.bank.web.enums.AccountStatus;
import com.app.bank.web.enums.AccountType;
import com.app.bank.web.enums.TransactionStatus;
import com.app.bank.web.enums.TransactionType;
import com.app.bank.web.exception.BankException;
import com.app.bank.web.model.Account;
import com.app.bank.web.model.Transaction;
import com.app.bank.web.model.User;
import com.app.bank.web.repository.AccountRepository;
import com.app.bank.web.repository.TransactionRepository;
import com.app.bank.web.repository.UserRepository;
import com.app.bank.web.util.AccountNumberGenerator;
import jakarta.transaction.Transactional;

@Service
public class AccountService {

	
	private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final AccountNumberGenerator accountNumberGenerator;

    public AccountService(AccountRepository accountRepository, UserRepository userRepository,
                          TransactionRepository transactionRepository, AccountNumberGenerator accountNumberGenerator) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.accountNumberGenerator = accountNumberGenerator;
    }

    @Value("${app.transaction.max-withdrawal-limit:50000}")
    private BigDecimal maxWithdrawalLimit;

    @Value("${app.transaction.min-balance:1000}")
    private BigDecimal minBalance;

    @Transactional
    public AccountResponse createAccount(CreateAccountRequest request) {
        User user = userRepository.findById(Long.parseLong(request.getUserId()))
                .orElseThrow(() -> new BankException("User Not Found"));

        if (request.getInitialDeposit().compareTo(minBalance) < 0) {
            throw new BankException("Initial deposit must be at least: " + minBalance);
        }

        Account account = new Account();
        account.setAccountNumber(accountNumberGenerator.generate());
        account.setUser(user);
        account.setAccountType(AccountType.valueOf(request.getAccountType()));
        account.setBalance(request.getInitialDeposit());
        account.setStatus(AccountStatus.ACTIVE);
        account.setMinimumBalance(minBalance);

        accountRepository.save(account);

        createTransaction(null, account, request.getInitialDeposit(),
                TransactionType.DEPOSIT, "Initial Deposit");

        return mapToAccountResponse(account);
    }

    @Transactional
    public Map<String, Object> transferFund(TransactionRequest request, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BankException("User Not Found"));

        Account fromAccount = accountRepository.findByAccountNumber(request.getFromAccountNumber())
                .orElseThrow(() -> new BankException("From Account Not Found"));

        Account toAccount = accountRepository.findByAccountNumber(request.getToAccountNumber())
                .orElseThrow(() -> new BankException("To Account Not Found"));

        if (!fromAccount.getUser().getId().equals(user.getId())) {
            throw new BankException("You don't own this account");
        }

        validateAccountsForTransaction(fromAccount, toAccount);
        validateAmount(request.getAmount());

        if (fromAccount.getBalance().subtract(request.getAmount()).compareTo(fromAccount.getMinimumBalance()) < 0) {
            throw new BankException("Insufficient balance. Minimum balance must be maintained");
        }

        fromAccount.setBalance(fromAccount.getBalance().subtract(request.getAmount()));
        toAccount.setBalance(toAccount.getBalance().add(request.getAmount()));
        fromAccount.setLastTransactionAt(LocalDateTime.now());
        toAccount.setLastTransactionAt(LocalDateTime.now());

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        Transaction debitTransaction = createTransaction(fromAccount, toAccount, request.getAmount(),
                TransactionType.TRANSFER, "Transfer to " + toAccount.getAccountNumber());

        Transaction creditTransaction = createTransaction(fromAccount, toAccount, request.getAmount(),
                TransactionType.TRANSFER, "Transfer from " + fromAccount.getAccountNumber());

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Transfer Successful");
        response.put("transactionId", debitTransaction.getTransactionId());
        response.put("newBalance", fromAccount.getBalance());
        return response;
    }

    @Transactional
    public Map<String, Object> deposit(String accountNumber, BigDecimal amount, String description, String username) {
    	User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new BankException("User Not Found"));
    	
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new BankException("Account not found"));
        
        if(!account.getUser().getId().equals(user.getId())) {
        	throw new BankException("You don't own this account");
        }
        
        validateAmount(amount);
        account.setBalance(account.getBalance().add(amount));
        account.setLastTransactionAt(LocalDateTime.now());
        accountRepository.save(account);

        Transaction transaction = createTransaction(null, account, amount, TransactionType.DEPOSIT, description);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Deposit Successful");
        response.put("transactionId", transaction.getTransactionId());
        response.put("newBalance", account.getBalance());
        return response;
    }

    @Transactional
    public Map<String, Object> withdraw(String accountNumber, BigDecimal amount, String description, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BankException("User Not Found"));

        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new BankException("Account Number Not Found"));

        if (!account.getUser().getId().equals(user.getId())) {
            throw new BankException("You don't own this account");
        }

        validateAccountsForTransaction(account, null);
        validateAmount(amount);

        if (amount.compareTo(maxWithdrawalLimit) > 0) {
            throw new BankException("Withdrawal amount exceeds daily limit");
        }

        if (account.getBalance().subtract(amount).compareTo(account.getMinimumBalance()) < 0) {
            throw new BankException("Insufficient balance. Minimum balance must be maintained!");
        }

        account.setBalance(account.getBalance().subtract(amount));
        account.setLastTransactionAt(LocalDateTime.now());
        accountRepository.save(account);

        Transaction transaction = createTransaction(account, null, amount, TransactionType.WITHDRAWAL, description);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Withdrawal Successful");
        response.put("transactionId", transaction.getTransactionId());
        response.put("newBalance", account.getBalance());
        return response;
    }

    public AccountResponse getAccount(String accountNumber, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BankException("User not found"));

        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new BankException("Account not found"));

        if (!account.getUser().getId().equals(user.getId())
                && !user.getRole().name().contains("ADMIN")
                && !user.getRole().name().contains("BANK_EMPLOYEE")) {
            throw new BankException("Access Denied");
        }

        return mapToAccountResponse(account);
    }

    public Page<Transaction> getAccountTransaction(String accountNumber, Pageable pageable) {
        return transactionRepository.findByAccountNumber(accountNumber, pageable);
    }

    public List<AccountResponse> getUserAccount(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BankException("User not found"));

        return accountRepository.findByUserId(user.getId())
                .stream().map(this::mapToAccountResponse)
                .collect(Collectors.toList());
    }

    public Map<String, Object> getAccountBalance(String accountNumber, String username) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new BankException("Account not found"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BankException("User not found"));

        if (!account.getUser().getId().equals(user.getId())) {
            throw new BankException("Access Denied");
        }

        Map<String, Object> response = new HashMap<>();
        response.put("accountNumber", account.getAccountNumber());
        response.put("balance", account.getBalance());
        response.put("availableBalance", account.getBalance().subtract(account.getMinimumBalance()));
        response.put("currency", "INR");
        response.put("lastUpdated", account.getLastTransactionAt());
        return response;
    }

    // -------------------- Helper Methods --------------------

    private Transaction createTransaction(Account fromAccount, Account toAccount,
                                          BigDecimal amount, TransactionType type, String description) {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(generateTransactionId());
        transaction.setFromAccount(fromAccount);
        transaction.setToAccount(toAccount);
        transaction.setAmount(amount);
        transaction.setTransactionType(type);
        transaction.setStatus(TransactionStatus.SUCCESS);
        transaction.setDescription(description);
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setReferenceNumber(generateReferenceNumber());

        if (toAccount != null) {
            transaction.setBalanceAfterTransaction(toAccount.getBalance());
        } else if (fromAccount != null) {
            transaction.setBalanceAfterTransaction(fromAccount.getBalance());
        }

        return transactionRepository.save(transaction);
    }

    private String generateTransactionId() {
        return "TXN" + System.currentTimeMillis() + (int) (Math.random() * 1000);
    }
    
    private String generateReferenceNumber() {
		return "REF" + System.currentTimeMillis() + (int) (Math.random() * 1000);
	}

    private void validateAccountsForTransaction(Account fromAccount, Account toAccount) {
        if (fromAccount != null && !fromAccount.getStatus().equals(AccountStatus.ACTIVE)) {
            throw new BankException("From account is not active");
        }
        if (toAccount != null && !toAccount.getStatus().equals(AccountStatus.ACTIVE)) {
            throw new BankException("To account is not active");
        }
    }

    private void validateAmount(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BankException("Amount must be greater than zero");
        }
    }

    private AccountResponse mapToAccountResponse(Account account) {
        AccountResponse response = new AccountResponse();
        response.setId(account.getId());
        response.setAccountNumber(account.getAccountNumber());
        response.setAccountType(account.getAccountType().name());
        response.setBalance(account.getBalance());
        response.setStatus(account.getStatus().name());
        response.setCreatedAt(account.getCreatedAt().toString());
        response.setCustomerName(account.getUser().getFirstName() + " " + account.getUser().getLastName());
        response.setCustomerEmail(account.getUser().getEmail());
        return response;
    }
}
