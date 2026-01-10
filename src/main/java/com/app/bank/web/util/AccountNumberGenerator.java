package com.app.bank.web.util;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class AccountNumberGenerator {

//	private static final String BANK_CODE = "ZPB";
//    private static final AtomicLong counter = new AtomicLong(System.currentTimeMillis());
//    public String generate() {
//        long timestamp = System.currentTimeMillis() % 1000000000L; // Last 9 digits
//        long sequence = counter.incrementAndGet() % 10000; // Last 4 digits
//        
//        return String.format("%s%09d%04d", BANK_CODE, timestamp, sequence);
//    }
//    public String generateLoanAccountNumber() {
//        return "LN" + generate();
//    }
//    public boolean isValidAccountNumber(String accountNumber) {
//        if (accountNumber == null || accountNumber.length() != 16) {
//            return false;
//        }
//        
//        return accountNumber.startsWith(BANK_CODE) && 
//               accountNumber.substring(3).matches("\\d+");
//    }
	
	
	
	
	
	
	
	
	
	
	
	private static final String BANK_CODE = "ZPB";
    private static final AtomicLong counter = new AtomicLong(System.currentTimeMillis());

    public String generate() {
        long timestamp = System.currentTimeMillis() % 1_000_000_000L; // Last 9 digits
        long sequence = counter.incrementAndGet() % 10_000; // Last 4 digits
        return String.format("%s%09d%04d", BANK_CODE, timestamp, sequence);
    }

    public String generateLoanAccountNumber() {
        return "LN" + generate();
    }

    public boolean isValidAccountNumber(String accountNumber) {
        if (accountNumber == null || accountNumber.length() != 16) {
            return false;
        }
        return accountNumber.startsWith(BANK_CODE) && accountNumber.substring(3).matches("\\d+");
    }
}
