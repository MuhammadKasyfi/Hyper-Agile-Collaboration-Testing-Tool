package demo_ver.demo.controllers;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import demo_ver.demo.model.ManageUser;
import demo_ver.demo.model.TestSuite;
import demo_ver.demo.service.ManageUserService;
import demo_ver.demo.service.TestSuiteService;

@Controller
public class TestSuiteController {

    @Autowired
    private TestSuiteService testSuiteService; // Injecting the service

    @Autowired
    private ManageUserService manageUserService; // Service to fetch user data

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
    public String editTestSuite(@RequestParam String id, Model model, RedirectAttributes redirectAttributes) {
        TestSuite testSuite;
        try {
            testSuite = testSuiteService.viewTestSuiteById(id);
        } catch (NoSuchElementException e) {
            redirectAttributes.addFlashAttribute("error", "Test suite not found");
            return "redirect:/viewTestSuites";
        }
        model.addAttribute("testSuite", testSuite);
        return "editTestSuite"; // Return the name of the view (editTestSuite.html)
    }

    // Update a test suite
    @PostMapping("/editTestSuite")
    public String updateTestSuite(@RequestParam String id,
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam String status,
            @RequestParam String importance,
            @RequestParam(required = false) String testCases,
            RedirectAttributes redirectAttributes) {
        try {
            testSuiteService.updateTestSuite(id, name, description, status, importance, testCases);
            redirectAttributes.addFlashAttribute("success", "Test suite updated successfully");
        } catch (NoSuchElementException e) {
            redirectAttributes.addFlashAttribute("error", "Test suite not found");
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
                redirectAttributes.addFlashAttribute("error", "Test suite not found");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete test suite");
        }
        return "redirect:/viewTestSuites"; // Redirect back to the test suites list
    }

    @GetMapping("/assignUsersToTestSuite")
    public String showAssignUsersForm(@RequestParam String id, Model model) {
        TestSuite testSuite = testSuiteService.viewTestSuiteById(id);
        List<ManageUser> allUsers = manageUserService.getAllUsers();

        model.addAttribute("testSuite", testSuite);
        model.addAttribute("users", allUsers); // Pass all users to the view
        return "assignUsersToTestSuite"; // Refers to assignUsersToTestSuite.html
    }

    /*// Assign users to a test suite
    @PostMapping("/assignUsersToTestSuite")
    public String assignUsersToTestSuite(@RequestParam String id,
            @RequestParam List<Integer> userIds,
            RedirectAttributes redirectAttributes) {
        try {
            testSuiteService.assignUsersToTestSuite(id, userIds);
            redirectAttributes.addFlashAttribute("success", "Users assigned successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to assign users");
        }
        return "redirect:/viewTestSuites"; // Redirect to test suites list
    }
*/
    @GetMapping("/viewTestSuite")
    public String viewTestSuiteDetails(@RequestParam String id, Model model, RedirectAttributes redirectAttributes) {
        try {
            TestSuite testSuite = testSuiteService.viewTestSuiteById(id);
            model.addAttribute("testSuite", testSuite);
            return "viewTestSuiteDetails"; // Refers to viewTestSuiteDetails.html
        } catch (NoSuchElementException e) {
            redirectAttributes.addFlashAttribute("error", "Test suite not found");
            return "redirect:/viewTestSuites"; // Redirect back to the test suites list with an error message
        }
    }
}
