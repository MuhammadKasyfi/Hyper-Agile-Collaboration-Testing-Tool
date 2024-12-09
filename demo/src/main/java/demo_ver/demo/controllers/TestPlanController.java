package demo_ver.demo.controllers;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import demo_ver.demo.model.TestPlan;
import demo_ver.demo.model.TestSuite;
import demo_ver.demo.service.TestPlanService;

@Controller
public class TestPlanController {

    @Autowired
    private TestPlanService testPlanService;

    // Utility method to check if a user has a specific role
    private boolean hasRole(Authentication authentication, String role) {
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equalsIgnoreCase("ROLE_" + role));
    }

    // View the list of test plans
    @GetMapping("/viewTestPlans")
    public String viewTestPlans(
            @RequestParam(required = false) String search, // Search parameter (optional)
            @RequestParam(required = false) Boolean isActive, // Filter parameter (optional)
            Model model,
            Authentication authentication) {
        // Determine if the user has the "STAKEHOLDER" role
        boolean isStakeholder = hasRole(authentication, "STAKEHOLDER");

        // Call service to filter test plans based on search and isActive
        List<TestPlan> testPlans = testPlanService.filterTestPlans(search, isActive);

        // Add attributes to the model
        model.addAttribute("testPlans", testPlans);
        model.addAttribute("isStakeholder", isStakeholder);
        model.addAttribute("search", search);
        model.addAttribute("isActive", isActive);

        // Return the view
        return "viewTestPlans";
    }

    // Create Test Plan - GET
    @GetMapping("/createTestPlan")
    public String createTestPlanForm() {
        return "createTestPlan";
    }

    // Create Test Plan - POST
    @PostMapping("/createTestPlan")
    public String createTestPlan(@RequestParam String name,
            @RequestParam String description,
            @RequestParam(required = false) String isActive,
            @RequestParam(required = false) String isPublic,
            RedirectAttributes redirectAttributes) {
        testPlanService.createTestPlan(name, description,
                isActive != null && !isActive.isEmpty() ? isActive : "false",
                isPublic != null && !isPublic.isEmpty() ? isPublic : "false");
        redirectAttributes.addFlashAttribute("success", "Test plan created successfully.");
        return "redirect:/viewTestPlans";
    }

    // Edit Test Plan - GET
    @GetMapping("/editTestPlan")
    public String editTestPlanForm(@RequestParam String id, Model model, RedirectAttributes redirectAttributes) {
        try {
            TestPlan testPlan = testPlanService.viewTestPlanById(id);
            model.addAttribute("testPlan", testPlan);
            return "editTestPlan";
        } catch (NoSuchElementException e) {
            redirectAttributes.addFlashAttribute("error", "Test plan not found.");
            return "redirect:/viewTestPlans";
        }
    }

    // Edit Test Plan - POST
    @PostMapping("/editTestPlan")
    public String updateTestPlan(@RequestParam String id,
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam(required = false) String isActive,
            @RequestParam(required = false) String isPublic,
            RedirectAttributes redirectAttributes) {
        try {
            testPlanService.updateTestPlan(id, name, description,
                    isActive != null && !isActive.isEmpty() ? isActive : "false",
                    isPublic != null && !isPublic.isEmpty() ? isPublic : "false");
            redirectAttributes.addFlashAttribute("success", "Test plan updated successfully.");
        } catch (NoSuchElementException e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update test plan: " + e.getMessage());
        }
        return "redirect:/viewTestPlans";
    }

    // Delete Test Plan
    @PostMapping("/deleteTestPlan")
    public String deleteTestPlan(@RequestParam String id, RedirectAttributes redirectAttributes) {
        if (testPlanService.deleteTestPlan(id)) {
            redirectAttributes.addFlashAttribute("success", "Test plan deleted successfully.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Failed to delete test plan.");
        }
        return "redirect:/viewTestPlans";
    }

    @GetMapping("/viewTestPlanDetails/{id}")
    public String viewTestPlanDetails(@PathVariable String id, Model model, RedirectAttributes redirectAttributes) {
        try {
            TestPlan testPlan = testPlanService.viewTestPlanById(id);
            List<TestSuite> testSuites = testPlanService.getTestSuitesByTestPlan(id);
            model.addAttribute("testPlan", testPlan);
            model.addAttribute("testSuites", testSuites);

            // Return the correct Thymeleaf template
            return "viewTestPlanDetails";

        } catch (NoSuchElementException e) {
            // Add an error message for missing Test Plan
            redirectAttributes.addFlashAttribute("error", "Test plan not found.");
            return "redirect:/viewTestPlans";
        } catch (Exception e) {
            // Handle unexpected exceptions
            redirectAttributes.addFlashAttribute("error", "An unexpected error occurred.");
            return "redirect:/viewTestPlans";
        }
    }

    // View Test Suites for a Test Plan
    @GetMapping("/viewTestPlanTestSuites/{testPlanId}")
    public String viewTestPlanTestSuites(@PathVariable String testPlanId, Model model) {
        List<TestSuite> testSuites = testPlanService.getTestSuitesByTestPlan(testPlanId);
        model.addAttribute("testSuites", testSuites);
        return "viewTestPlanTestSuites";
    }

    // Assign Test Suite to a Test Plan
    @PostMapping("/assignTestSuite")
    public String assignTestSuite(@RequestParam String testPlanId, @RequestParam String testSuiteId,
            RedirectAttributes redirectAttributes) {
        try {
            testPlanService.assignTestSuiteToTestPlan(testPlanId, testSuiteId);
            redirectAttributes.addFlashAttribute("success", "Test suite assigned successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to assign test suite: " + e.getMessage());
        }
        return "redirect:/viewTestPlans";
    }

    @GetMapping("/getTestSuitesByTestPlan/{testPlanId}")
    public String getTestSuitesByTestPlan(@PathVariable String testPlanId, Model model,
            RedirectAttributes redirectAttributes) {
        try {
            List<TestSuite> testSuites = testPlanService.getTestSuitesByTestPlan(testPlanId);
            model.addAttribute("testSuites", testSuites);
            return "testSuitesView";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Unable to retrieve test suites.");
            return "redirect:/viewTestPlans";
        }
    }
}
