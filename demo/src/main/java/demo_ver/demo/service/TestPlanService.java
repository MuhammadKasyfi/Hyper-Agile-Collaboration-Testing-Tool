package demo_ver.demo.service;

import org.springframework.stereotype.Service;
import demo_ver.demo.model.TestPlan;
import demo_ver.demo.model.TestSuite;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TestPlanService {

    private List<TestPlan> testPlans = new ArrayList<>();
    private List<TestSuite> testSuites = new ArrayList<>();

    // Constructor to initialize some sample Test Suites (if needed)
    public TestPlanService() {
        // Sample Test Suites
        //testSuites.add(new TestSuite("1", "Test Suite 1", "Description of Test Suite 1"));
        //testSuites.add(new TestSuite("2", "Test Suite 2", "Description of Test Suite 2"));
    }

    // Create a test plan
    public TestPlan createTestPlan(String name, String description, String activeStatus, String publicStatus) {
        String id = UUID.randomUUID().toString(); // Generate unique ID
        String isActive = (activeStatus != null && !activeStatus.isEmpty()) ? activeStatus : "false";
        String isPublic = (publicStatus != null && !publicStatus.isEmpty()) ? publicStatus : "false";

        TestPlan testPlan = new TestPlan(id, name, description, isActive, isPublic);
        testPlans.add(testPlan); // Add to the list of test plans
        return testPlan;
    }

    // View all test plans
    public List<TestPlan> viewTestPlans() {
        return testPlans;
    }

    // View a test plan by ID
    public TestPlan viewTestPlanById(String id) {
        return testPlans.stream()
                .filter(plan -> plan.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Test plan not found with ID: " + id));
    }

    // Update a test plan by ID
    public TestPlan updateTestPlan(String id, String name, String description, String activeStatus,
            String publicStatus) {
        Optional<TestPlan> testPlanOptional = testPlans.stream()
                .filter(plan -> plan.getId().equals(id))
                .findFirst();

        if (testPlanOptional.isPresent()) {
            TestPlan testPlan = testPlanOptional.get();
            testPlan.setName(name);
            testPlan.setDescription(description);
            String isActive = (activeStatus != null && !activeStatus.isEmpty()) ? activeStatus : "false";
            String isPublic = (publicStatus != null && !publicStatus.isEmpty()) ? publicStatus : "false";

            testPlan.setIsActive(isActive);
            testPlan.setIsPublic(isPublic);

            return testPlan;
        } else {
            throw new NoSuchElementException("Test plan not found with ID: " + id);
        }
    }

    // Delete a test plan by ID
    public boolean deleteTestPlan(String id) {
        Optional<TestPlan> testPlanOptional = testPlans.stream()
                .filter(plan -> plan.getId().equals(id))
                .findFirst();

        if (testPlanOptional.isPresent()) {
            testPlans.remove(testPlanOptional.get());
            return true;
        } else {
            return false;
        }
    }

    // Assign a Test Suite to a Test Plan
    public void assignTestSuiteToTestPlan(TestPlan testPlan, TestSuite testSuite) {
        testPlan.addTestSuite(testSuite); // Calls the implemented method
    }

    // Retrieve test suites assigned to a specific test plan
    public List<TestSuite> getTestSuitesByTestPlan(String testPlanId) {
        return testPlans.stream()
                .filter(plan -> plan.getId().equals(testPlanId))
                .findFirst()
                .map(TestPlan::getTestSuites)
                .orElseThrow(() -> new NoSuchElementException("Test plan not found with ID: " + testPlanId));
    }

    // Filter test plans by search and active status
    public List<TestPlan> filterTestPlans(String search, Boolean isActive) {
        return testPlans.stream()
                .filter(testPlan -> (search == null || testPlan.getName().toLowerCase().contains(search.toLowerCase()))
                        &&
                        (isActive == null || testPlan.getIsActive().equals(isActive.toString())))
                .collect(Collectors.toList());
    }

    // Retrieve the test plans assigned to a specific test suite by the test suite's
    // ID
    public List<TestPlan> getAssignedTestPlansByTestSuiteId(String testSuiteId) {
        return testPlans.stream()
                .filter(testPlan -> testPlan.getTestSuites() != null &&
                        testPlan.getTestSuites().stream()
                                .anyMatch(testSuite -> testSuite.getId().equals(testSuiteId)))
                .collect(Collectors.toList());
    }

    // Find a Test Suite by its ID
    public Optional<TestSuite> findTestSuiteById(String id) {
        return testSuites.stream()
                .filter(testSuite -> testSuite.getId().equals(id))
                .findFirst();
    }

    public TestPlan getTestPlanById(String testPlanId) {
        // Assuming testPlans is a list of all available TestPlans
        return testPlans.stream()
                .filter(testPlan -> testPlan.getId().equals(testPlanId))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("TestPlan not found with ID: " + testPlanId));
    }

}
