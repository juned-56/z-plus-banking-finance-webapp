package com.app.bank.web.repository;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.app.bank.web.enums.UserStatus;
import com.app.bank.web.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	
	Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByPhone(String phone);
    Optional<User> findByPanNumber(String panNumber);
    Optional<User> findByAadharNumber(String aadharNumber);
    //boolean existByUserName(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    boolean existsByPanNumber(String panNumber);
    boolean existsByAadharNumber(String aadharNumber);
    long countByStatus(UserStatus status);

}
