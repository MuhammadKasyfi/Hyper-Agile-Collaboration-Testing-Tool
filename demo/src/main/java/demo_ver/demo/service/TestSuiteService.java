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
    private List<TestPlan> testPlans = new ArrayList<>(); // Simulated TestPlan storage

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
    public TestSuite updateTestSuite(String id, String name, String description, String status, String importance) {
        Optional<TestSuite> testSuiteOptional = testSuites.stream()
                .filter(suite -> suite.getId().equals(id))
                .findFirst();

        if (testSuiteOptional.isPresent()) {
            TestSuite testSuite = testSuiteOptional.get();
            testSuite.setName(name);
            testSuite.setDescription(description);
            testSuite.setStatus(status);
            testSuite.setImportance(importance);
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
            // Simulating fetching test plans from in-memory storage
            for (String testPlanId : testPlanIds) {
                TestPlan testPlan = findTestPlanById(testPlanId); // Fetch test plan from the in-memory list
                assignedTestPlans.add(testPlan);
            }
            testSuite.setAssignedTestPlans(assignedTestPlans);
        } else {
            throw new NoSuchElementException("Test suite not found with ID: " + testSuiteId);
        }
    }

    // Simulated method to find Test Plan by ID (from in-memory list)
    private TestPlan findTestPlanById(String testPlanId) {
        Optional<TestPlan> testPlan = testPlans.stream()
                .filter(plan -> plan.getId().equals(testPlanId))
                .findFirst();

        if (testPlan.isPresent()) {
            return testPlan.get();
        } else {
            throw new NoSuchElementException("Test plan not found with ID: " + testPlanId);
        }
    }

    // Save or update a test suite (simulating persistence in-memory)
    public void save(TestSuite testSuite) {
        Optional<TestSuite> existingTestSuite = findById(testSuite.getId());
        if (existingTestSuite.isPresent()) {
            // If the test suite exists, update it
            TestSuite existing = existingTestSuite.get();
            existing.setName(testSuite.getName());
            existing.setDescription(testSuite.getDescription());
            existing.setStatus(testSuite.getStatus());
            existing.setImportance(testSuite.getImportance());
        } else {
            // Otherwise, add the new test suite
            testSuites.add(testSuite);
        }
    }

    // Get all test suites
    public List<TestSuite> getAllTestSuites() {
        return testSuites;
    }

    // Find Test Suite by ID
    public Optional<TestSuite> findById(String testSuiteId) {
        return testSuites.stream()
                .filter(suite -> suite.getId().equals(testSuiteId))
                .findFirst();
    }

    // Simulate creating and adding test plans for demonstration
    public void createTestPlan(String id, String name, String description) {
        TestPlan testPlan = new TestPlan(id, name, description, description, description);
        testPlans.add(testPlan);
    }

    // Get all test plans (simulated)
    public List<TestPlan> getAllTestPlans() {
        return testPlans;
    }
}
