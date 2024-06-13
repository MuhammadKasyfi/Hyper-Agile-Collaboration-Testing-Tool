package demo_ver.demo.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import demo_ver.demo.model.TestCase;
import demo_ver.demo.model.TestCaseUserId;

@Repository
public interface TestCaseUserIdRepository extends CrudRepository<TestCaseUserId, Integer> {
    List<TestCaseUserId> findByTestCase(TestCase testCase);
}