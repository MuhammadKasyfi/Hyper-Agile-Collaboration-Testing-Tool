package demo_ver.demo.model;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import demo_ver.demo.service.ManageUserService;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name="test_case")
public class TestCase {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int idtest_cases;
    
    private String test_desc;
    private String deadline;
    private String dateUpdated;
    private String projectId;
    // private Map<String, String> userReasons = new HashMap<>(); 
    private String testCaseName;
    private String dateCreated;
    private String smartContractID; // Changed from int to String
    // private List<Integer> userID;
    // private Map<String, String> userStatuses = new HashMap<>(); // New field for user-specific statuses
    private String overallStatus;
    private String username;
    private String createdBy;
    private String status;

    @OneToMany(mappedBy = "testCase", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TestCaseUserReason> userReasons;

    @OneToMany(mappedBy = "testCase", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TestCaseUserStatus> userStatuses;

    @OneToMany(mappedBy = "testCase", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TestCaseUserId> userIDs;

    @Transient
    @Autowired
    private ManageUserService manageUserService;

    public TestCase() {
        // Default constructor
    }

    public TestCase(String status, int idtest_cases, String projectId, String testCaseName,
            String test_desc, String dateCreated, String deadline) {
        this.status = status;
        this.idtest_cases = idtest_cases;
        this.projectId = projectId;
        this.testCaseName = testCaseName;
        this.test_desc = test_desc;
        this.dateCreated = dateCreated;
        this.deadline = deadline;
    }

    public TestCase(String status, int idtest_cases, String projectId, String smartContractID, String testCaseName,
            String test_desc, String dateCreated, String deadline) {
        this.status = status;
        this.idtest_cases = idtest_cases;
        this.projectId = projectId;
        this.smartContractID = smartContractID;
        this.testCaseName = testCaseName;
        this.test_desc = test_desc;
        this.dateCreated = dateCreated;
        this.deadline = deadline;
    }

    // public void setUserStatuses(Map<String, String> userStatuses) {
    //     this.userStatuses = userStatuses;
    // }

    // Getters and setters for existing fields
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // public Map<String, String> getUserReason() {
    //     return userReasons;
    // }

    // public void setUserReason(Map<String, String> userReason) {
    //     this.userReasons = userReason;
    // }

    // public void setUserReason(String username, String reason) {
    //     userReasons.put(username, reason);
    // }

    public List<TestCaseUserReason> getUserReasons() {
        return userReasons;
    }

    public void setUserReasons(List<TestCaseUserReason> userReasons) {
        this.userReasons = userReasons;
    }

    public List<TestCaseUserStatus> getUserStatuses() {
        return userStatuses;
    }

    public void setUserStatuses(List<TestCaseUserStatus> userStatuses) {
        this.userStatuses = userStatuses;
    }

    public List<TestCaseUserId> getUserIDs() {
        return userIDs;
    }

    public void setUserIDs(List<TestCaseUserId> userIDs) {
        this.userIDs = userIDs;
    }

    public int getIdtest_cases() {
        return idtest_cases;
    }

    public void setIdtest_cases(int idtest_cases) {
        this.idtest_cases = idtest_cases;
    }

    public String getTest_desc() {
        return test_desc;
    }

    public void setTest_desc(String test_desc) {
        this.test_desc = test_desc;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(String dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getTestCaseName() {
        return testCaseName;
    }

    public void setTestCaseName(String testCaseName) {
        this.testCaseName = testCaseName;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getSmartContractID() {
        return smartContractID;
    }

    public void setSmartContractID(String smartContractID) {
        this.smartContractID = smartContractID;
    }

    // public List<Integer> getUserID() {
    //     return userID;
    // }

    // public void setUserID(List<Integer> userID) {
    //     this.userID = userID;
    // }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    // Method to get usernames of assigned users
    public List<String> getUsernames() {
        return userIDs.stream()
                .map(userId -> {
                    ManageUser user = manageUserService.getUserById(userId.getUserId());
                    return (user != null) ? user.getUsername() : "";
                })
                .collect(Collectors.toList());
    }

    public void resetUserStatuses() {
        this.userStatuses.clear();
    }

   public String determineOverallStatus() {
        boolean allApproved = true;
        boolean anyRejected = false;

        for (TestCaseUserStatus userStatus : userStatuses) {
            if ("Rejected".equals(userStatus.getStatus())) {
                anyRejected = true;
                break;
            } else if (!"Approved".equals(userStatus.getStatus())) {
                allApproved = false;
            }
        }

        if (anyRejected) {
            return "Rejected";
        } else if (allApproved) {
            return "Approved";
        } else {
            return "Pending";
        }
    }

    public String getOverallStatus() {
        return overallStatus;
    }

    public void setOverallStatus(String overallStatus) {
        this.overallStatus = overallStatus;
    }
}