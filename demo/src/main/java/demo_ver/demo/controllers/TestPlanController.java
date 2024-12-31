package demo_ver.demo.controllers;

import java.util.ArrayList;
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
import demo_ver.demo.service.TestSuiteService;

@Controller
public class TestPlanController {

    @Autowired
    private TestPlanService testPlanService;

    @Autowired
    private TestSuiteService testSuiteService;

    // Utility method to check if a user has a specific role
    private boolean hasRole(Authentication authentication, String role) {
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equalsIgnoreCase("ROLE_" + role));
    }

    // View the list of test plans
    @GetMapping("/viewTestPlans")
    public String viewTestPlans(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Boolean isActive,
            Model model,
            Authentication authentication) {
        boolean isStakeholder = hasRole(authentication, "STAKEHOLDER");

        // Call service to filter test plans based on search and isActive
        List<TestPlan> testPlans = testPlanService.filterTestPlans(search, isActive);
        model.addAttribute("testPlans", testPlans);
        model.addAttribute("isStakeholder", isStakeholder);
        model.addAttribute("search", search);
        model.addAttribute("isActive", isActive);

        return "viewTestPlans";
    }

    @GetMapping("/createTestPlan")
    public String createTestPlan(Model model) {
        List<TestSuite> testSuites = testSuiteService.viewTestSuites();
        if (testSuites.isEmpty()) {
            model.addAttribute("error", "No test suites available.");
        }
        model.addAttribute("testSuites", testSuites);
        return "createTestPlan";
    }

    // Create Test Plan - POST
    @PostMapping("/createTestPlan")
    public String createTestPlan(@RequestParam String name,
            @RequestParam String description,
            @RequestParam(required = false) String isActive,
            @RequestParam(required = false) String isPublic,
            @RequestParam(required = false) List<String> testSuiteIds, // Optional
            RedirectAttributes redirectAttributes) {
        isActive = isActive != null && !isActive.isEmpty() ? isActive : "false";
        isPublic = isPublic != null && !isPublic.isEmpty() ? isPublic : "false";

        // Handle the case where no test suites are selected
        if (testSuiteIds == null) {
            testSuiteIds = new ArrayList<>(); // Provide a default empty list if no test suites are selected
        }

        // Call service to create the test plan with assigned test suites
        testPlanService.createTestPlan(name, description, isActive, isPublic, testSuiteIds);
        redirectAttributes.addFlashAttribute("success", "Test plan created successfully.");
        return "redirect:/viewTestPlans";
    }

    // View Test Plan Details
    @GetMapping("/viewTestPlanDetails/{id}")
    public String viewTestPlanDetails(@PathVariable String id, Model model, RedirectAttributes redirectAttributes) {
        try {
            // Fetch the test plan by ID
            TestPlan testPlan = testPlanService.viewTestPlanById(id);

            // Fetch the assigned test suites for this test plan
            List<TestSuite> assignedTestSuites = testPlanService.getAssignedTestSuitesByTestPlanId(id);

            // Add both the test plan and the assigned test suites to the model
            model.addAttribute("testPlan", testPlan);
            model.addAttribute("assignedTestSuites", assignedTestSuites);

            return "viewTestPlanDetails"; // Return the Thymeleaf template for viewing test plan details

        } catch (NoSuchElementException e) {
            redirectAttributes.addFlashAttribute("error", "Test plan not found.");
            return "redirect:/viewTestPlans";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "An unexpected error occurred.");
            return "redirect:/viewTestPlans";
        }
    }

    // Edit Test Plan - GET
    @GetMapping("/editTestPlan")
    public String editTestPlanForm(@RequestParam String id, Model model, RedirectAttributes redirectAttributes) {
        try {
            TestPlan testPlan = testPlanService.viewTestPlanById(id);
            List<TestSuite> availableTestSuites = testSuiteService.viewTestSuites(); // Get available test suites

            // Fetch the assigned test suites for the test plan
            List<TestSuite> assignedTestSuites = testSuiteService.getAssignedTestSuitesByTestPlanId(id);

            // Add to model
            model.addAttribute("testPlan", testPlan);
            model.addAttribute("availableTestSuites", availableTestSuites);
            model.addAttribute("assignedTestSuites", assignedTestSuites);

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
            @RequestParam(required = false) List<String> assignedTestSuites, // Get assigned test suites from the form
            RedirectAttributes redirectAttributes) {
        try {
            isActive = isActive != null && !isActive.isEmpty() ? isActive : "false";
            isPublic = isPublic != null && !isPublic.isEmpty() ? isPublic : "false";

            // Update the test plan
            testPlanService.updateTestPlan(id, name, description, isActive, isPublic, assignedTestSuites);
            redirectAttributes.addFlashAttribute("success", "Test plan updated successfully.");
        } catch (NoSuchElementException e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update test plan: " + e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "An unexpected error occurred: " + e.getMessage());
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

    // Show form to assign test suites to a test plan
    @GetMapping("/assignTestSuitesToTestPlan")
    public String showAssignTestSuitesForm(@RequestParam String testPlanId, Model model,
            RedirectAttributes redirectAttributes) {
        try {
            // Fetch the test plan by ID
            TestPlan testPlan = testPlanService.viewTestPlanById(testPlanId);

            // Fetch the list of all available test suites
            List<TestSuite> allTestSuites = testSuiteService.viewTestSuites();

            // Add test plan and test suites to the model
            model.addAttribute("testPlan", testPlan);
            model.addAttribute("testSuites", allTestSuites);

            return "assignTestSuitesToTestPlan"; // Return the Thymeleaf template for assigning test suites
        } catch (NoSuchElementException e) {
            redirectAttributes.addFlashAttribute("error", "Test plan not found.");
            return "redirect:/viewTestPlans";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "An unexpected error occurred.");
            return "redirect:/viewTestPlans";
        }
    }

    // Handle assignment of test suites to a test plan
    @PostMapping("/assignTestSuitesToTestPlan")
    public String assignTestSuitesToTestPlan(@RequestParam String testPlanId,
            @RequestParam List<String> testSuiteIds,
            RedirectAttributes redirectAttributes) {
        try {
            // Assign the selected test suites to the test plan
            testPlanService.assignTestSuitesToTestPlan(testPlanId, testSuiteIds, null);

            redirectAttributes.addFlashAttribute("success", "Test suites successfully assigned to the test plan.");
        } catch (NoSuchElementException e) {
            redirectAttributes.addFlashAttribute("error", "Test plan not found.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to assign test suites to the test plan.");
        }
        return "redirect:/viewTestPlanDetails/" + testPlanId;
    }
}
