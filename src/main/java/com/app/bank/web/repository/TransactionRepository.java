package com.app.bank.web.repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.app.bank.web.model.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>{

	Optional<Transaction> findByTransactionId(String transactionId);
//	@Query("SELECT t FROM Transaction t WHERE t.fromAccount.accountNumber = :accountNumber OR "
//	+ "t.toAcconut.accountNumber = :accountNumber")
//	Page<Transaction> findByAccountNumber(String accountNumber, Pageable pageable);
	@Query("""
	        SELECT t
	        FROM Transaction t
	        WHERE t.fromAccount.accountNumber = :accountNumber
	           OR t.toAccount.accountNumber = :accountNumber
	    """)
	    Page<Transaction> findByAccountNumber(
	            @Param("accountNumber") String accountNumber,
	            Pageable pageable
	    );
	List<Transaction> findByTransactionDateBetween(LocalDateTime start, LocalDateTime end);
	@Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.transactionType = 'DEPOSIT' AND "
	+ "t.status = 'SUCCESS' AND t.transactionDate BETWEEN :start AND :end")
    Double getTotalDeposits(LocalDateTime start, LocalDateTime end);
	@Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.transactionType = 'WITHDRAWAL' AND "
	+ "t.status = 'SUCCESS' AND t.transactionDate BETWEEN :start AND :end")
	Double getTotalWithdrawals(LocalDateTime start, LocalDateTime end);
}
