package demo_ver.demo.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import demo_ver.demo.model.TestCase;
import demo_ver.demo.model.TestCaseUserReason;

@Repository
public interface TestCaseUserReasonRepository extends CrudRepository<TestCaseUserReason, Integer> {
    List<TestCaseUserReason> findByTestCase(TestCase testCase);
}