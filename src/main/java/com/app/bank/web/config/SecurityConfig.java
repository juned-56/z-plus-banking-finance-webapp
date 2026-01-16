package com.app.bank.web.config;
import java.util.Arrays;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import com.app.bank.web.security.JwtAuthenticationFilter;
import com.app.bank.web.security.JwtTokenProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

	private final UserDetailsService userDetailsService;
	private final JwtAuthenticationEntryPoint unauthorizedHandler;
	private final JwtTokenProvider tokenProvider;
	
	public SecurityConfig(UserDetailsService userDetailsService, JwtAuthenticationEntryPoint unauthorizedHandler,
					JwtTokenProvider tokenProvider) {
		this.tokenProvider = tokenProvider;
		this.unauthorizedHandler = unauthorizedHandler;
		this.userDetailsService = userDetailsService;
	}
	
	@Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(tokenProvider, userDetailsService);
    }

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12); // Higher strength for banking
    }
    
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//            .cors().and()
//            .csrf().disable()
//            .exceptionHandling()
//                .authenticationEntryPoint(unauthorizedHandler)
//                .and()
//            .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//            .authorizeHttpRequests()
//            	.requestMatchers("/api/v1/auth/register").permitAll()
//            	.requestMatchers("/api/v1/auth/login").permitAll()
//                .requestMatchers("/api/v1/auth/**").permitAll()
//                .requestMatchers("/api/v1/public/**").permitAll()
//                .requestMatchers("/api/v1/test/**").permitAll()
//                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", 
//                           "/webjars/**", "/configuration/**").permitAll()
//                .requestMatchers("/actuator/health", "/actuator/info").permitAll()
//                .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
//                .requestMatchers("/api/v1/employee/**").hasAnyRole("EMPLOYEE", "ADMIN", "BANK_MANAGER")
//                .requestMatchers("/api/v1/manager/**").hasAnyRole("BANK_MANAGER", "ADMIN")
//                .requestMatchers("/api/v1/customer/**").hasAnyRole("CUSTOMER", "EMPLOYEE", "ADMIN")
//                .anyRequest().authenticated()
//                .and()
//            .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
//            .headers()
//                .frameOptions().sameOrigin()
//                .contentSecurityPolicy("script-src 'self'; object-src 'none';")
//                .and()
//                .xssProtection()
//                .and()
//                .httpStrictTransportSecurity()
//                .includeSubDomains(true)
//                .maxAgeInSeconds(31536000);
//
//        // register the authentication provider
//        http.authenticationProvider(authenticationProvider());
//
//        return http.build();
//    }
    
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors().and()
            .csrf().disable()
            .exceptionHandling()
                .authenticationEntryPoint(unauthorizedHandler)
                .and()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
            .authorizeHttpRequests()
                // Public endpoints
                .requestMatchers("/api/v1/auth/register", "/api/v1/auth/login").permitAll()
                .requestMatchers("/error").permitAll()
                .requestMatchers("/api/v1/auth/create-account").hasAnyRole("ADMIN", "BANK_EMPLOYEE", "BANK_MANAGER")
                .requestMatchers("/api/v1/auth/*/approve-loan").hasAnyRole("ADMIN", "BANK_MANAGER")
                .requestMatchers("/api/v1/auth/all-loans").hasAnyRole("ADMIN", "BANK_EMPLOYEE", "BANK_MANAGER")
                .requestMatchers("/api/v1/public/**").permitAll()
                .requestMatchers("/api/v1/swagger-ui/**", "/api/v1/v3/api-docs/**").permitAll()
                .requestMatchers("/api/v1/actuator/health", "/api/v1/actuator/info").permitAll()
                // Role-based endpoints
                .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/v1/employee/**").hasAnyRole("BANK_EMPLOYEE", "ADMIN", "BANK_MANAGER")
                .requestMatchers("/api/v1/manager/**").hasAnyRole("BANK_MANAGER", "ADMIN")
                .requestMatchers("/api/v1/customer/**").hasAnyRole("CUSTOMER", "BANK_EMPLOYEE", "ADMIN")
                .anyRequest().authenticated()
                .and()
            .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
            .authenticationProvider(authenticationProvider());

        return http.build();
    }


    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Allowed origins
        configuration.setAllowedOrigins(Arrays.asList(
            "http://localhost:3000",    // React dev server
            "http://localhost:4200",    // Angular dev server
            "http://localhost:8080",    // Vue dev server
            "https://bank.zplus.com"    // Production frontend
        ));
        
        // Allowed methods
        configuration.setAllowedMethods(Arrays.asList(
            "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS", "HEAD"
        ));
        
        // Allowed headers
        configuration.setAllowedHeaders(Arrays.asList(
            "Authorization",
            "Content-Type",
            "Accept",
            "X-Requested-With",
            "Cache-Control",
            "X-API-Key",
            "X-CSRF-Token"
        ));
        
        // Exposed headers
        configuration.setExposedHeaders(Arrays.asList(
            "Authorization",
            "X-Total-Count",
            "X-Rate-Limit-Remaining",
            "X-Rate-Limit-Reset"
        ));
        
        // Allow credentials
        configuration.setAllowCredentials(true);
        
        // Max age
        configuration.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }
}