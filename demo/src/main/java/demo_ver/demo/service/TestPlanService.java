package demo_ver.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import demo_ver.demo.model.TestPlan;
//import demo_ver.demo.repository.TestPlanRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import java.util.ArrayList;


public class TestPlanService {

    private List<TestPlan> testPlans = new ArrayList<>();

    // Create a test plan
    public TestPlan createTestPlan(String name, String description, boolean isActive) {
        Long id = (long) (testPlans.size() + 1); // Generate an ID
        TestPlan testPlan = new TestPlan(id, name, description, isActive);
        testPlans.add(testPlan);
        return testPlan;
    }

    // View all test plans
    public List<TestPlan> viewTestPlans() {
        return testPlans;
    }

    // Update a test plan by ID
    public TestPlan updateTestPlan(Long id, String name, String description, boolean isActive) {
        Optional<TestPlan> testPlanOptional = testPlans.stream()
                .filter(plan -> plan.getId().equals(id))
                .findFirst();

        if (testPlanOptional.isPresent()) {
            TestPlan testPlan = testPlanOptional.get();
            testPlan.setName(name);
            testPlan.setDescription(description);
            testPlan.setActive(isActive);
            return testPlan;
        } else {
            throw new NoSuchElementException("Test plan not found with ID: " + id);
        }
    }

    // Delete a test plan by ID
    public void deleteTestPlan(Long id) {
        testPlans.removeIf(plan -> plan.getId().equals(id));
    }
}
