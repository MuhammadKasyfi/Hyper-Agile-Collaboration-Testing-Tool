package demo_ver.demo.model;

import java.time.LocalDateTime;

public class TestResult {

    private Long id;
    private Long testCaseId; // Optional field for associating with a specific test case
    private String testName; // Name of the test
    private String status; // Passed, Failed, Blocked, etc.
    private String description; // Details or comments about the test
    private LocalDateTime executionDate; // Timestamp for when the test was executed

    // Default constructor
    public TestResult() {
    }

    // Parameterized constructor
    public TestResult(Long id, Long testCaseId, String testName, String status, String description, LocalDateTime executionDate) {
        this.id = id;
        this.testCaseId = testCaseId;
        this.testName = testName;
        this.status = status;
        this.description = description;
        this.executionDate = executionDate;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTestCaseId() {
        return testCaseId;
    }

    public void setTestCaseId(Long testCaseId) {
        this.testCaseId = testCaseId;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        if (testName == null || testName.trim().isEmpty()) {
            throw new IllegalArgumentException("Test name cannot be null or empty");
        }
        this.testName = testName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("Status cannot be null or empty");
        }
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getExecutionDate() {
        return executionDate;
    }

    public void setExecutionDate(LocalDateTime executionDate) {
        if (executionDate == null) {
            throw new IllegalArgumentException("Execution date cannot be null");
        }
        this.executionDate = executionDate;
    }

    // Utility Methods

    /**
     * Validates the `TestResult` fields to ensure they are complete and consistent.
     *
     * @return true if all required fields are valid, false otherwise
     */
    public boolean isValid() {
        return id != null && testName != null && !testName.trim().isEmpty()
                && status != null && !status.trim().isEmpty() && executionDate != null;
    }

    @Override
    public String toString() {
        return "TestResult{" +
                "id=" + id +
                ", testCaseId=" + testCaseId +
                ", testName='" + testName + '\'' +
                ", status='" + status + '\'' +
                ", description='" + description + '\'' +
                ", executionDate=" + executionDate +
                '}';
    }
}
