package demo_ver.demo.service;

import org.springframework.stereotype.Service;
import demo_ver.demo.model.TestPlan;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.ArrayList;
import java.util.UUID;

@Service
public class TestPlanService {

    private List<TestPlan> testPlans = new ArrayList<>();

    // Create a test plan
    public TestPlan createTestPlan(String name, String description, String activeStatus, String publicStatus) {
        // Use UUID for unique string ID generation
        String id = UUID.randomUUID().toString(); 
        // Default to "false" if null or empty
        String isActive = (activeStatus != null && !activeStatus.isEmpty()) ? activeStatus : "false";
        String isPublic = (publicStatus != null && !publicStatus.isEmpty()) ? publicStatus : "false";

        TestPlan testPlan = new TestPlan(id, name, description, isActive, isPublic);
        testPlans.add(testPlan);
        return testPlan;
    }

    // View all test plans
    public List<TestPlan> viewTestPlans() {
        return testPlans;
    }

    // Update a test plan by ID
    public TestPlan updateTestPlan(String id, String name, String description, String activeStatus, String publicStatus) {
        Optional<TestPlan> testPlanOptional = testPlans.stream()
                .filter(plan -> plan.getId().equals(id))
                .findFirst();

        if (testPlanOptional.isPresent()) {
            TestPlan testPlan = testPlanOptional.get();
            testPlan.setName(name);
            testPlan.setDescription(description);

            // Update active and public status, default to "false" if null or empty
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
            return false; // No test plan found with the given ID
        }
    }

    // View a test plan by ID
    public TestPlan viewTestPlanById(String id) {
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
