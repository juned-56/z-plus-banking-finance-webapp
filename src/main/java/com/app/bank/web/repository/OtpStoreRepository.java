package com.app.bank.web.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.app.bank.web.model.OtpStore;

import jakarta.transaction.Transactional;

//@Repository
public interface OtpStoreRepository {
//		extends JpaRepository<OtpStore, Long> {

//	Optional<OtpStore> findById(Long id);
//    @Query("SELECT o FROM OtpStore o WHERE o.email = :email " +
//           "AND o.purpose = :purpose " +
//           "AND o.used = false " +
//           "AND o.expiresAt > CURRENT_TIMESTAMP " +
//           "ORDER BY o.generatedAt DESC")
//    Optional<OtpStore> findLatestValidOtp(@Param("email") String email, 
//                                         @Param("purpose") String purpose);
//    List<OtpStore> findByEmail(String email);
//    List<OtpStore> findByEmailAndPurpose(String email, String purpose);
//    List<OtpStore> findByEmailAndUsed(String email, Boolean used);
//    Optional<OtpStore> findByEmailAndOtp(String email, String otp);
//    Optional<OtpStore> findByEmailAndOtpAndPurpose(String email, String otp, String purpose);
//    Optional<OtpStore> findByEmailAndOtpAndPurposeAndUsed(String email, String otp, 
//                                                         String purpose, Boolean used);
//    @Query("SELECT o FROM OtpStore o WHERE o.email = :email " +
//           "AND o.used = false " +
//           "ORDER BY o.generatedAt DESC")
//    List<OtpStore> findUnusedOtpsByEmail(@Param("email") String email);
//    @Query("SELECT o FROM OtpStore o WHERE o.expiresAt < CURRENT_TIMESTAMP")
//    List<OtpStore> findExpiredOtps();
//    @Query("SELECT o FROM OtpStore o WHERE o.expiresAt < CURRENT_TIMESTAMP " +
//           "AND o.used = false")
//    List<OtpStore> findExpiredUnusedOtps();
//    List<OtpStore> findByGeneratedAtBetween(LocalDateTime startTime, LocalDateTime endTime);
//    List<OtpStore> findByExpiresAtBetween(LocalDateTime startTime, LocalDateTime endTime);
//    List<OtpStore> findByPurpose(String purpose);
//    List<OtpStore> findByPurposeAndUsed(String purpose, Boolean used);
//    @Query("SELECT CASE WHEN COUNT(o) > 0 THEN true ELSE false END " +
//           "FROM OtpStore o WHERE o.email = :email " +
//           "AND o.purpose = :purpose " +
//           "AND o.used = false " +
//           "AND o.expiresAt > CURRENT_TIMESTAMP")
//    boolean existsValidOtp(@Param("email") String email, 
//                          @Param("purpose") String purpose);
//    @Query("SELECT COUNT(o) FROM OtpStore o WHERE o.email = :email " +
//           "AND o.generatedAt >= :startTime")
//    Long countOtpAttemptsSince(@Param("email") String email, 
//                              @Param("startTime") LocalDateTime startTime);
//    @Query("SELECT COUNT(o) FROM OtpStore o WHERE o.email = :email " +
//           "AND o.generatedAt >= :twentyFourHoursAgo")
//    Long countRecentOtpAttempts(@Param("email") String email, 
//                               @Param("twentyFourHoursAgo") LocalDateTime twentyFourHoursAgo);
//    @Query("SELECT COUNT(o) FROM OtpStore o WHERE o.email = :email " +
//           "AND o.purpose = :purpose " +
//           "AND o.generatedAt >= :startTime")
//    Long countRecentOtpAttemptsForPurpose(@Param("email") String email, 
//                                         @Param("purpose") String purpose,
//                                         @Param("startTime") LocalDateTime startTime);
//    @Modifying
//    @Transactional
//    @Query("UPDATE OtpStore o SET o.used = true WHERE o.id = :id")
//    int markAsUsed(@Param("id") Long id);
//    @Modifying
//    @Transactional
//    @Query("UPDATE OtpStore o SET o.used = true " +
//           "WHERE o.email = :email AND o.otp = :otp AND o.purpose = :purpose")
//    int markAsUsed(@Param("email") String email, 
//                  @Param("otp") String otp, 
//                  @Param("purpose") String purpose);
//    @Modifying
//    @Transactional
//    @Query("UPDATE OtpStore o SET o.used = true WHERE o.email = :email")
//    int markAllAsUsedForEmail(@Param("email") String email);
//    @Modifying
//    @Transactional
//    @Query("UPDATE OtpStore o SET o.used = true " +
//           "WHERE o.email = :email AND o.purpose = :purpose")
//    int markAllAsUsedForEmailAndPurpose(@Param("email") String email, 
//                                       @Param("purpose") String purpose);
//    @Modifying
//    @Transactional
//    @Query("UPDATE OtpStore o SET o.used = true " +
//           "WHERE o.email = :email AND o.used = false")
//    int invalidateAllOtpsForEmail(@Param("email") String email);
//    @Modifying
//    @Transactional
//    @Query("UPDATE OtpStore o SET o.used = true " +
//           "WHERE o.email = :email AND o.purpose = :purpose AND o.used = false")
//    int invalidateOtpsForEmailAndPurpose(@Param("email") String email, 
//                                        @Param("purpose") String purpose);
//    @Modifying
//    @Transactional
//    @Query("DELETE FROM OtpStore o WHERE o.expiresAt < CURRENT_TIMESTAMP")
//    int deleteExpiredOtps();
//    @Modifying
//    @Transactional
//    @Query("DELETE FROM OtpStore o WHERE o.expiresAt < :expiryDate")
//    int deleteOtpsExpiredBefore(@Param("expiryDate") LocalDateTime expiryDate);
//    @Modifying
//    @Transactional
//    @Query("DELETE FROM OtpStore o WHERE o.used = true")
//    int deleteUsedOtps();
//    @Modifying
//    @Transactional
//    @Query("DELETE FROM OtpStore o WHERE o.used = true " +
//           "AND o.generatedAt < :generatedBefore")
//    int deleteUsedOtpsOlderThan(@Param("generatedBefore") LocalDateTime generatedBefore);
//    @Modifying
//    @Transactional
//    @Query("DELETE FROM OtpStore o WHERE " +
//           "(o.expiresAt < CURRENT_TIMESTAMP) OR " +
//           "(o.used = true AND o.generatedAt < :retentionThreshold)")
//    int cleanupOldOtps(@Param("retentionThreshold") LocalDateTime retentionThreshold);
//    @Query("SELECT o.purpose, COUNT(o) as total, " +
//           "SUM(CASE WHEN o.used = true THEN 1 ELSE 0 END) as usedCount, " +
//           "SUM(CASE WHEN o.expiresAt < CURRENT_TIMESTAMP AND o.used = false THEN 1 ELSE 0 END) as expiredUnusedCount " +
//           "FROM OtpStore o " +
//           "WHERE o.generatedAt >= :startDate " +
//           "GROUP BY o.purpose")
//    List<Object[]> getOtpUsageStatistics(@Param("startDate") LocalDateTime startDate);
//    @Query("SELECT HOUR(o.generatedAt) as hour, COUNT(o) as count " +
//           "FROM OtpStore o " +
//           "WHERE DATE(o.generatedAt) = CURRENT_DATE " +
//           "GROUP BY HOUR(o.generatedAt) " +
//           "ORDER BY hour")
//    List<Object[]> getOtpGenerationFrequencyByHour();
//    @Query("SELECT o.email, COUNT(o) as requestCount " +
//           "FROM OtpStore o " +
//           "WHERE o.generatedAt >= :startDate " +
//           "GROUP BY o.email " +
//           "ORDER BY requestCount DESC")
//    List<Object[]> getTopOtpRequestors(@Param("startDate") LocalDateTime startDate, 
//                                      org.springframework.data.domain.Pageable pageable);
//    @Query("SELECT " +
//           "COUNT(o) as total, " +
//           "SUM(CASE WHEN o.used = true THEN 1 ELSE 0 END) as used, " +
//           "SUM(CASE WHEN o.used = false AND o.expiresAt > CURRENT_TIMESTAMP THEN 1 ELSE 0 END) as unusedValid, " +
//           "SUM(CASE WHEN o.used = false AND o.expiresAt < CURRENT_TIMESTAMP THEN 1 ELSE 0 END) as unusedExpired " +
//           "FROM OtpStore o " +
//           "WHERE o.generatedAt >= :startDate")
//    Object getOtpSuccessRate(@Param("startDate") LocalDateTime startDate);
//    @Query("SELECT " +
//           "COUNT(o) as totalAttempts, " +
//           "SUM(CASE WHEN o.used = true THEN 1 ELSE 0 END) as successfulAttempts, " +
//           "AVG(TIMESTAMPDIFF(SECOND, o.generatedAt, " +
//           "    COALESCE((SELECT MAX(o2.generatedAt) FROM OtpStore o2 " +
//           "              WHERE o2.email = o.email AND o2.purpose = o.purpose AND o2.used = true), " +
//           "             o.generatedAt))) as avgTimeToUseSeconds " +
//           "FROM OtpStore o " +
//           "WHERE o.generatedAt >= :startDate")
//    Object getOtpValidationAnalysis(@Param("startDate") LocalDateTime startDate);
//    @Query("SELECT o.email, COUNT(o) as requestCount " +
//           "FROM OtpStore o " +
//           "WHERE o.generatedAt >= :recentTime " +
//           "GROUP BY o.email " +
//           "HAVING COUNT(o) > :threshold")
//    List<Object[]> detectPotentialAbuse(@Param("recentTime") LocalDateTime recentTime,
//                                       @Param("threshold") Long threshold);
//    @Query("SELECT o FROM OtpStore o WHERE o.used = false " +
//           "AND o.expiresAt < CURRENT_TIMESTAMP " +
//           "AND o.generatedAt >= :startDate " +
//           "ORDER BY o.generatedAt DESC")
//    List<OtpStore> findExpiredUnusedOtpsSince(@Param("startDate") LocalDateTime startDate);
//    @Query("SELECT o.otp, COUNT(DISTINCT o.email) as uniqueEmails, " +
//           "COUNT(o) as totalUses " +
//           "FROM OtpStore o " +
//           "WHERE o.generatedAt >= :startDate " +
//           "GROUP BY o.otp " +
//           "HAVING COUNT(DISTINCT o.email) > 1 " +
//           "OR COUNT(o) > 5")
//    List<Object[]> findSuspiciousOtpPatterns(@Param("startDate") LocalDateTime startDate);
//    @Query("SELECT CASE WHEN COUNT(o) > 0 THEN true ELSE false END " +
//           "FROM OtpStore o WHERE o.email = :email " +
//           "AND o.otp = :otp " +
//           "AND o.purpose = :purpose " +
//           "AND o.used = false " +
//           "AND o.expiresAt > CURRENT_TIMESTAMP")
//    boolean validateOtp(@Param("email") String email,
//                       @Param("otp") String otp,
//                       @Param("purpose") String purpose);
//    @Query("SELECT CASE WHEN COUNT(o) > 0 THEN true ELSE false END " +
//           "FROM OtpStore o WHERE o.email = :email " +
//           "AND o.otp = :otp " +
//           "AND o.purpose = :purpose " +
//           "AND o.generatedAt >= :recentTime")
//    boolean wasOtpRecentlyGenerated(@Param("email") String email,
//                                   @Param("otp") String otp,
//                                   @Param("purpose") String purpose,
//                                   @Param("recentTime") LocalDateTime recentTime);
//    @Query("SELECT o.expiresAt FROM OtpStore o WHERE o.email = :email " +
//           "AND o.otp = :otp AND o.purpose = :purpose")
//    Optional<LocalDateTime> getOtpExpiryTime(@Param("email") String email,
//                                            @Param("otp") String otp,
//                                            @Param("purpose") String purpose);
//    @Query("SELECT o FROM OtpStore o WHERE " +
//           "(o.expiresAt < :expiryThreshold) OR " +
//           "(o.used = true AND o.generatedAt < :cleanupThreshold)")
//    List<OtpStore> findOtpsForCleanup(@Param("expiryThreshold") LocalDateTime expiryThreshold,
//                                     @Param("cleanupThreshold") LocalDateTime cleanupThreshold);
//    @Modifying
//    @Transactional
//    @Query("DELETE FROM OtpStore o WHERE o.id IN :ids")
//    int deleteOtpsInBulk(@Param("ids") List<Long> ids);
//    @Query("SELECT o FROM OtpStore o WHERE o.transactionReference = :transactionReference")
//    Optional<OtpStore> findByTransactionReference(@Param("transactionReference") String transactionReference);
//    @Query("SELECT o FROM OtpStore o WHERE o.sessionId = :sessionId")
//    List<OtpStore> findBySessionId(@Param("sessionId") String sessionId);
//    List<OtpStore> findByIpAddress(String ipAddress);
//    List<OtpStore> findByUserAgentContaining(String userAgent);
//    @Query("SELECT COUNT(o) FROM OtpStore o")
//    Long countTotalOtps();
//    @Query("SELECT COUNT(o) FROM OtpStore o WHERE o.used = false " +
//           "AND o.expiresAt > CURRENT_TIMESTAMP")
//    Long countActiveOtps();
//    @Query(value = "SELECT " +
//                   "table_name AS `Table`, " +
//                   "ROUND(((data_length + index_length) / 1024 / 1024), 2) AS `Size (MB)` " +
//                   "FROM information_schema.TABLES " +
//                   "WHERE table_schema = DATABASE() " +
//                   "AND table_name = 'otp_store'",
//           nativeQuery = true)
//    List<Object[]> getTableSize();
}
