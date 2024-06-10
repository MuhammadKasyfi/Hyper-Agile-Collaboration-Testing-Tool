package demo_ver.demo;

import org.springframework.data.repository.CrudRepository;
import demo_ver.demo.model.ManageUser;
import demo_ver.demo.model.TestCase;

import java.util.List;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface TestCaseRepository  extends CrudRepository<TestCase, Long> {
    // generate some custom queries
    // public List<ManageUser> findByUserID(int userID);
    // public List<ManageUser> findByEmail(String email);
    public List<TestCase> findByidtest_cases(Long idtest_cases);
    public List<TestCase> findByProjectId(String projectId);
    public List<TestCase> findByStatus(String status);


    // queries for User model
    // public List<TestCase> findByUserID(Integer userID);
}