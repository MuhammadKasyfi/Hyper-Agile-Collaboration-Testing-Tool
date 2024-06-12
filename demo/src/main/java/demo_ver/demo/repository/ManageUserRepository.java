package demo_ver.demo.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import demo_ver.demo.model.ManageUser;

@Repository
public interface ManageUserRepository extends CrudRepository<ManageUser, Integer> {
    // Custom query methods (if needed) can be defined here
    
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByUsernameOrEmail(String username, String email);
    boolean existsByUsernameAndUserIDNot(String username, int userID);
    boolean existsByEmailAndUserIDNot(String email, int userID);
    Optional<ManageUser> findByUsername(String username);
    Optional<ManageUser> findByEmail(String email);
    Optional<ManageUser> findByResetToken(String resetToken);
}