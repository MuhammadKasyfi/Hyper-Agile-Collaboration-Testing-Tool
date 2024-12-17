package demo_ver.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.stereotype.Service;

import demo_ver.demo.model.TestPlan;
import demo_ver.demo.model.TestSuite;

@Service
public class TestSuiteService {

    private List<TestSuite> testSuites = new ArrayList<>();

    // Create a test suite
    public TestSuite createTestSuite(String name, String description) {
        String id = String.valueOf(testSuites.size() + 1); // Simple String ID generation
        TestSuite testSuite = new TestSuite(id, name, description);
        testSuites.add(testSuite);
        return testSuite;
    }

    // View all test suites
    public List<TestSuite> viewTestSuites() {
        return testSuites;
    }

    // Update a test suite by ID
    public TestSuite updateTestSuite(String id, String name, String description, String status, String importance,
            String testCases) {
        Optional<TestSuite> testSuiteOptional = testSuites.stream()
                .filter(suite -> suite.getId().equals(id))
                .findFirst();

        if (testSuiteOptional.isPresent()) {
            TestSuite testSuite = testSuiteOptional.get();
            testSuite.setName(name);
            testSuite.setDescription(description);
            testSuite.setStatus(status);
            testSuite.setImportance(importance);
            // testSuite.setTestCases(testCases); // Assuming testCases is stored as a String
            return testSuite;
        } else {
            throw new NoSuchElementException("Test suite not found with ID: " + id);
        }
    }

    // Delete a test suite by ID
    public boolean deleteTestSuite(String id) {
        Optional<TestSuite> testSuiteOptional = testSuites.stream()
                .filter(suite -> suite.getId().equals(id))
                .findFirst();

        if (testSuiteOptional.isPresent()) {
            testSuites.remove(testSuiteOptional.get());
            return true;
        } else {
            return false; // No test suite found with the given ID
        }
    }

    // View a test suite by ID
    public TestSuite viewTestSuiteById(String id) {
        Optional<TestSuite> testSuite = testSuites.stream()
                .filter(suite -> suite.getId().equals(id))
                .findFirst();

        if (testSuite.isPresent()) {
            return testSuite.get();
        } else {
            throw new NoSuchElementException("Test suite not found with ID: " + id);
        }
    }

    // Assign users to a test suite
    public void assignUsersToTestSuite(String testSuiteId, List<String> userIds) {
        Optional<TestSuite> testSuiteOptional = testSuites.stream()
                .filter(suite -> suite.getId().equals(testSuiteId))
                .findFirst();

        if (testSuiteOptional.isPresent()) {
            TestSuite testSuite = testSuiteOptional.get();
            testSuite.setAssignedUserIds(userIds); // Assuming TestSuite has a field for assigned user IDs
        } else {
            throw new NoSuchElementException("Test suite not found with ID: " + testSuiteId);
        }
    }

    // Assign test plans to a test suite
    public void assignTestPlansToTestSuite(String testSuiteId, List<String> testPlanIds) {
        Optional<TestSuite> testSuiteOptional = testSuites.stream()
                .filter(suite -> suite.getId().equals(testSuiteId))
                .findFirst();

        if (testSuiteOptional.isPresent()) {
            TestSuite testSuite = testSuiteOptional.get();
            List<TestPlan> assignedTestPlans = new ArrayList<>();
            // You need to fetch the test plans by IDs from wherever they are stored (e.g., from a database)
            for (String testPlanId : testPlanIds) {
                TestPlan testPlan = findTestPlanById(testPlanId); // Assume this method is implemented
                assignedTestPlans.add(testPlan);
            }
            testSuite.setAssignedTestPlans(assignedTestPlans);
        } else {
            throw new NoSuchElementException("Test suite not found with ID: " + testSuiteId);
        }
    }

    // Find Test Suite by ID
    public Optional<TestSuite> findById(String testSuiteId) {
        return testSuites.stream()
                .filter(suite -> suite.getId().equals(testSuiteId))
                .findFirst();
    }

    // Find a Test Plan by ID (helper method)
    private TestPlan findTestPlanById(String testPlanId) {
        // Assuming you have a collection or service for TestPlans
        // You would typically fetch the test plan from a repository or service, not from a list.
        // For simplicity, here it’s assumed that you have a `TestPlanService` that can provide this functionality.
        return new TestPlan(testPlanId, "Test Plan Name", testPlanId, testPlanId, testPlanId); // Dummy implementation for demonstration
    }

    // Save a test suite (simulating persistence in-memory)
    public void save(TestSuite testSuite) {
        Optional<TestSuite> existingTestSuite = findById(testSuite.getId());
        if (existingTestSuite.isPresent()) {
            // If the test suite exists, update it
            TestSuite existing = existingTestSuite.get();
            existing.setName(testSuite.getName());
            existing.setDescription(testSuite.getDescription());
            existing.setStatus(testSuite.getStatus());
            existing.setImportance(testSuite.getImportance());
            // existing.setAssignedUserIds(testSuite.getAssignedUserIds()); // If applicable
        } else {
            // Otherwise, add the new test suite
            testSuites.add(testSuite);
        }
    }

    // Get all test suites
    public List<TestSuite> getAllTestSuites() {
        return testSuites;
    }
}
