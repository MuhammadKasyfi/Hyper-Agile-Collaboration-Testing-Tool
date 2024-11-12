package demo_ver.demo.controllers;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import demo_ver.demo.model.TestSuite;
import demo_ver.demo.service.TestSuiteService;

@Controller
public class TestSuiteController {

    @Autowired
    private TestSuiteService testSuiteService; // Injecting the service

    // View the list of test suites
    @GetMapping("/viewTestSuites")
    public String viewTestSuites(Model model) {
        model.addAttribute("testSuites", testSuiteService.viewTestSuites());
        return "viewTestSuites"; // Refers to viewTestSuites.html
    }

    // Handle the "Create Test Suite" page
    @GetMapping("/createTestSuite")
    public String createTestSuite() {
        return "createTestSuite"; // Refers to createTestSuite.html
    }

    // Create a test suite
    @PostMapping("/createTestSuite")
    public String createTestSuite(@RequestParam String name, 
                                  @RequestParam String description,
                                  RedirectAttributes redirectAttributes) {
        // Default to false if null
        testSuiteService.createTestSuite(name, description);
        redirectAttributes.addFlashAttribute("success", "Test suite created successfully");
        return "redirect:/viewTestSuites"; // Redirect to the viewTestSuites page after creation
    }

    // Edit a test suite
    @GetMapping("/editTestSuite")
    public String editTestSuite(@RequestParam Long id, Model model, RedirectAttributes redirectAttributes) {
        TestSuite testSuite;
        try {
            testSuite = testSuiteService.viewTestSuiteById(id); // Find the test suite by ID
        } catch (NoSuchElementException e) {
            // Handle case when test suite is not found
            redirectAttributes.addFlashAttribute("error", "Test suite not found");
            return "redirect:/viewTestSuites"; // Redirect back to the test suites list with an error message
        }

        model.addAttribute("testSuite", testSuite); // Add it to the model
        return "editTestSuite"; // Return the name of the view (editTestSuite.html)
    }

    // Update a test suite
    @PostMapping("/editTestSuite")
    public String updateTestSuite(@RequestParam Long id, 
                                  @RequestParam String name, 
                                  @RequestParam String description, 
                                  RedirectAttributes redirectAttributes) {
        // Default to false if null for both fields
        testSuiteService.updateTestSuite(id, name, description);

        redirectAttributes.addFlashAttribute("success", "Test suite updated successfully");
        return "redirect:/viewTestSuites"; // Redirect to the viewTestSuites page after update
    }

    // Delete a test suite
    @PostMapping("/deleteTestSuite")
    public String deleteTestSuite(@RequestParam Long id, RedirectAttributes redirectAttributes) {
        try {
            boolean deleted = testSuiteService.deleteTestSuite(id);
            if (deleted) {
                redirectAttributes.addFlashAttribute("success", "Test suite deleted successfully");
            } else {
                redirectAttributes.addFlashAttribute("error", "Test suite not found");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete test suite");
        }
        return "redirect:/viewTestSuites"; // Redirect back to the test suites list
    }
    
}
