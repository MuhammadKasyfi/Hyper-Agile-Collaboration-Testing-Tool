package demo_ver.demo.controllers;

import java.util.Collection;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import demo_ver.demo.model.TestPlan;
import demo_ver.demo.service.TestPlanService;

@Controller
public class TestPlanController {

    @Autowired
    private TestPlanService testPlanService; // Injecting the service

    // View the list of test plans
    @GetMapping("/viewTestPlans")
    public String viewTestPlans(Model model, Authentication authentication) {
        // Check if the current user has the 'STAKEHOLDER' role
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        boolean isStakeholder = authorities.stream()
                .anyMatch(authority -> authority.getAuthority().equalsIgnoreCase("ROLE_STAKEHOLDER"));

        // Add the role flag and test plans to the model
        model.addAttribute("testPlans", testPlanService.viewTestPlans());
        model.addAttribute("isStakeholder", isStakeholder);

        return "viewTestPlans"; // Refers to viewTestPlans.html
    }

    // Handle the "Create Test Plan" page
    @GetMapping("/createTestPlan")
    public String createTestPlan() {
        return "createTestPlan"; // Refers to createTestPlan.html
    }

    // Create a test plan
    @PostMapping("/createTestPlan")
    public String createTestPlan(@RequestParam String name,
            @RequestParam String description,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(required = false) Boolean isPublic,
            RedirectAttributes redirectAttributes) {
        // Default to false if null
        testPlanService.createTestPlan(name, description,
                isActive != null ? isActive : false,
                isPublic != null ? isPublic : false);
        redirectAttributes.addFlashAttribute("success", "Test plan created successfully");
        return "redirect:/viewTestPlans";
    }

    // Edit a test plan
    @GetMapping("/editTestPlan")
    public String editTestPlan(@RequestParam Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            TestPlan testPlan = testPlanService.viewTestPlanById(id); // Find the test plan by ID
            model.addAttribute("testPlan", testPlan); // Add it to the model
            return "editTestPlan"; // Return the name of the view (editTestPlan.html)
        } catch (NoSuchElementException e) {
            // Handle case when test plan is not found
            redirectAttributes.addFlashAttribute("error", "Test plan not found");
            return "redirect:/viewTestPlans"; // Redirect back to the test plans list with an error message
        }
    }

    @PostMapping("/editTestPlan")
    public String updateTestPlan(@RequestParam Long id,
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(required = false) Boolean isPublic,
            RedirectAttributes redirectAttributes) {
        testPlanService.updateTestPlan(id, name, description,
                isActive != null ? isActive : false,
                isPublic != null ? isPublic : false);
        redirectAttributes.addFlashAttribute("success", "Test plan updated successfully");
        return "redirect:/viewTestPlans";
    }

    // Delete a test plan
    @PostMapping("/deleteTestPlan")
    public String deleteTestPlan(@RequestParam Long id, RedirectAttributes redirectAttributes) {
        try {
            boolean deleted = testPlanService.deleteTestPlan(id);
            if (deleted) {
                redirectAttributes.addFlashAttribute("success", "Test plan deleted successfully");
            } else {
                redirectAttributes.addFlashAttribute("error", "Test plan not found");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete test plan");
        }
        return "redirect:/viewTestPlans";
    }

    // View Test Plan Details
    @GetMapping("/viewTestPlanDetails/{id}")
    public String viewTestPlanDetails(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            // Use 'id' to find the specific test plan
            TestPlan testPlan = testPlanService.viewTestPlanById(id);

            // Fetch the current user's role
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUserRole = authentication.getAuthorities().toString();

            // Check if the user is a Stakeholder
            boolean isStakeholder = currentUserRole.contains("STAKEHOLDER");

            // Optional: You can also check for other roles if needed
            boolean isAdmin = currentUserRole.contains("ADMIN");
            boolean isProjectManager = currentUserRole.contains("PROJECT_MANAGER");

            // Role-based restrictions or additional checks (if needed)
            // Example: if stakeholders should have limited access, you can add checks here
            if (isStakeholder) {
                // Add a message or restrict access if the user is a Stakeholder
                redirectAttributes.addFlashAttribute("error",
                        "You do not have permission to view detailed test plan information.");
                return "redirect:/viewTestPlans";
            }

            // If no restrictions, continue to display the details of the test plan
            model.addAttribute("testPlan", testPlan);
            return "viewTestPlanDetails";

        } catch (NoSuchElementException e) {
            // If the test plan is not found, redirect with an error message
            redirectAttributes.addFlashAttribute("error", "Test plan not found");
            return "redirect:/viewTestPlans";
        }
    }

}
