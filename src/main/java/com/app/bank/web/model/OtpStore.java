package com.app.bank.web.model;
import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

//@Entity
//@Table(name = "otp_store")
public class OtpStore {

//	@Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//    @Column(nullable = false)
//    private String email;   
//    @Column(nullable = false)
//    private String otp;    
//    @Column(nullable = false)
//    private LocalDateTime generatedAt = LocalDateTime.now();    
//    @Column(nullable = false)
//    private LocalDateTime expiresAt;    
//    @Column(nullable = false)
//    private Boolean used = false;    
//    @Column(nullable = false)
//    private String purpose; // LOGIN, TRANSACTION, FORGOT_PASSWORD
//    
//    public OtpStore() {
//		// TODO Auto-generated constructor stub
//	}
//
//	public Long getId() {
//		return id;
//	}
//
//	public void setId(Long id) {
//		this.id = id;
//	}
//
//	public String getEmail() {
//		return email;
//	}
//
//	public void setEmail(String email) {
//		this.email = email;
//	}
//
//	public String getOtp() {
//		return otp;
//	}
//
//	public void setOtp(String otp) {
//		this.otp = otp;
//	}
//
//	public LocalDateTime getGeneratedAt() {
//		return generatedAt;
//	}
//
//	public void setGeneratedAt(LocalDateTime generatedAt) {
//		this.generatedAt = generatedAt;
//	}
//
//	public LocalDateTime getExpiresAt() {
//		return expiresAt;
//	}
//
//	public void setExpiresAt(LocalDateTime expiresAt) {
//		this.expiresAt = expiresAt;
//	}
//
//	public Boolean getUsed() {
//		return used;
//	}
//
//	public void setUsed(Boolean used) {
//		this.used = used;
//	}
//
//	public String getPurpose() {
//		return purpose;
//	}
//
//	public void setPurpose(String purpose) {
//		this.purpose = purpose;
//	}
//
//	public OtpStore(Long id, String email, String otp, LocalDateTime generatedAt, LocalDateTime expiresAt, Boolean used,
//			String purpose) {
//		super();
//		this.id = id;
//		this.email = email;
//		this.otp = otp;
//		this.generatedAt = generatedAt;
//		this.expiresAt = expiresAt;
//		this.used = used;
//		this.purpose = purpose;
//	}
}
