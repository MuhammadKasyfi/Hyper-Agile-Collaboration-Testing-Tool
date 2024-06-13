package demo_ver.demo.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import demo_ver.demo.model.TestCase;
import demo_ver.demo.model.TestCaseUserStatus;

@Repository
public interface TestCaseUserStatusRepository extends CrudRepository<TestCaseUserStatus, Integer> {
    List<TestCaseUserStatus> findByTestCase(TestCase testCase);
}
