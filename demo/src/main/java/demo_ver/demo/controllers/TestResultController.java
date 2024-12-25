package demo_ver.demo.controllers;

import demo_ver.demo.model.TestResult;
import demo_ver.demo.service.TestResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class TestResultController {

    @Autowired
    private TestResultService testResultService;

    // View Test Results for a Test Case
    @GetMapping("/viewTestResults")
    public String viewTestResults(@RequestParam(required = false) Long testCaseId, Model model) {
        List<TestResult> testResults;

        if (testCaseId != null) {
            testResults = testResultService.getTestResultsByTestCaseId(testCaseId);
            model.addAttribute("testCaseId", testCaseId);
        } else {
            testResults = testResultService.getAllTestResults();
        }

        model.addAttribute("testResults", testResults);
        return "viewTestResults";
    }

    // Show Insert Test Result Form
    // @GetMapping("/insertTestResult")
    // public String insertTestResultForm(@RequestParam(required = false) Long testCaseId, Model model) {
    //     model.addAttribute("testCaseId", testCaseId); // Include testCaseId if provided
    //     return "insertTestResult";
    // }

    // Show Insert Test Result Form
    @GetMapping("/insertTestResult")
    public String insertTestResultForm(@RequestParam(required = false) Long testCaseId, Model model) {
    List<String> sampleTestCases = List.of(
        "Login Functionality",
        "Registration Page",
        "Password Reset",
        "Checkout Process"
    );
    model.addAttribute("sampleTestCases", sampleTestCases);
    model.addAttribute("testCaseId", testCaseId); // Include testCaseId if provided
    return "insertTestResult";
}


    // Handle Insert Test Result Form Submission
    @PostMapping("/insertTestResult")
    public String insertTestResult(
            @RequestParam(required = false) Long testCaseId,
            @RequestParam String testName,
            @RequestParam String status,
            @RequestParam(required = false) String description,
            RedirectAttributes redirectAttributes) {

        try {
            if (testCaseId != null) {
                testResultService.addTestResult(testCaseId, testName, status, description);
                redirectAttributes.addFlashAttribute("success", "Test result added successfully!");
                return "redirect:/viewTestResults?testCaseId=" + testCaseId;
            } else {
                testResultService.addGeneralTestResult(testName, status, description);
                redirectAttributes.addFlashAttribute("success", "Test result added successfully!");
                return "redirect:/viewTestResults";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to insert test result: " + e.getMessage());
            return "redirect:/insertTestResult";
        }
    }

    // Handle Delete Test Result
    @PostMapping("/deleteTestResult")
    public String deleteTestResult(@RequestParam Long id, RedirectAttributes redirectAttributes) {
        try {
            boolean isDeleted = testResultService.deleteTestResult(id);
            if (isDeleted) {
                redirectAttributes.addFlashAttribute("success", "Test result deleted successfully!");
            } else {
                redirectAttributes.addFlashAttribute("error", "Test result not found with ID: " + id);
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete test result: " + e.getMessage());
        }
        return "redirect:/viewTestResults";
    }
}
