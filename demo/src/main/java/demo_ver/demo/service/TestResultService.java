package demo_ver.demo.service;

import demo_ver.demo.model.TestResult;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TestResultService {

    private final List<TestResult> testResults = new ArrayList<>();
    private Long idCounter = 1L;

    // Add Test Result with a Test Case ID
    public void addTestResult(Long testCaseId, String testName, String status, String description) {
        TestResult testResult = new TestResult();
        testResult.setId(idCounter++);
        testResult.setTestCaseId(testCaseId);
        testResult.setTestName(testName);
        testResult.setStatus(status);
        testResult.setDescription(description);
        testResult.setExecutionDate(LocalDateTime.now());

        testResults.add(testResult);
    }

    // Add General Test Result (No Test Case ID)
    public void addGeneralTestResult(String testName, String status, String description) {
        TestResult testResult = new TestResult();
        testResult.setId(idCounter++);
        testResult.setTestCaseId(null); // General result
        testResult.setTestName(testName);
        testResult.setStatus(status);
        testResult.setDescription(description);
        testResult.setExecutionDate(LocalDateTime.now());

        testResults.add(testResult);
    }

    // Get Test Results by Test Case ID
    public List<TestResult> getTestResultsByTestCaseId(Long testCaseId) {
        return testResults.stream()
                .filter(result -> result.getTestCaseId() != null && result.getTestCaseId().equals(testCaseId))
                .collect(Collectors.toList());
    }

    // Get All Test Results
    public List<TestResult> getAllTestResults() {
        return new ArrayList<>(testResults);
    }

    public boolean deleteTestResult(Long id) {
        return testResults.removeIf(result -> result.getId().equals(id));
    }
}
