package demo_ver.demo.service;

import org.springframework.stereotype.Service;
import demo_ver.demo.model.TestPlan;
import demo_ver.demo.model.TestSuite;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TestPlanService {

    private List<TestPlan> testPlans = new ArrayList<>();
    private Map<String, List<TestSuite>> testPlanToSuitesMap = new HashMap<>();

    // Create a test plan
    public TestPlan createTestPlan(String name, String description, String activeStatus, String publicStatus) {
        String id = UUID.randomUUID().toString(); // Generate unique ID
        String isActive = (activeStatus != null && !activeStatus.isEmpty()) ? activeStatus : "false";
        String isPublic = (publicStatus != null && !publicStatus.isEmpty()) ? publicStatus : "false";

        TestPlan testPlan = new TestPlan(id, name, description, isActive, isPublic);
        testPlans.add(testPlan);
        testPlanToSuitesMap.put(id, new ArrayList<>()); // Initialize suite mapping for the test plan
        return testPlan;
    }

    // View all test plans
    public List<TestPlan> viewTestPlans() {
        return testPlans;
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
            testPlanToSuitesMap.remove(id); // Remove the mapping as well
            return true;
        } else {
            return false;
        }
    }

    // View a test plan by ID
    public TestPlan viewTestPlanById(String id) {
        return testPlans.stream()
                .filter(plan -> plan.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Test plan not found with ID: " + id));
    }

    // Retrieve test suites assigned to a specific test plan
    public List<TestSuite> getTestSuitesByTestPlan(String testPlanId) {
        if (testPlanToSuitesMap.containsKey(testPlanId)) {
            return testPlanToSuitesMap.get(testPlanId);
        } else {
            throw new NoSuchElementException("Test plan not found with ID: " + testPlanId);
        }
    }

    // Assign a test suite to a test plan
    public void assignTestSuiteToTestPlan(String testPlanId, String testSuiteId) {
        if (testPlanToSuitesMap.containsKey(testPlanId)) {
            TestSuite testSuite = new TestSuite(null, testSuiteId, "Sample Test Suite"); // Replace with actual lookup
            testPlanToSuitesMap.get(testPlanId).add(testSuite);
        } else {
            throw new NoSuchElementException("Test plan not found with ID: " + testPlanId);
        }
    }

    // Filter test plans by search and active status
    public List<TestPlan> filterTestPlans(String search, Boolean isActive) {
        return testPlans.stream()
                .filter(testPlan -> (search == null || testPlan.getName().toLowerCase().contains(search.toLowerCase()))
                        &&
                        (isActive == null || testPlan.getIsActive().equals(isActive.toString())))
                .collect(Collectors.toList());
    }
}
