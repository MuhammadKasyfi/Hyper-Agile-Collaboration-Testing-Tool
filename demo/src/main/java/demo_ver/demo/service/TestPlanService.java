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

    // Create a test plan
    public TestPlan createTestPlan(String name, String description, String activeStatus, String publicStatus,
            List<String> testSuiteIds) {
        String id = UUID.randomUUID().toString(); // Generate unique ID
        String isActive = (activeStatus != null && !activeStatus.isEmpty()) ? activeStatus : "false";
        String isPublic = (publicStatus != null && !publicStatus.isEmpty()) ? publicStatus : "false";

        TestPlan testPlan = new TestPlan(id, name, description, isActive, isPublic);
        testPlans.add(testPlan); // Add to the list of test plans

        // Assign test suites if provided
        if (testSuiteIds != null && !testSuiteIds.isEmpty()) {
            List<TestSuite> selectedTestSuites = getTestSuitesByIds(testSuiteIds);
            testPlan.setTestSuites(selectedTestSuites);
        }

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
            String publicStatus, List<String> assignedTestSuites) {
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

    // Filter test plans by search and active status
    public List<TestPlan> filterTestPlans(String search, Boolean isActive) {
        return testPlans.stream()
                .filter(testPlan -> (search == null || testPlan.getName().toLowerCase().contains(search.toLowerCase()))
                        &&
                        (isActive == null || testPlan.getIsActive().equals(isActive.toString())))
                .collect(Collectors.toList());
    }

    // Get test plan by ID
    public TestPlan getTestPlanById(String testPlanId) {
        return testPlans.stream()
                .filter(testPlan -> testPlan.getId().equals(testPlanId))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("TestPlan not found with ID: " + testPlanId));
    }

    // Get test suites by their IDs
    private List<TestSuite> getTestSuitesByIds(List<String> testSuiteIds) {
        // In a real-world scenario, this method would query a database or service to
        // fetch TestSuite objects
        // For simplicity, we'll mock this by returning new TestSuite objects with the
        // given IDs

        List<TestSuite> allTestSuites = new ArrayList<>();
        for (String id : testSuiteIds) {
            TestSuite testSuite = new TestSuite(id, "Test Suite " + id, id);
            allTestSuites.add(testSuite);
        }
        return allTestSuites;
    }

    // Get assigned test suites for a specific test plan
    public List<TestSuite> getAssignedTestSuitesByTestPlanId(String testPlanId) {
        TestPlan testPlan = getTestPlanById(testPlanId);
        if (testPlan.getTestSuites() == null || testPlan.getTestSuites().isEmpty()) {
            throw new NoSuchElementException("No test suites assigned to this test plan.");
        }
        return testPlan.getTestSuites();
    }

    // Assign test suites to a test plan
    public void assignTestSuitesToTestPlan(String testPlanId, List<String> testSuiteIds,
            List<TestSuite> allTestSuites) {
        TestPlan testPlan = getTestPlanById(testPlanId);
        List<TestSuite> selectedTestSuites = allTestSuites.stream()
                .filter(testSuite -> testSuiteIds.contains(testSuite.getId()))
                .collect(Collectors.toList());
        testPlan.setTestSuites(selectedTestSuites);
    }
}
