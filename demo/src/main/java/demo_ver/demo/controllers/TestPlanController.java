package demo_ver.demo.controllers;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.info.ProjectInfoProperties.Build;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import demo_ver.demo.model.TestPlan;
import demo_ver.demo.model.TestSuite;
import demo_ver.demo.model.Build;
import demo_ver.demo.service.TestPlanService;
import demo_ver.demo.service.TestSuiteService;
import demo_ver.demo.service.BuildService;

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
        isActive = isActive != null && !isActive.isEmpty() ? isActive : "false";
        isPublic = isPublic != null && !isPublic.isEmpty() ? isPublic : "false";

        testPlanService.createTestPlan(name, description, isActive, isPublic);
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
            isActive = isActive != null && !isActive.isEmpty() ? isActive : "false";
            isPublic = isPublic != null && !isPublic.isEmpty() ? isPublic : "false";

            testPlanService.updateTestPlan(id, name, description, isActive, isPublic);
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

    // View Test Plan Details
    @GetMapping("/viewTestPlanDetails/{id}")
    public String viewTestPlanDetails(@PathVariable String id, Model model, RedirectAttributes redirectAttributes) {
        try {
            TestPlan testPlan = testPlanService.viewTestPlanById(id);
            model.addAttribute("testPlan", testPlan);

            return "viewTestPlanDetails";

        } catch (NoSuchElementException e) {
            redirectAttributes.addFlashAttribute("error", "Test plan not found.");
            return "redirect:/viewTestPlans";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "An unexpected error occurred.");
            return "redirect:/viewTestPlans";
        }
    }


}
