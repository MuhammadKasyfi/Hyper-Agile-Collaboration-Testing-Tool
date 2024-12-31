package demo_ver.demo.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import demo_ver.demo.model.ManageUser;
import demo_ver.demo.model.TestPlan;
import demo_ver.demo.model.Build;
import demo_ver.demo.model.TestSuite;
import demo_ver.demo.service.ManageUserService;
import demo_ver.demo.service.TestSuiteService;
import demo_ver.demo.service.TestPlanService;
import demo_ver.demo.service.BuildService;

import javax.servlet.http.HttpSession;

@Controller
public class TestSuiteController {

    @Autowired
    private TestSuiteService testSuiteService;

    @Autowired
    private ManageUserService manageUserService;

    @Autowired
    private TestPlanService testPlanService;

    @Autowired
    private BuildService buildService;

    // View the list of test suites
    @GetMapping("/viewTestSuites")
    public String viewTestSuites(Model model) {
        model.addAttribute("testSuites", testSuiteService.viewTestSuites());
        return "viewTestSuites";
    }

    // View Test Suite Details with assigned test plans
    @GetMapping("/viewTestSuiteDetails/{id}")
    public String viewTestSuiteDetails(@PathVariable String id, Model model, RedirectAttributes redirectAttributes) {
        try {
            // Fetch the TestSuite by ID
            TestSuite testSuite = testSuiteService.viewTestSuiteById(id);

            // Fetch the assigned TestPlans for the TestSuite
            List<TestPlan> assignedTestPlans = testSuite.getAssignedTestPlans(); // Retrieve assigned test plans
            // List<Build> assignedBuilds = build.getAssignedBuilds(); // Retrieve assigned
            // test plans

            // Add the TestSuite and assigned TestPlans to the model
            model.addAttribute("testSuite", testSuite);
            model.addAttribute("assignedTestPlans", assignedTestPlans);
            // model.addAttribute("assignedBuilds", assignedBuilds);

            return "viewTestSuiteDetails"; // Return the view template for displaying the test suite details

        } catch (NoSuchElementException e) {
            redirectAttributes.addFlashAttribute("error", "Test suite not found.");
            return "redirect:/viewTestSuites";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "An unexpected error occurred.");
            return "redirect:/viewTestSuites";
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
    public String createTestSuite(@RequestParam String name,
            @RequestParam String description,
            @RequestParam List<String> testPlanIds,
            @RequestParam(required = false) List<String> userID,
            RedirectAttributes redirectAttributes) {

        // Create the TestSuite
        TestSuite testSuite = testSuiteService.createTestSuite(name, description);

        // Fetch the TestPlans by their IDs and assign them to the TestSuite
        List<TestPlan> assignedTestPlans = new ArrayList<>();
        for (String testPlanId : testPlanIds) {
            TestPlan testPlan = testPlanService.getTestPlanById(testPlanId); // Fetching TestPlan from the service
            assignedTestPlans.add(testPlan);
        }

        // // Fetch the TestPlans by their IDs and assign them to the TestSuite
        // List<Build> assignedBuilds = new ArrayList<>();
        // for (String bId : bIds) {
        // Build builds = buildService.getBuildById(bId); // Fetching TestPlan from the
        // service
        // assignedBuilds.add(builds);
        // }

        // Assign the selected TestPlans to the TestSuite
        testSuite.setAssignedTestPlans(assignedTestPlans);

        // Add success message and redirect to the view page
        redirectAttributes.addFlashAttribute("success", "Test suite created successfully");
        return "redirect:/viewTestSuites";
    }

    // Edit a test suite form
    @GetMapping("/editTestSuite/{id}")
    public String editTestSuite(@PathVariable String id, Model model, RedirectAttributes redirectAttributes) {
        try {
            TestSuite testSuite = testSuiteService.viewTestSuiteById(id);
            model.addAttribute("testSuite", testSuite);
            return "editTestSuite";
        } catch (NoSuchElementException e) {
            redirectAttributes.addFlashAttribute("error", "Test suite not found with ID: " + id);
            return "redirect:/viewTestSuites";
        }
    }

    // Update a test suite
    @PostMapping("/editTestSuite/{id}")
    public String updateTestSuite(
            @PathVariable String id,
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam String status,
            @RequestParam String importance,
            RedirectAttributes redirectAttributes) {
        try {
            testSuiteService.updateTestSuite(id, name, description, status, importance);
            redirectAttributes.addFlashAttribute("success", "Test suite updated successfully!");
        } catch (NoSuchElementException e) {
            redirectAttributes.addFlashAttribute("error", "Test suite not found with ID: " + id);
        }
        return "redirect:/viewTestSuites";
    }

    // Delete a test suite by ID
    @PostMapping("/deleteTestSuite/{id}")
    public String deleteTestSuite(@PathVariable String id, RedirectAttributes redirectAttributes) {
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

    // Show form to assign users to a test suite
    @GetMapping("/assignUsersToTestSuite")
    public String showAssignUsersForm(@RequestParam String id, Model model, RedirectAttributes redirectAttributes) {
        try {
            // Fetch the TestSuite by ID
            TestSuite testSuite = testSuiteService.viewTestSuiteById(id);

            // Fetch all users
            List<ManageUser> allUsers = ManageUserService.getAllUsers();

            // Add TestSuite and users to the model
            model.addAttribute("testSuite", testSuite);
            model.addAttribute("users", allUsers);

            return "viewTestSuites"; // Thymeleaf template for user assignment

        } catch (NoSuchElementException e) {
            redirectAttributes.addFlashAttribute("error", "Test suite not found.");
            return "redirect:/viewTestSuites";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "An unexpected error occurred.");
            return "redirect:/viewTestSuites";
        }
    }

    // Handle user assignment to test suite
    @PostMapping("/assignUsersToTestSuite")
    public String assignUsersToTestSuite(@RequestParam String testSuiteId, @RequestParam List<String> userIds,
            RedirectAttributes redirectAttributes) {
        try {
            // Assign users to the TestSuite
            testSuiteService.assignUsersToTestSuite(testSuiteId, userIds);
            redirectAttributes.addFlashAttribute("success", "Users successfully assigned to the test suite.");
        } catch (NoSuchElementException e) {
            redirectAttributes.addFlashAttribute("error", "Failed to assign users to the test suite.");
        }
        return "redirect:/viewTestSuiteDetails/" + testSuiteId;
    }

    // Assign test plans to a test suite (POST request)
    @PostMapping("/assignBuildToTestSuite")
    public String assignBuildToTestSuite(@RequestParam String testSuiteId, @RequestParam List<String> bIds,
            RedirectAttributes redirectAttributes) {
        try {
            testSuiteService.assignTestPlansToTestSuite(testSuiteId, bIds);
            redirectAttributes.addFlashAttribute("success", "Test plans successfully assigned to the test suite");
        } catch (NoSuchElementException e) {
            redirectAttributes.addFlashAttribute("error", "Failed to assign test plans to the test suite");
        }
        return "redirect:/viewTestSuiteDetails/" + testSuiteId;
    }

}
