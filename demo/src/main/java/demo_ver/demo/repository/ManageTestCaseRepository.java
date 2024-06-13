package demo_ver.demo.repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import demo_ver.demo.model.TestCase;

@Repository
public interface ManageTestCaseRepository extends CrudRepository<TestCase, Integer> {
    // Custom query methods (if needed) can be defined here
    Optional<TestCase> findById(int testCaseId);

    boolean existsById(int testCaseId);

    boolean existsByTestCaseName(String testCaseName);

    List<TestCase> findByUsername(String username);
    
}
