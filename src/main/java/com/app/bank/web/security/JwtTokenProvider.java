package com.app.bank.web.security;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


@Component
public class JwtTokenProvider {

	private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);
	@Value("${app.jwt.secret}")
    private String jwtSecret;
    
    @Value("${app.jwt.expiration-ms}")
    private int jwtExpirationInMs;
    
    //new method added
    private byte[] getSigningKey() {
        return jwtSecret.getBytes(StandardCharsets.UTF_8);
    }
    
    public String generateToken(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        
        Map<String, Object> claims = new HashMap<>();
        
        // Add user details to claims
        claims.put("username", userDetails.getUsername());
        
        // Add authorities
        String authorities = userDetails.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","));
        claims.put("authorities", authorities);
        
        // Add token creation timestamp
        claims.put("created", new Date());
        
        return generateToken(claims, userDetails.getUsername());
    }
    
    public String generateToken(Map<String, Object> claims, String subject) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);
        
        return Jwts.builder()
            .setClaims(claims)
            .setSubject(subject)
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(SignatureAlgorithm.HS512, getSigningKey())
            //.signWith(getSigningKey(), SignatureAlgorithm.HS512)
            .compact();
    }
    
    public String getUsernameFromJWT(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }
    
    public Long getUserIdFromJWT(String token) {
//        Claims claims = getAllClaimsFromToken(token);
//        return claims.get("userId", Long.class);
    	return null;
    }
    
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }
    
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }
    
//    private Claims getAllClaimsFromToken(String token) {
//        return Jwts.parser()
//            .setSigningKey(getSigningKey())
//            .build()
//            .parseClaimsJws(token)
//            .getBody();
//    }
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
            .setSigningKey(getSigningKey())
            .parseClaimsJws(token)
            .getBody();
    }
    
//    public Boolean validateToken(String token) {
//        try {
//            Jwts.parser()
//                .setSigningKey(getSigningKey())
//                .build()
//                .parseClaimsJws(token);
//            return true;
//        } catch (SignatureException ex) {
//            logger.error("Invalid JWT signature: {}", ex.getMessage());
//        } catch (MalformedJwtException ex) {
//        	logger.error("Invalid JWT token: {}", ex.getMessage());
//        } catch (ExpiredJwtException ex) {
//        	logger.error("Expired JWT token: {}", ex.getMessage());
//        } catch (UnsupportedJwtException ex) {
//        	logger.error("Unsupported JWT token: {}", ex.getMessage());
//        } catch (IllegalArgumentException ex) {
//        	logger.error("JWT claims string is empty: {}", ex.getMessage());
//        }
//        return false;
//    }
    
    
    public Boolean validateToken(String token) {
        try {
            Jwts.parser()
                .setSigningKey(getSigningKey())
                .parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
    
    public Boolean isTokenExpired(String token) {
        Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }
    
    public long getRemainingValidity(String token) {
        Date expiration = getExpirationDateFromToken(token);
        Date now = new Date();
        return expiration.getTime() - now.getTime();
    }
    
//    public String refreshToken(String token) {
//        try {
//            Claims claims = getAllClaimsFromToken(token);
//            ((ClaimsMutator<JwtBuilder>) claims).setIssuedAt(new Date());
//            ((ClaimsMutator<JwtBuilder>) claims).setExpiration(new Date(System.currentTimeMillis() + jwtExpirationInMs));
//            
//            return Jwts.builder()
//                .setClaims(claims)
//                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
//                .compact();
//        } catch (Exception ex) {
//            logger.error("Could not refresh token: {}", ex.getMessage());
//            return null;
//        }
//    }
    
    public String refreshToken(String token) {
        try {
            Claims claims = getAllClaimsFromToken(token);

            return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationInMs))
                .signWith(SignatureAlgorithm.HS512, getSigningKey())
                .compact();

        } catch (Exception ex) {
            logger.error("Could not refresh token: {}", ex.getMessage());
            return null;
        }
    }

    
    public String generateTokenForUser(String username, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", username);
        claims.put("role", role);
        claims.put("created", new Date());
        
        return generateToken(claims, username);
    }
    
    public Map<String, Object> getTokenInfo(String token) {
        Claims claims = getAllClaimsFromToken(token);
        
        Map<String, Object> tokenInfo = new HashMap<>();
        tokenInfo.put("username", claims.getSubject());
        tokenInfo.put("authorities", claims.get("authorities", String.class));
        tokenInfo.put("issuedAt", claims.getIssuedAt());
        tokenInfo.put("expiration", claims.getExpiration());
        tokenInfo.put("remainingValidity", getRemainingValidity(token));
        
        return tokenInfo;
    }
}