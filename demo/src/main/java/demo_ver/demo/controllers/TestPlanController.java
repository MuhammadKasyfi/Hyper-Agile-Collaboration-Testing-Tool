package demo_ver.demo.controllers;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes; // For flash messages

import demo_ver.demo.model.TestPlan;
import demo_ver.demo.service.TestPlanService;

@Controller
public class TestPlanController {

    @Autowired
    private TestPlanService testPlanService; // Injecting the service

    // View the list of test plans
    @GetMapping("/viewTestPlans")
    public String viewTestPlans(Model model) {
        model.addAttribute("testPlans", testPlanService.viewTestPlans());
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
            RedirectAttributes redirectAttributes) { // Add RedirectAttributes for flash messages
        // Default to false if null
        testPlanService.createTestPlan(name, description,
                isActive != null ? isActive : false,
                isPublic != null ? isPublic : false);
        redirectAttributes.addFlashAttribute("success", "Test plan created successfully");
        return "redirect:/viewTestPlans"; // Redirect to the viewTestPlans page after creation
    }

    // Edit a test plan
    @GetMapping("/editTestPlan")
    public String editTestPlan(@RequestParam Long id, Model model, RedirectAttributes redirectAttributes) {
        TestPlan testPlan;
        try {
            testPlan = testPlanService.viewTestPlanById(id); // Find the test plan by ID
        } catch (NoSuchElementException e) {
            // Handle case when test plan is not found
            redirectAttributes.addFlashAttribute("error", "Test plan not found");
            return "redirect:/viewTestPlans"; // Redirect back to the test plans list with an error message
        }

        model.addAttribute("testPlan", testPlan); // Add it to the model
        return "editTestPlan"; // Return the name of the view (editTestPlan.html)
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
        return "redirect:/viewTestPlans"; // Redirect back to the test plans list
    }

}
