package demo_ver.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import demo_ver.demo.model.TestPlan;
import demo_ver.demo.service.TestPlanService;

import java.util.List;
import java.util.Optional;

@Controller
public class TestPlanController {
    
    private TestPlanService testPlanService = new TestPlanService();
    
    // Create a test plan
    public TestPlan createTestPlan(String name, String description, boolean isActive) {
        return testPlanService.createTestPlan(name, description, isActive);
    }

    // View the list of test plans
    @GetMapping("/viewTestPlans")
    public String viewTestPlans() {
        // return testPlanService.viewTestPlans();
        return "viewTestPlans";
    }

    // Update a test plan
    public TestPlan updateTestPlan(Long id, String name, String description, boolean isActive) {
        return testPlanService.updateTestPlan(id, name, description, isActive);
    }

    // Delete a test plan
    public void deleteTestPlan(Long id) {
        testPlanService.deleteTestPlan(id);
    }

        // Handle the "Create Test Plan" page
    @GetMapping("/createTestPlan")
    public String createTestPlan() {
        return "createTestPlan"; // Refers to createTestPlan.html
    }

}
