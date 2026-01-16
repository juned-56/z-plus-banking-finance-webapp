package com.app.bank.web.repository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.app.bank.web.enums.RepaymentStatus;
import com.app.bank.web.model.LoanRepayment;

import jakarta.transaction.Transactional;

@Repository
public interface LoanRepaymentRepository extends JpaRepository<LoanRepayment, Long>{

Optional<LoanRepayment> findById(Long id);
    
    
//    List<LoanRepayment> findByLoanId(Long loanId);
//    Page<LoanRepayment> findByLoanId(Long loanId, Pageable pageable);
//    List<LoanRepayment> findByStatus(RepaymentStatus status);
//    Page<LoanRepayment> findByStatus(RepaymentStatus status, Pageable pageable);
//    List<LoanRepayment> findByLoanIdAndStatus(Long loanId, RepaymentStatus status);
//    Page<LoanRepayment> findByLoanIdAndStatus(Long loanId, RepaymentStatus status, Pageable pageable);
//    Optional<LoanRepayment> findByLoanIdAndInstallmentNumber(Long loanId, Integer installmentNumber);
//    List<LoanRepayment> findByLoanIdAndStatusIn(Long loanId, List<RepaymentStatus> statuses);
//    List<LoanRepayment> findByPaymentDateBetween(LocalDate startDate, LocalDate endDate);
//    List<LoanRepayment> findByDueDateBetween(LocalDate startDate, LocalDate endDate);
//    Page<LoanRepayment> findByDueDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);
//    List<LoanRepayment> findByDueDate(LocalDate dueDate);
//    List<LoanRepayment> findByPaymentMode(String paymentMode);
//    Optional<LoanRepayment> findByPaymentReference(String paymentReference);
//    List<LoanRepayment> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
//    @Query("SELECT lr FROM LoanRepayment lr WHERE lr.loan.id = :loanId " +
//           "AND lr.status = 'PENDING' " +
//           "AND lr.dueDate >= CURRENT_DATE " +
//           "ORDER BY lr.dueDate ASC")
//    Optional<LoanRepayment> findNextDueRepayment(@Param("loanId") Long loanId);
//    @Query("SELECT lr FROM LoanRepayment lr WHERE lr.loan.id = :loanId " +
//           "AND lr.status IN ('PENDING', 'PARTIALLY_PAID') " +
//           "AND lr.dueDate < CURRENT_DATE")
//    List<LoanRepayment> findOverdueRepayments(@Param("loanId") Long loanId);
//    @Query("SELECT lr FROM LoanRepayment lr WHERE " +
//           "lr.status IN ('PENDING', 'PARTIALLY_PAID') " +
//           "AND lr.dueDate < CURRENT_DATE")
//    List<LoanRepayment> findAllOverdueRepayments();
//    @Query("SELECT lr FROM LoanRepayment lr WHERE " +
//           "lr.status IN ('PENDING', 'PARTIALLY_PAID') " +
//           "AND lr.dueDate < CURRENT_DATE")
//    Page<LoanRepayment> findAllOverdueRepayments(Pageable pageable);
//    @Query("SELECT lr FROM LoanRepayment lr WHERE lr.loan.id IN :loanIds " +
//           "AND lr.status IN ('PENDING', 'PARTIALLY_PAID') " +
//           "AND lr.dueDate < CURRENT_DATE")
//    List<LoanRepayment> findOverdueRepaymentsByLoanIds(@Param("loanIds") List<Long> loanIds);
////    @Query("SELECT lr FROM LoanRepayment lr WHERE " +
////           "lr.status = 'PENDING' " +
////           "AND lr.dueDate BETWEEN CURRENT_DATE AND CURRENT_DATE + :days")
////    List<LoanRepayment> findRepaymentsDueInNextDays(@Param("days") Long days);
//    @Query("SELECT lr FROM LoanRepayment lr WHERE lr.status = 'PENDING' AND lr.dueDate BETWEEN CURRENT_DATE AND :endDate")
//    List<LoanRepayment> findRepaymentsDueInNextDays(@Param("endDate") LocalDate endDate);
//    @Query("SELECT lr FROM LoanRepayment lr WHERE lr.latePaymentFee > 0")
//    List<LoanRepayment> findRepaymentsWithLateFees();
//    @Query("SELECT lr FROM LoanRepayment lr WHERE lr.status = 'PARTIALLY_PAID'")
//    List<LoanRepayment> findPartiallyPaidRepayments();
//    @Query("SELECT lr FROM LoanRepayment lr WHERE lr.status = 'ADVANCED_PAID'")
//    List<LoanRepayment> findAdvancedPaidRepayments();
////    @Query("SELECT lr FROM LoanRepayment lr WHERE lr.status = 'DEFAULTED' OR " +
////           "(lr.status IN ('PENDING', 'PARTIALLY_PAID') AND lr.dueDate < CURRENT_DATE - 90)")
////    List<LoanRepayment> findDefaultedRepayments();
//    @Query("SELECT lr FROM LoanRepayment lr " +
//    	       "WHERE lr.status = com.app.bank.web.enums.RepaymentStatus.DEFAULTED " +
//    	       "OR (lr.status IN (com.app.bank.web.enums.RepaymentStatus.PENDING, com.app.bank.web.enums.RepaymentStatus.PARTIALLY_PAID) " +
//    	       "AND lr.dueDate < :cutoffDate)")
//    	List<LoanRepayment> findDefaultedRepayments(@Param("cutoffDate") LocalDate cutoffDate);
//    @Query("SELECT lr FROM LoanRepayment lr WHERE " +
//           "lr.status IN ('PENDING', 'PARTIALLY_PAID') " +
//           "AND lr.dueDate < CURRENT_DATE " +
//           "AND (lr.gracePeriodEndDate IS NULL OR CURRENT_DATE <= lr.gracePeriodEndDate)")
//    List<LoanRepayment> findRepaymentsInGracePeriod();
//    @Query("SELECT COALESCE(SUM(lr.paidAmount), 0) FROM LoanRepayment lr " +
//           "WHERE lr.loan.id = :loanId " +
//           "AND lr.status IN ('PAID', 'ADVANCED_PAID', 'PARTIALLY_PAID')")
//    BigDecimal getTotalPaidAmount(@Param("loanId") Long loanId);
//    @Query("SELECT COALESCE(SUM(lr.principalAmount), 0) FROM LoanRepayment lr " +
//           "WHERE lr.loan.id = :loanId " +
//           "AND lr.status IN ('PAID', 'ADVANCED_PAID')")
//    BigDecimal getTotalPrincipalPaid(@Param("loanId") Long loanId);
//    @Query("SELECT COALESCE(SUM(lr.interestAmount), 0) FROM LoanRepayment lr " +
//           "WHERE lr.loan.id = :loanId " +
//           "AND lr.status IN ('PAID', 'ADVANCED_PAID')")
//    BigDecimal getTotalInterestPaid(@Param("loanId") Long loanId);
//    @Query("SELECT COALESCE(SUM(lr.latePaymentFee), 0) FROM LoanRepayment lr " +
//           "WHERE lr.loan.id = :loanId")
//    BigDecimal getTotalLatePaymentFees(@Param("loanId") Long loanId);
//    @Query("SELECT COALESCE(SUM(lr.dueAmount - lr.paidAmount), 0) FROM LoanRepayment lr " +
//           "WHERE lr.loan.id = :loanId " +
//           "AND lr.status IN ('PENDING', 'PARTIALLY_PAID') " +
//           "AND lr.dueDate < CURRENT_DATE")
//    BigDecimal getOverdueAmount(@Param("loanId") Long loanId);
//    @Query("SELECT COALESCE(SUM(lr.dueAmount - lr.paidAmount + lr.latePaymentFee), 0) " +
//           "FROM LoanRepayment lr WHERE lr.loan.id = :loanId " +
//           "AND lr.status IN ('PENDING', 'PARTIALLY_PAID') " +
//           "AND lr.dueDate < CURRENT_DATE")
//    BigDecimal getTotalOverdueAmountWithFees(@Param("loanId") Long loanId);
//    @Query("SELECT COUNT(lr) FROM LoanRepayment lr WHERE lr.loan.id = :loanId " +
//           "AND lr.status IN ('PENDING', 'PARTIALLY_PAID') " +
//           "AND lr.dueDate < CURRENT_DATE")
//    Long countOverdueRepayments(@Param("loanId") Long loanId);
//    @Query("SELECT lr.status, COUNT(lr) FROM LoanRepayment lr " +
//           "WHERE lr.loan.id = :loanId GROUP BY lr.status")
//    List<Object[]> countRepaymentsByStatus(@Param("loanId") Long loanId);
//    @Query("SELECT " +
//           "COUNT(lr) as totalInstallments, " +
//           "COUNT(CASE WHEN lr.status = 'PAID' THEN 1 END) as paidCount, " +
//           "COUNT(CASE WHEN lr.status = 'PENDING' THEN 1 END) as pendingCount, " +
//           "COUNT(CASE WHEN lr.status = 'PARTIALLY_PAID' THEN 1 END) as partialCount, " +
//           "COUNT(CASE WHEN lr.status IN ('PENDING', 'PARTIALLY_PAID') AND lr.dueDate < CURRENT_DATE THEN 1 END) as overdueCount, " +
//           "SUM(lr.dueAmount) as totalDueAmount, " +
//           "SUM(lr.paidAmount) as totalPaidAmount, " +
//           "SUM(lr.latePaymentFee) as totalLateFees " +
//           "FROM LoanRepayment lr WHERE lr.loan.id = :loanId")
//    Object getRepaymentSummary(@Param("loanId") Long loanId);
//    @Query("SELECT YEAR(lr.paymentDate), MONTH(lr.paymentDate), " +
//           "COUNT(lr), SUM(lr.paidAmount), SUM(lr.principalAmount), SUM(lr.interestAmount) " +
//           "FROM LoanRepayment lr " +
//           "WHERE lr.paymentDate IS NOT NULL " +
//           "AND lr.paymentDate BETWEEN :startDate AND :endDate " +
//           "AND lr.status IN ('PAID', 'ADVANCED_PAID', 'PARTIALLY_PAID') " +
//           "GROUP BY YEAR(lr.paymentDate), MONTH(lr.paymentDate) " +
//           "ORDER BY YEAR(lr.paymentDate) DESC, MONTH(lr.paymentDate) DESC")
//    List<Object[]> getMonthlyRepaymentCollection(@Param("startDate") LocalDate startDate,
//                                                @Param("endDate") LocalDate endDate);
//    @Query("SELECT l.loanType, " +
//           "COUNT(lr), " +
//           "SUM(lr.paidAmount), " +
//           "AVG(lr.paidAmount), " +
//           "SUM(CASE WHEN lr.status IN ('PENDING', 'PARTIALLY_PAID') AND lr.dueDate < CURRENT_DATE THEN 1 ELSE 0 END) as overdueCount " +
//           "FROM LoanRepayment lr " +
//           "JOIN lr.loan l " +
//           "GROUP BY l.loanType")
//    List<Object[]> getRepaymentTrendsByLoanType();
//    @Query("SELECT l.user.id, l.user.firstName, l.user.lastName, " +
//           "COUNT(DISTINCT l.id) as loanCount, " +
//           "SUM(lr.paidAmount) as totalPaid, " +
//           "MAX(lr.paymentDate) as lastPaymentDate " +
//           "FROM LoanRepayment lr " +
//           "JOIN lr.loan l " +
//           "WHERE lr.status IN ('PAID', 'ADVANCED_PAID') " +
//           "GROUP BY l.user.id, l.user.firstName, l.user.lastName " +
//           "ORDER BY totalPaid DESC")
//    List<Object[]> getTopPayingCustomers(Pageable pageable);
//    @Query("SELECT " +
//           "COUNT(CASE WHEN lr.paymentDate <= lr.dueDate THEN 1 END) as onTimePayments, " +
//           "COUNT(CASE WHEN lr.paymentDate > lr.dueDate THEN 1 END) as latePayments, " +
//           "COUNT(CASE WHEN lr.paymentDate IS NULL AND lr.dueDate < CURRENT_DATE THEN 1 END) as missedPayments, " +
//           "COUNT(*) as totalPayments " +
//           "FROM LoanRepayment lr " +
//           "WHERE lr.loan.id = :loanId " +
//           "AND lr.status IN ('PAID', 'ADVANCED_PAID', 'PARTIALLY_PAID')")
//    Object getRepaymentEfficiency(@Param("loanId") Long loanId);
//    @Modifying
//    @Transactional
//    @Query("UPDATE LoanRepayment lr SET lr.status = :status, lr.updatedAt = CURRENT_TIMESTAMP " +
//           "WHERE lr.id = :repaymentId")
//    int updateRepaymentStatus(@Param("repaymentId") Long repaymentId, 
//                            @Param("status") RepaymentStatus status);
//    @Modifying
//    @Transactional
//    @Query("UPDATE LoanRepayment lr SET lr.latePaymentFee = :lateFee, lr.updatedAt = CURRENT_TIMESTAMP " +
//           "WHERE lr.id = :repaymentId")
//    int updateLatePaymentFee(@Param("repaymentId") Long repaymentId, 
//                           @Param("lateFee") BigDecimal lateFee);
////    @Modifying
////    @Transactional
////    @Query("UPDATE LoanRepayment lr SET lr.status = 'DEFAULTED', lr.updatedAt = CURRENT_TIMESTAMP " +
////           "WHERE lr.status IN ('PENDING', 'PARTIALLY_PAID') " +
////           "AND lr.dueDate < CURRENT_DATE - 90")
////    int markRepaymentsAsDefaulted();
//    @Modifying
//    @Transactional
//    @Query("UPDATE LoanRepayment lr " +
//           "SET lr.status = com.app.bank.web.enums.RepaymentStatus.DEFAULTED, " +
//           "    lr.updatedAt = CURRENT_TIMESTAMP " +
//           "WHERE lr.status IN (com.app.bank.web.enums.RepaymentStatus.PENDING, com.app.bank.web.enums.RepaymentStatus.PARTIALLY_PAID) " +
//           "  AND lr.dueDate < :cutoffDate")
//    int markRepaymentsAsDefaulted(@Param("cutoffDate") LocalDate cutoffDate);
//    @Modifying
//    @Transactional
//    @Query("DELETE FROM LoanRepayment lr WHERE lr.loan.id = :loanId")
//    void deleteByLoanId(@Param("loanId") Long loanId);
//    @Query("SELECT lr FROM LoanRepayment lr WHERE " +
//           "(:loanAccountNumber IS NULL OR lr.loan.loanAccountNumber LIKE %:loanAccountNumber%) AND " +
//           "(:customerName IS NULL OR CONCAT(lr.loan.user.firstName, ' ', lr.loan.user.lastName) LIKE %:customerName%) AND " +
//           "(:status IS NULL OR lr.status = :status) AND " +
//           "(:installmentNumber IS NULL OR lr.installmentNumber = :installmentNumber) AND " +
//           "(:paymentMode IS NULL OR lr.paymentMode LIKE %:paymentMode%) AND " +
//           "(:minAmount IS NULL OR lr.dueAmount >= :minAmount) AND " +
//           "(:maxAmount IS NULL OR lr.dueAmount <= :maxAmount) AND " +
//           "(:startDate IS NULL OR lr.dueDate >= :startDate) AND " +
//           "(:endDate IS NULL OR lr.dueDate <= :endDate)")
//    Page<LoanRepayment> searchRepayments(@Param("loanAccountNumber") String loanAccountNumber,
//                                        @Param("customerName") String customerName,
//                                        @Param("status") RepaymentStatus status,
//                                        @Param("installmentNumber") Integer installmentNumber,
//                                        @Param("paymentMode") String paymentMode,
//                                        @Param("minAmount") BigDecimal minAmount,
//                                        @Param("maxAmount") BigDecimal maxAmount,
//                                        @Param("startDate") LocalDate startDate,
//                                        @Param("endDate") LocalDate endDate,
//                                        Pageable pageable);
//    @Query("SELECT " +
//           "DAYOFWEEK(lr.paymentDate) as dayOfWeek, " +
//           "HOUR(lr.createdAt) as hourOfDay, " +
//           "COUNT(lr) as paymentCount, " +
//           "AVG(lr.paidAmount) as avgAmount " +
//           "FROM LoanRepayment lr " +
//           "WHERE lr.loan.user.id = :userId " +
//           "AND lr.paymentDate IS NOT NULL " +
//           "AND lr.status IN ('PAID', 'ADVANCED_PAID') " +
//           "GROUP BY DAYOFWEEK(lr.paymentDate), HOUR(lr.createdAt)")
//    List<Object[]> getCustomerPaymentPattern(@Param("userId") Long userId);
//    @Query("SELECT lr FROM LoanRepayment lr WHERE lr.remarks LIKE '%bounce%' OR lr.remarks LIKE '%return%'")
//    List<LoanRepayment> findRepaymentsWithBouncedPayments();
//    @Query("SELECT AVG(DATEDIFF(lr.paymentDate, lr.dueDate)) FROM LoanRepayment lr " +
//           "WHERE lr.paymentDate > lr.dueDate " +
//           "AND lr.status IN ('PAID', 'ADVANCED_PAID')")
//    Double getAverageDaysLate();
//    @Query("SELECT SUM(lr.dueAmount) FROM LoanRepayment lr " +
//           "WHERE lr.status = 'PENDING' " +
//           "AND lr.dueDate BETWEEN :startDate AND :endDate")
//    BigDecimal getRepaymentForecast(@Param("startDate") LocalDate startDate,
//                                   @Param("endDate") LocalDate endDate);
//    @Query("SELECT lr FROM LoanRepayment lr WHERE " +
//           "lr.status = 'PENDING' AND " +
//           "lr.dueDate <= :processingDate")
//    List<LoanRepayment> findRepaymentsForBatchProcessing(@Param("processingDate") LocalDate processingDate);
//    @Modifying
//    @Transactional
//    @Query("UPDATE LoanRepayment lr SET lr.status = :status, lr.updatedAt = CURRENT_TIMESTAMP " +
//           "WHERE lr.id IN :repaymentIds")
//    int updateBulkRepaymentStatus(@Param("repaymentIds") List<Long> repaymentIds,
//                                 @Param("status") RepaymentStatus status);
//    @Query("SELECT lr FROM LoanRepayment lr WHERE lr.loan.id = :loanId " +
//           "AND lr.status = 'PENDING' " +
//           "ORDER BY lr.installmentNumber ASC")
//    Optional<LoanRepayment> findFirstUnpaidInstallment(@Param("loanId") Long loanId);
//    @Query("SELECT lr FROM LoanRepayment lr WHERE lr.loan.id = :loanId " +
//           "AND lr.status IN ('PAID', 'ADVANCED_PAID') " +
//           "ORDER BY lr.installmentNumber DESC")
//    Optional<LoanRepayment> findLastPaidInstallment(@Param("loanId") Long loanId);
//    @Query("SELECT lr FROM LoanRepayment lr WHERE lr.paymentDate IS NOT NULL " +
//           "AND lr.paymentReference IS NULL")
//    List<LoanRepayment> findRepaymentsWithoutTransactionReference();
//    @Query("SELECT lr.paymentMode, COUNT(lr), SUM(lr.paidAmount) " +
//           "FROM LoanRepayment lr " +
//           "WHERE lr.paymentMode IS NOT NULL " +
//           "AND lr.status IN ('PAID', 'ADVANCED_PAID') " +
//           "GROUP BY lr.paymentMode")
//    List<Object[]> getRepaymentStatsByPaymentMode();


List<LoanRepayment> findByLoanId(Long loanId);
Optional<LoanRepayment> findByLoanIdAndInstallmentNumber(Long loanId, Integer installmentNumber);
List<LoanRepayment> findByLoanIdAndStatus(Long loanId, RepaymentStatus status);
List<LoanRepayment> findByStatus(RepaymentStatus status);

//@Query("SELECT lr FROM LoanRepayment lr WHERE lr.loan.id = :loanId AND lr.status = 'PENDING' ORDER BY lr.dueDate ASC")
//Optional<LoanRepayment> findNextDueRepayment(@Param("loanId") Long loanId);

//@Query("SELECT lr FROM LoanRepayment lr WHERE lr.loan.id = :loanId AND lr.status = 'PENDING' ORDER BY lr.dueDate ASC")
//Page<LoanRepayment> findNextDueRepayment(@Param("loanId") Long loanId, Pageable pageable);

@Query(value = "SELECT * FROM loan_repayments WHERE loan_id = :loanId AND status = 'PENDING' ORDER BY due_date ASC LIMIT 1", nativeQuery = true)
Optional<LoanRepayment> findNextDueRepayment(@Param("loanId") Long loanId);


@Query("SELECT lr FROM LoanRepayment lr WHERE lr.loan.id = :loanId AND lr.status IN ('PENDING', 'PARTIALLY_PAID') AND lr.dueDate < CURRENT_DATE")
List<LoanRepayment> findOverdueRepayments(@Param("loanId") Long loanId);

@Query("SELECT COALESCE(SUM(lr.paidAmount), 0) FROM LoanRepayment lr WHERE lr.loan.id = :loanId AND lr.status IN ('PAID', 'ADVANCED_PAID', 'PARTIALLY_PAID')")
BigDecimal getTotalPaidAmount(@Param("loanId") Long loanId);

@Query("SELECT COALESCE(SUM(lr.dueAmount - lr.paidAmount), 0) FROM LoanRepayment lr WHERE lr.loan.id = :loanId AND lr.status IN ('PENDING', 'PARTIALLY_PAID') AND lr.dueDate < CURRENT_DATE")
BigDecimal getOverdueAmount(@Param("loanId") Long loanId, @Param("currentDate") LocalDate currentDate);

@Modifying
@Transactional
@Query("UPDATE LoanRepayment lr SET lr.status = :status WHERE lr.id = :repaymentId")
int updateRepaymentStatus(@Param("repaymentId") Long repaymentId, @Param("status") RepaymentStatus status);
}
