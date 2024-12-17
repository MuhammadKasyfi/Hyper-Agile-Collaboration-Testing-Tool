package demo_ver.demo.controllers;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import demo_ver.demo.model.ManageUser;
import demo_ver.demo.model.TestPlan;
import demo_ver.demo.model.TestSuite;
import demo_ver.demo.service.ManageUserService;
import demo_ver.demo.service.TestSuiteService;
import demo_ver.demo.service.TestPlanService;

@Controller
public class TestSuiteController {

    @Autowired
    private TestSuiteService testSuiteService;

    @Autowired
    private ManageUserService manageUserService;

    @Autowired
    private TestPlanService testPlanService;

    // View the list of test suites
    @GetMapping("/viewTestSuites")
    public String viewTestSuites(Model model) {
        model.addAttribute("testSuites", testSuiteService.viewTestSuites());
        return "viewTestSuites";
    }

    // View Test Suite Details with assigned test plans
    // View Test Suite Details with assigned test plans
    @GetMapping("/viewTestSuiteDetails/{id}")
    public String viewTestSuiteDetails(@PathVariable String id, Model model, RedirectAttributes redirectAttributes) {
        try {
            // Fetch the TestSuite by ID
            TestSuite testSuite = testSuiteService.viewTestSuiteById(id);

            // Fetch the assigned test plans for the test suite
            List<TestPlan> assignedTestPlans = testPlanService.getAssignedTestPlansByTestSuiteId(id);

            // Add the test suite and assigned test plans to the model
            model.addAttribute("testSuite", testSuite);
            model.addAttribute("assignedTestPlans", assignedTestPlans);

            // If no assigned test plans, display a message in the view (handled by the
            // Thymeleaf template)
            if (assignedTestPlans.isEmpty()) {
                model.addAttribute("noAssignedPlansMessage", "No test plans assigned to this test suite.");
            }

            return "viewTestSuiteDetails"; // Return the view template

        } catch (NoSuchElementException e) {
            // If test suite not found, redirect with an error message
            redirectAttributes.addFlashAttribute("error", "Test suite not found.");
            return "redirect:/viewTestSuites"; // Redirect to the list of test suites
        } catch (Exception e) {
            // Catch any other unexpected errors and redirect with an error message
            redirectAttributes.addFlashAttribute("error", "An unexpected error occurred.");
            return "redirect:/viewTestSuites"; // Redirect to the list of test suites
        }
    }

    // Handle the "Create Test Suite" page
    @GetMapping("/createTestSuite")
    public String createTestSuite(Model model) {
        // Fetch the list of all test plans from the service
        List<TestPlan> testPlans = testPlanService.viewTestPlans();

        // Add the test plans to the model to be accessed in the Thymeleaf template
        model.addAttribute("testPlans", testPlans);

        // Return the view
        return "createTestSuite";
    }

    // Create a test suite
    @PostMapping("/createTestSuite")
    public String createTestSuite(@RequestParam String name, @RequestParam String description,
            RedirectAttributes redirectAttributes) {
        testSuiteService.createTestSuite(name, description);
        redirectAttributes.addFlashAttribute("success", "Test suite created successfully");
        return "redirect:/viewTestSuites";
    }

    // Edit a test suite
    @GetMapping("/editTestSuite")
    public String editTestSuite(@RequestParam String id, Model model, RedirectAttributes redirectAttributes) {
        try {
            TestSuite testSuite = testSuiteService.viewTestSuiteById(id);
            model.addAttribute("testSuite", testSuite);
            return "editTestSuite";
        } catch (NoSuchElementException e) {
            redirectAttributes.addFlashAttribute("error", "Test suite with ID " + id + " not found");
            return "redirect:/viewTestSuites";
        }
    }

    // Update a test suite
    @PostMapping("/editTestSuite")
    public String updateTestSuite(@RequestParam String id, @RequestParam String name, @RequestParam String description,
            @RequestParam String status, @RequestParam String importance, RedirectAttributes redirectAttributes) {
        try {
            testSuiteService.updateTestSuite(id, name, description, status, importance, importance);
            redirectAttributes.addFlashAttribute("success", "Test suite updated successfully");
        } catch (NoSuchElementException e) {
            redirectAttributes.addFlashAttribute("error", "Test suite with ID " + id + " not found");
        }
        return "redirect:/viewTestSuites";
    }

    // Delete a test suite
    @PostMapping("/deleteTestSuite")
    public String deleteTestSuite(@RequestParam String id, RedirectAttributes redirectAttributes) {
        try {
            boolean deleted = testSuiteService.deleteTestSuite(id);
            if (deleted) {
                redirectAttributes.addFlashAttribute("success", "Test suite deleted successfully");
            } else {
                redirectAttributes.addFlashAttribute("error", "Test suite with ID " + id + " not found");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete test suite with ID " + id);
        }
        return "redirect:/viewTestSuites";
    }

    // Show form to assign users to a test suite
    @GetMapping("/assignUsersToTestSuite")
    public String showAssignUsersForm(@RequestParam String id, Model model) {
        TestSuite testSuite = testSuiteService.viewTestSuiteById(id);
        List<ManageUser> allUsers = manageUserService.getAllUsers();
        model.addAttribute("testSuite", testSuite);
        model.addAttribute("users", allUsers);
        return "assignUsersToTestSuite";
    }

    // Handle user assignment to test suite
    @PostMapping("/assignUsersToTestSuite")
    public String assignUsersToTestSuite(@RequestParam String testSuiteId, @RequestParam List<String> userIds,
            RedirectAttributes redirectAttributes) {
        try {
            testSuiteService.assignUsersToTestSuite(testSuiteId, userIds);
            redirectAttributes.addFlashAttribute("success", "Users successfully assigned to the test suite");
        } catch (NoSuchElementException e) {
            redirectAttributes.addFlashAttribute("error", "Failed to assign users to the test suite");
        }
        return "redirect:/viewTestSuite?id=" + testSuiteId;
    }

    // Assign test plans to a test suite (POST request)
    @PostMapping("/assignTestPlansToTestSuite")
    public String assignTestPlansToTestSuite(@RequestParam String testSuiteId, @RequestParam List<String> testPlanIds,
            RedirectAttributes redirectAttributes) {
        try {
            testSuiteService.assignTestPlansToTestSuite(testSuiteId, testPlanIds);
            redirectAttributes.addFlashAttribute("success", "Test plans successfully assigned to the test suite");
        } catch (NoSuchElementException e) {
            redirectAttributes.addFlashAttribute("error", "Failed to assign test plans to the test suite");
        }
        return "redirect:/viewTestSuiteDetails/" + testSuiteId;
    }
}
