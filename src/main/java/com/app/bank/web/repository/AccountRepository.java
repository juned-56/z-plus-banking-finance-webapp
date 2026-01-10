package com.app.bank.web.repository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.app.bank.web.enums.AccountStatus;
import com.app.bank.web.model.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long>{

	Optional<Account> findByAccountNumber(String accountNumber);
    List<Account> findByUserId(Long userId);
    List<Account> findByStatus(AccountStatus status);
    long countByAccountType(String accountType);
    @Query("SELECT SUM(a.balance) FROM Account a WHERE a.status = 'ACTIVE'")
    BigDecimal getTotalBankBalance();
    @Query("SELECT COUNT(a) FROM Account a WHERE a.balance < a.minimumBalance")
    long countAccountsBelowMinimumBalance();
    
}
