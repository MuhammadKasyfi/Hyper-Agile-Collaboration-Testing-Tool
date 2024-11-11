package demo_ver.demo.service;

import org.springframework.stereotype.Service;
import demo_ver.demo.model.TestPlan;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.ArrayList;

@Service
public class TestPlanService {

    private List<TestPlan> testPlans = new ArrayList<>();

    // Create a test plan
    public TestPlan createTestPlan(String name, String description, boolean isActive, boolean isPublic) {
        Long id = (long) (testPlans.size() + 1); // Simple ID generation, consider alternatives for production
        TestPlan testPlan = new TestPlan(id, name, description, isActive, isPublic);
        testPlans.add(testPlan);
        return testPlan;
    }

    // View all test plans
    public List<TestPlan> viewTestPlans() {
        return testPlans;
    }

    // Update a test plan by ID
    public TestPlan updateTestPlan(Long id, String name, String description, boolean isActive, boolean isPublic) {
        Optional<TestPlan> testPlanOptional = testPlans.stream()
                .filter(plan -> plan.getId().equals(id))
                .findFirst();

        if (testPlanOptional.isPresent()) {
            TestPlan testPlan = testPlanOptional.get();
            testPlan.setName(name);
            testPlan.setDescription(description);
            testPlan.setActive(isActive);
            testPlan.setIsPublic(isPublic); // Properly set the "isPublic" field
            return testPlan;
        } else {
            throw new NoSuchElementException("Test plan not found with ID: " + id);
        }
    }

    // Delete a test plan by ID
    public boolean deleteTestPlan(Long id) {
        Optional<TestPlan> testPlanOptional = testPlans.stream()
                .filter(plan -> plan.getId().equals(id))
                .findFirst();
    
        if (testPlanOptional.isPresent()) {
            testPlans.remove(testPlanOptional.get());
            return true;
        } else {
            return false; // No test plan found with the given ID
        }
    }
    

    // View a test plan by ID
    public TestPlan viewTestPlanById(Long id) {
        Optional<TestPlan> testPlan = testPlans.stream()
                .filter(plan -> plan.getId().equals(id))
                .findFirst();

        if (testPlan.isPresent()) {
            return testPlan.get();
        } else {
            throw new NoSuchElementException("Test plan not found with ID: " + id);
        }
    }
}
