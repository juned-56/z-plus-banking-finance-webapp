package com.app.bank.web.service;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.app.bank.web.dto.LoginRequest;
import com.app.bank.web.dto.LoginResponse;
import com.app.bank.web.dto.RegisterRequest;
import com.app.bank.web.enums.UserRole;
import com.app.bank.web.enums.UserStatus;
import com.app.bank.web.exception.BankException;
import com.app.bank.web.model.User;
import com.app.bank.web.repository.LoanRepository;
import com.app.bank.web.repository.TransactionRepository;
import com.app.bank.web.repository.UserRepository;
import com.app.bank.web.security.JwtTokenProvider;

import jakarta.transaction.Transactional;

@Service
public class AuthService {

	private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    private final TransactionRepository transactionRepository;
    private final LoanRepository loanRepository;
	private final AuthenticationManager authenticationManager;
	private final JwtTokenProvider jwtTokenProvider; 
	private final PasswordEncoder passwordEncoder;
	private final UserRepository userRespository;
	public AuthService(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider,
			PasswordEncoder passwordEncoder, UserRepository userRespository, LoanRepository loanRepository, TransactionRepository transactionRepository) {
		this.authenticationManager = authenticationManager;
		this.jwtTokenProvider = jwtTokenProvider;
		this.passwordEncoder = passwordEncoder;
		this.userRespository = userRespository;
		this.loanRepository = loanRepository;
		this.transactionRepository = transactionRepository;
	}
	
	@Transactional
	public Map<String, String> register(RegisterRequest registerRequest){
		validateRegistration(registerRequest);
		
		User user = new User();
		user.setUsername(registerRequest.getUsername());
		user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
		user.setFirstName(registerRequest.getFirstName());
		user.setLastName(registerRequest.getLastName());
		user.setEmail(registerRequest.getEmail());
		user.setPhone(registerRequest.getPhone());
		user.setAddress(registerRequest.getAddress());
		user.setPanNumber(registerRequest.getPanNumber());
		user.setAadharNumber(registerRequest.getAadharNumber());
		//user.setRole(UserRole.CUSTOMER);
		user.setRole(
			    registerRequest.getRole() != null 
			    ? registerRequest.getRole() 
			    : UserRole.CUSTOMER
			);
		user.setStatus(UserStatus.ACTIVE);
		user.setCreatedAt(LocalDateTime.now());
		user.setMonthlyIncome(registerRequest.getMonthlyIncome());
		userRespository.save(user);
		Map<String, String> response = new HashMap<>();
		response.put("Message", "user Registred Successfully");
		response.put("userId", user.getId().toString());
		return response;
	}

	public LoginResponse login(LoginRequest request) {
		try {
			Authentication authentication = authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
			SecurityContextHolder.getContext().setAuthentication(authentication);
			User user = userRespository.findByUsername(request.getUsername()).orElseThrow(() -> 
			new BankException("User Not Found"));
			user.setLastLoginAt(LocalDateTime.now());
			user.setFailedLoginAttempts(0);
			userRespository.save(user);
			String jwt = jwtTokenProvider.generateToken(authentication);
			LoginResponse response = new LoginResponse();
			response.setToken(jwt);
			response.setUserId(user.getId());
			response.setUsername(user.getUsername());
			response.setRole(user.getRole().name());
			response.setFullName(user.getFirstName() +  " " + user.getLastName());
			return response;
		} catch (Exception e) {
			userRespository.findByUsername(request.getUsername()).ifPresent(user -> {
				user.setFailedLoginAttempts(user.getFailedLoginAttempts() + 1);
				if(user.getFailedLoginAttempts() >= 5) {
					user.setStatus(UserStatus.LOCKED);
				}
				userRespository.save(user);
			});
			throw new BankException("Invalid username or Password");
		}
	}
	
	
	@Transactional
	public Map<String, String> resetPassword(String email, String newPassword){
		User user = userRespository.findByEmail(email).orElseThrow(() -> new BankException("User Not Found"));
		user.setPassword(passwordEncoder.encode(newPassword));
		userRespository.save(user);
		Map<String, String> response = new HashMap<>();
		response.put("message", "Password reset Successfully");
		return response;
	}
	
	private void validateRegistration(RegisterRequest registerRequest) {
		if(userRespository.existsByUsername(registerRequest.getUsername())) {
			throw new BankException("UserName Already Exists");
		}
		if(userRespository.existsByEmail(registerRequest.getEmail())) {
			throw new BankException("Email Already Exists");
		}
		if(userRespository.existsByPhone(registerRequest.getPhone())) {
			throw new BankException("Phone Number Already Exists");
		}
		if(userRespository.existsByPanNumber(registerRequest.getPanNumber())) {
			throw new BankException("Pan Card is already Exists");
		}
		if(userRespository.existsByAadharNumber(registerRequest.getAadharNumber())) {
			throw new BankException("Aadhar card is already registred");
		}
	}
	
	
}
