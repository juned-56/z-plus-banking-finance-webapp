package com.app.bank.web.security;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


public class JwtAuthenticationFilter extends OncePerRequestFilter{

	private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);
	private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    
    private final JwtTokenProvider tokenProvider;
    private final UserDetailsService userDetailsService;
    
    public JwtAuthenticationFilter(JwtTokenProvider tokenProvider, UserDetailsService userDetailsService) {
        this.tokenProvider = tokenProvider;
        this.userDetailsService = userDetailsService;
    }

//    @Override
//    protected void doFilterInternal(HttpServletRequest request, 
//                                    HttpServletResponse response, 
//                                    FilterChain filterChain) throws ServletException, IOException {
//    	
//    	
//    	//updated code
//    	String path = request.getRequestURI();
//
//        // Skip authentication for register and login
//        if (path.startsWith("/api/v1/auth/register") || path.startsWith("/api/v1/auth/login")) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//        
//        try {
//            String jwt = getJwtFromRequest(request);
//            
//            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
//                String username = tokenProvider.getUsernameFromJWT(jwt);
//                
//                logger.debug("JWT Token validated for user: {}", username);
//                
//                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//                
//                if (userDetails != null && userDetails.isEnabled() && userDetails.isAccountNonLocked()) {
//                    UsernamePasswordAuthenticationToken authentication = 
//                        new UsernamePasswordAuthenticationToken(
//                            userDetails, 
//                            null, 
//                            userDetails.getAuthorities()
//                        );
//                    
//                    authentication.setDetails(
//                        new WebAuthenticationDetailsSource().buildDetails(request)
//                    );
//                    
//                    SecurityContextHolder.getContext().setAuthentication(authentication);
//                    
//                    // Add user info to request for logging/monitoring
//                    request.setAttribute("username", username);
//                    request.setAttribute("userId", getUserIdFromJWT(jwt));
//                } else {
//                    logger.warn("User account is disabled or locked: {}", username);
//                }
//            } else if (StringUtils.hasText(jwt)) {
//                logger.warn("Invalid JWT token provided");
//            }
//            
//        } catch (ExpiredJwtException ex) {
//            logger.error("JWT token has expired: {}", ex.getMessage());
//            request.setAttribute("expired", ex.getMessage());
//        } catch (UnsupportedJwtException ex) {
//        	logger.error("Unsupported JWT token: {}", ex.getMessage());
//        } catch (MalformedJwtException ex) {
//        	logger.error("Malformed JWT token: {}", ex.getMessage());
//        } catch (SignatureException ex) {
//        	logger.error("Invalid JWT signature: {}", ex.getMessage());
//        } catch (IllegalArgumentException ex) {
//        	logger.error("JWT claims string is empty: {}", ex.getMessage());
//        } catch (Exception ex) {
//        	logger.error("Could not set user authentication in security context: {}", ex.getMessage());
//        }
//
//        filterChain.doFilter(request, response);
//    }
    
    
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            String token = header.substring(7);

            if (tokenProvider.validateToken(token)) {
                String username = tokenProvider.getUsernameFromJWT(token);

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authentication.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }

    
   
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        
        // Also check for token in query parameter (for websocket connections)
        if (!StringUtils.hasText(bearerToken)) {
            bearerToken = request.getParameter("token");
        }
        
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }
        
        return null;
    }
    
    private Long getUserIdFromJWT(String token) {
        try {
            // Extract user ID from token claims
            return tokenProvider.getUserIdFromJWT(token);
        } catch (Exception e) {
        	logger.warn("Could not extract user ID from JWT: {}", e.getMessage());
            return null;
        }
    }
    
//    @Override
//    protected boolean shouldNotFilter(HttpServletRequest request) {
//        String path = request.getRequestURI();
//        
//        // Skip JWT filter for public endpoints
//        return path.startsWith("/api/v1/auth/") ||
//               path.startsWith("/api/v1/public/") ||
//               path.contains("swagger") ||
//               path.contains("api-docs") ||
//               path.contains("webjars") ||
//               path.contains("configuration") ||
//               path.equals("/favicon.ico") ||
//               path.equals("/error");
//    }
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        
        return path.equals("/api/v1/auth/login") ||
               path.equals("/api/v1/auth/register") ||
               path.startsWith("/api/v1/public/") ||
               path.contains("swagger") ||
               path.contains("api-docs") ||
               path.equals("/error");
    }

}
