package com.app.bank.web.repository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.app.bank.web.enums.LoanStatus;
import com.app.bank.web.enums.LoanType;
import com.app.bank.web.model.Loan;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long>{

//	Optional<Loan> findByLoanAccountNumber(String loanAccountNumber);
//    List<Loan> findByUserId(Long userId);
//    Page<Loan> findByUserId(Long userId, Pageable pageable);
//    List<Loan> findByStatus(LoanStatus status);
//    Page<Loan> findByStatus(LoanStatus status, Pageable pageable);
//    List<Loan> findByLoanType(LoanType loanType);
//    Page<Loan> findByLoanType(LoanType loanType, Pageable pageable);
//    List<Loan> findByUserIdAndStatus(Long userId, LoanStatus status);
//    List<Loan> findByUserIdAndLoanType(Long userId, LoanType loanType);
//    List<Loan> findByStatusAndLoanType(LoanStatus status, LoanType loanType);
//    Page<Loan> findByStatusAndLoanType(LoanStatus status, LoanType loanType, Pageable pageable);
//    List<Loan> findByUserIdAndStatusAndLoanType(Long userId, LoanStatus status, LoanType loanType);
//    boolean existsByUserIdAndStatusIn(Long userId, List<LoanStatus> statuses);
//    long countByStatus(LoanStatus status);
//    long countByLoanType(LoanType loanType);
//    long countByUserIdAndStatus(Long userId, LoanStatus status);
//    List<Loan> findByAppliedDateBetween(LocalDateTime startDate, LocalDateTime endDate);
//    Page<Loan> findByAppliedDateBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
//    @Query("SELECT l FROM Loan l WHERE l.disbursementDate BETWEEN :startDate AND :endDate")
//    List<Loan> findDisbursedLoansBetween(@Param("startDate") LocalDate startDate, 
//                                        @Param("endDate") LocalDate endDate);
//    List<Loan> findByMaturityDateAfter(LocalDate date);
//    List<Loan> findByMaturityDateBefore(LocalDate date);
//    List<Loan> findByApprovedBy(String approvedBy);
//    List<Loan> findByPrincipalAmountGreaterThan(BigDecimal amount);
//    List<Loan> findByPrincipalAmountLessThan(BigDecimal amount);
//    List<Loan> findByPrincipalAmountBetween(BigDecimal minAmount, BigDecimal maxAmount);
//    List<Loan> findByInterestRateGreaterThan(BigDecimal rate);
//    List<Loan> findByInterestRateLessThan(BigDecimal rate);
//    @Query("SELECT COALESCE(SUM(l.principalAmount), 0) FROM Loan l "
//    + "WHERE l.status IN ('APPROVED', 'DISBURSED', 'ACTIVE', 'NPA', 'DELINQUENT')")
//    BigDecimal getTotalDisbursedAmount();
//    @Query("SELECT COALESCE(SUM(l.principalAmount), 0) FROM Loan l WHERE l.status = :status")
//    BigDecimal getTotalAmountByStatus(@Param("status") LoanStatus status);
//    @Query("SELECT COALESCE(SUM(l.principalAmount), 0) FROM Loan l WHERE l.loanType = :loanType")
//    BigDecimal getTotalAmountByLoanType(@Param("loanType") LoanType loanType);
//    @Query("SELECT COALESCE(SUM(l.principalAmount), 0) FROM Loan l WHERE "
//    + "l.status IN ('APPROVED', 'DISBURSED', 'ACTIVE', 'DELINQUENT', 'NPA')")
//    BigDecimal getTotalOutstandingPrincipal();
//    @Query("SELECT COALESCE(SUM(l.totalAmount - l.principalAmount), 0) FROM Loan l "
//    + "WHERE l.status IN ('APPROVED', 'DISBURSED', 'ACTIVE', 'DELINQUENT', 'NPA')")
//    BigDecimal getTotalExpectedInterest();
//    @Query("SELECT l FROM Loan l WHERE l.status = 'APPROVED' AND l.disbursementDate <= CURRENT_DATE")
//    List<Loan> findLoansDueForDisbursement();
//    @Query("SELECT l FROM Loan l WHERE l.status IN ('DISBURSED', 'ACTIVE')")
//    List<Loan> findActiveLoans();
//    @Query("SELECT l FROM Loan l WHERE l.status IN ('DELINQUENT', 'NPA')")
//    List<Loan> findDelinquentLoans();
//    @Query("SELECT l FROM Loan l WHERE l.maturityDate BETWEEN CURRENT_DATE AND CURRENT_DATE + 30 AND l.status = 'ACTIVE'")
//    List<Loan> findLoansNearingMaturity();
//    @Query("SELECT l FROM Loan l WHERE l.approvedBy = :approvedBy AND l.approvedDate BETWEEN :startDate AND :endDate")
//    List<Loan> findLoansApprovedByUserBetweenDates(@Param("approvedBy") String approvedBy,
//                                                  @Param("startDate") LocalDateTime startDate,
//                                                  @Param("endDate") LocalDateTime endDate);
//    @Query("SELECT l FROM Loan l WHERE l.principalAmount > :minAmount AND l.interestRate > :minRate AND l.tenureMonths < :maxTenure")
//    List<Loan> findHighRiskLoans(@Param("minAmount") BigDecimal minAmount,
//                                @Param("minRate") BigDecimal minRate,
//                                @Param("maxTenure") Integer maxTenure);
//    @Query("SELECT YEAR(l.appliedDate) as year, MONTH(l.appliedDate) as month, " +
//           "COUNT(l) as count, SUM(l.principalAmount) as totalAmount " +
//           "FROM Loan l " +
//           "WHERE l.appliedDate BETWEEN :startDate AND :endDate " +
//           "GROUP BY YEAR(l.appliedDate), MONTH(l.appliedDate) " +
//           "ORDER BY year DESC, month DESC")
//    List<Object[]> getLoanStatisticsByMonth(@Param("startDate") LocalDateTime startDate,
//                                           @Param("endDate") LocalDateTime endDate);
//    @Query("SELECT l.loanType, COUNT(l), SUM(l.principalAmount) " +
//           "FROM Loan l " +
//           "WHERE l.status IN ('APPROVED', 'DISBURSED', 'ACTIVE', 'DELINQUENT', 'NPA') " +
//           "GROUP BY l.loanType")
//    List<Object[]> getLoanDistributionByType();
//    @Query("SELECT l FROM Loan l WHERE l.status IN ('APPROVED', 'DISBURSED', 'ACTIVE') " +
//           "ORDER BY l.principalAmount DESC")
//    Page<Loan> findTopLoansByAmount(Pageable pageable);
//    @Query("SELECT l FROM Loan l WHERE l.status = 'DELINQUENT' " +
//           "AND EXISTS (SELECT 1 FROM LoanRepayment lr WHERE lr.loan = l " +
//           "AND lr.status IN ('PENDING', 'PARTIALLY_PAID') " +
//           "AND lr.dueDate < CURRENT_DATE - :days)")
//    List<Loan> findLoansOverdueForMoreThanDays(@Param("days") Long days);
////    @Query("SELECT DISTINCT l FROM Loan l JOIN l.repayments lr " +
////           "WHERE l.status = 'DELINQUENT' " +
////           "AND lr.status IN ('PENDING', 'PARTIALLY_PAID') " +
////           "AND lr.dueDate < CURRENT_DATE - 90")
////    List<Loan> findLoansToConvertToNPA();
//    @Query("""
//    	    SELECT DISTINCT l
//    	    FROM Loan l
//    	    JOIN l.repayments r
//    	    WHERE l.status = com.app.bank.web.enums.LoanStatus.DELINQUENT
//    	      AND r.status IN (
//    	          com.app.bank.web.enums.RepaymentStatus.PENDING,
//    	          com.app.bank.web.enums.RepaymentStatus.PARTIALLY_PAID
//    	      )
//    	      AND r.dueDate < :cutoffDate
//    	    """)
//    	    List<Loan> findLoansToConvertToNPA(@Param("cutoffDate") LocalDate cutoffDate);
//    @Query("SELECT l FROM Loan l WHERE l.branchCode = :branchCode")
//    List<Loan> findByBranchCode(@Param("branchCode") String branchCode);
//    @Query("SELECT l FROM Loan l WHERE " +
//           "(:loanAccountNumber IS NULL OR l.loanAccountNumber LIKE %:loanAccountNumber%) AND " +
//           "(:customerName IS NULL OR CONCAT(l.user.firstName, ' ', l.user.lastName) LIKE %:customerName%) AND " +
//           "(:status IS NULL OR l.status = :status) AND " +
//           "(:loanType IS NULL OR l.loanType = :loanType) AND " +
//           "(:minAmount IS NULL OR l.principalAmount >= :minAmount) AND " +
//           "(:maxAmount IS NULL OR l.principalAmount <= :maxAmount) AND " +
//           "(:startDate IS NULL OR l.appliedDate >= :startDate) AND " +
//           "(:endDate IS NULL OR l.appliedDate <= :endDate)")
//    Page<Loan> searchLoans(@Param("loanAccountNumber") String loanAccountNumber,
//                          @Param("customerName") String customerName,
//                          @Param("status") LoanStatus status,
//                          @Param("loanType") LoanType loanType,
//                          @Param("minAmount") BigDecimal minAmount,
//                          @Param("maxAmount") BigDecimal maxAmount,
//                          @Param("startDate") LocalDateTime startDate,
//                          @Param("endDate") LocalDateTime endDate,
//                          Pageable pageable);
//    @Query("SELECT " +
//           "COUNT(CASE WHEN l.status = 'APPROVED' THEN 1 END) as approvedCount, " +
//           "COUNT(CASE WHEN l.status = 'DISBURSED' THEN 1 END) as disbursedCount, " +
//           "COUNT(CASE WHEN l.status = 'ACTIVE' THEN 1 END) as activeCount, " +
//           "COUNT(CASE WHEN l.status = 'DELINQUENT' THEN 1 END) as delinquentCount, " +
//           "COUNT(CASE WHEN l.status = 'NPA' THEN 1 END) as npaCount, " +
//           "COUNT(CASE WHEN l.status = 'CLOSED' THEN 1 END) as closedCount, " +
//           "SUM(CASE WHEN l.status IN ('APPROVED', 'DISBURSED', 'ACTIVE', 'DELINQUENT', 'NPA') "
//           + "THEN l.principalAmount ELSE 0 END) as totalPortfolio, " +
//           "SUM(CASE WHEN l.status IN ('DELINQUENT', 'NPA') THEN l.principalAmount ELSE 0 END) as riskyPortfolio " +
//           "FROM Loan l")
//    Object[] getLoanPerformanceMetrics();
//    @Query("SELECT l FROM Loan l WHERE l.user.panNumber = :panNumber")
//    List<Loan> findByUserPanNumber(@Param("panNumber") String panNumber);
//    @Query("SELECT l FROM Loan l WHERE l.user.aadharNumber = :aadharNumber")
//    List<Loan> findByUserAadharNumber(@Param("aadharNumber") String aadharNumber);
//    @Query("SELECT l.loanType, AVG(l.principalAmount) FROM Loan l GROUP BY l.loanType")
//    List<Object[]> getAverageLoanAmountByType();
//    @Query("SELECT DISTINCT l FROM Loan l JOIN l.repayments lr " +
//           "WHERE l.status = 'ACTIVE' " +
//           "AND lr.status IN ('PARTIALLY_PAID', 'OVERDUE') " +
//           "GROUP BY l " +
//           "HAVING COUNT(lr) > 3")
//    List<Loan> findLoansWithIrregularPayments();
//    @Query("SELECT l FROM Loan l WHERE l.status = 'ACTIVE' AND " +
//           "DATEDIFF(CURRENT_DATE, l.disbursementDate) > 180") 
//    List<Loan> findLoansEligibleForPrepayment();
//    @Query("SELECT l FROM Loan l WHERE l.collateralValue IS NOT NULL AND " +
//           "l.collateralValue < (l.principalAmount * 1.5)") 
//    List<Loan> findLoansWithInsufficientCollateral();
////    @Query("SELECT l FROM Loan l WHERE l.emiAmount > (l.user.monthlyIncome * 0.5)")
////    List<Loan> findLoansWithHighDebtToIncomeRatio();
//    @Query("""
//    		SELECT l
//    		FROM Loan l
//    		JOIN l.user u
//    		WHERE l.emiAmount > (u.monthlyIncome * :ratio)
//    		""")
//    		List<Loan> findLoansWithHighDebtToIncomeRatio(@Param("ratio") BigDecimal ratio);
//    @Query("""
//    	    SELECT DISTINCT l
//    	    FROM Loan l
//    	    JOIN l.repayments r
//    	    WHERE l.status = com.app.bank.web.enums.LoanStatus.DELINQUENT
//    	      AND r.status IN (
//    	          com.app.bank.web.enums.RepaymentStatus.PENDING,
//    	          com.app.bank.web.enums.RepaymentStatus.PARTIALLY_PAID
//    	      )
//    	      AND r.dueDate < :cutoffDate
//    	    """)
//    	    List<Loan> findLoansOverdueSince(@Param("cutoffDate") LocalDate cutoffDate);
	
	Optional<Loan> findByLoanAccountNumber(String loanAccountNumber);

    List<Loan> findByUserId(Long userId);
    Page<Loan> findByUserId(Long userId, Pageable pageable);

    List<Loan> findByStatus(LoanStatus status);
    Page<Loan> findByStatus(LoanStatus status, Pageable pageable);

    List<Loan> findByLoanType(LoanType loanType);
    Page<Loan> findByLoanType(LoanType loanType, Pageable pageable);

    List<Loan> findByUserIdAndStatus(Long userId, LoanStatus status);
    List<Loan> findByUserIdAndLoanType(Long userId, LoanType loanType);
    List<Loan> findByStatusAndLoanType(LoanStatus status, LoanType loanType);
    Page<Loan> findByStatusAndLoanType(LoanStatus status, LoanType loanType, Pageable pageable);

    boolean existsByUserIdAndStatusIn(Long userId, List<LoanStatus> statuses);

    long countByStatus(LoanStatus status);
    long countByLoanType(LoanType loanType);
    long countByUserIdAndStatus(Long userId, LoanStatus status);

    List<Loan> findByAppliedDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    Page<Loan> findByAppliedDateBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    @Query("SELECT l FROM Loan l WHERE l.disbursementDate BETWEEN :startDate AND :endDate")
    List<Loan> findDisbursedLoansBetween(@Param("startDate") LocalDate startDate,
                                         @Param("endDate") LocalDate endDate);

    List<Loan> findByMaturityDateAfter(LocalDate date);
    List<Loan> findByMaturityDateBefore(LocalDate date);

    List<Loan> findByApprovedBy(String approvedBy);

    List<Loan> findByPrincipalAmountGreaterThan(BigDecimal amount);
    List<Loan> findByPrincipalAmountLessThan(BigDecimal amount);
    List<Loan> findByPrincipalAmountBetween(BigDecimal minAmount, BigDecimal maxAmount);

    List<Loan> findByInterestRateGreaterThan(BigDecimal rate);
    List<Loan> findByInterestRateLessThan(BigDecimal rate);

    /* ====================== AGGREGATES ====================== */

    @Query("""
        SELECT COALESCE(SUM(l.principalAmount), 0)
        FROM Loan l
        WHERE l.status IN (
            com.app.bank.web.enums.LoanStatus.APPROVED,
            com.app.bank.web.enums.LoanStatus.DISBURSED,
            com.app.bank.web.enums.LoanStatus.ACTIVE,
            com.app.bank.web.enums.LoanStatus.DELINQUENT,
            com.app.bank.web.enums.LoanStatus.NPA
        )
    """)
    BigDecimal getTotalDisbursedAmount();

    @Query("SELECT COALESCE(SUM(l.principalAmount), 0) FROM Loan l WHERE l.status = :status")
    BigDecimal getTotalAmountByStatus(@Param("status") LoanStatus status);

    @Query("SELECT COALESCE(SUM(l.principalAmount), 0) FROM Loan l WHERE l.loanType = :loanType")
    BigDecimal getTotalAmountByLoanType(@Param("loanType") LoanType loanType);

    /* ====================== STATUS QUERIES ====================== */

    @Query("SELECT l FROM Loan l WHERE l.status = com.app.bank.web.enums.LoanStatus.APPROVED")
    List<Loan> findLoansDueForDisbursement();

    @Query("""
        SELECT l FROM Loan l
        WHERE l.status IN (
            com.app.bank.web.enums.LoanStatus.DISBURSED,
            com.app.bank.web.enums.LoanStatus.ACTIVE
        )
    """)
    List<Loan> findActiveLoans();

    @Query("""
        SELECT l FROM Loan l
        WHERE l.status IN (
            com.app.bank.web.enums.LoanStatus.DELINQUENT,
            com.app.bank.web.enums.LoanStatus.NPA
        )
    """)
    List<Loan> findDelinquentLoans();

    @Query("""
        SELECT l FROM Loan l
        WHERE l.status = com.app.bank.web.enums.LoanStatus.ACTIVE
          AND l.maturityDate BETWEEN :startDate AND :endDate
    """)
    List<Loan> findLoansNearingMaturity(@Param("startDate") LocalDate startDate,
                                        @Param("endDate") LocalDate endDate);

    /* ====================== RISK & NPA ====================== */

    @Query("""
        SELECT DISTINCT l
        FROM Loan l
        JOIN l.repayments r
        WHERE l.status = com.app.bank.web.enums.LoanStatus.DELINQUENT
          AND r.status IN (
              com.app.bank.web.enums.RepaymentStatus.PENDING,
              com.app.bank.web.enums.RepaymentStatus.PARTIALLY_PAID
          )
          AND r.dueDate < :cutoffDate
    """)
    List<Loan> findLoansOverdueSince(@Param("cutoffDate") LocalDate cutoffDate);

    @Query("""
        SELECT l
        FROM Loan l
        JOIN l.user u
        WHERE l.emiAmount > (u.monthlyIncome * :ratio)
    """)
    List<Loan> findLoansWithHighDebtToIncomeRatio(@Param("ratio") BigDecimal ratio);

    /* ====================== SEARCH ====================== */

    @Query("""
        SELECT l FROM Loan l
        WHERE (:loanAccountNumber IS NULL OR l.loanAccountNumber LIKE %:loanAccountNumber%)
          AND (:customerName IS NULL OR CONCAT(l.user.firstName, ' ', l.user.lastName) LIKE %:customerName%)
          AND (:status IS NULL OR l.status = :status)
          AND (:loanType IS NULL OR l.loanType = :loanType)
          AND (:minAmount IS NULL OR l.principalAmount >= :minAmount)
          AND (:maxAmount IS NULL OR l.principalAmount <= :maxAmount)
          AND (:startDate IS NULL OR l.appliedDate >= :startDate)
          AND (:endDate IS NULL OR l.appliedDate <= :endDate)
    """)
    Page<Loan> searchLoans(
            @Param("loanAccountNumber") String loanAccountNumber,
            @Param("customerName") String customerName,
            @Param("status") LoanStatus status,
            @Param("loanType") LoanType loanType,
            @Param("minAmount") BigDecimal minAmount,
            @Param("maxAmount") BigDecimal maxAmount,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable
    );
}
