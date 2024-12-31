package demo_ver.demo.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.userdetails.User;

import demo_ver.demo.service.ManageUserService;

public class TestSuite {
    private String id;
    private String name;
    private String description;
    private String status; // Overall status of the suite (e.g., Active, Inactive)
    private String importance; // Importance level (e.g., High, Medium, Low)
    private List<Integer> assignedUserIds = new ArrayList<>(); // List of assigned user IDs
    private Map<Integer, String> userStatuses = new HashMap<>(); // Map of user IDs to their statuses
    private TestPlan testPlan;
    private Build build;
    //private List<Integer> userID;
    private ManageUser user;
    
    private List<TestPlan> testPlans = new ArrayList<>();
    private List<Build> builds = new ArrayList<>();

    // Constructor
    public TestSuite(String id, String name, String description /*,List<Integer> userID*/) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = "Pending"; // Default status when created
        this.importance = "Medium"; // Default importance level
        //this.userID = userID;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImportance() {
        return importance;
    }

    public void setImportance(String importance) {
        this.importance = importance;
    }

    public List<Integer> getAssignedUserIds() {
        return assignedUserIds;
    }

    /*
     * public void setAssignedUserIds(List<String> userIds) {
     * this.assignedUserIds = userIds;
     * }
     */

    public Map<Integer, String> getUserStatuses() {
        return userStatuses;
    }

    public void setUserStatuses(Map<Integer, String> userStatuses) {
        this.userStatuses = userStatuses;
    }

    public TestPlan getTestPlan() {
        return testPlan;
    }

    public void setTestPlan(TestPlan testPlan) {
        this.testPlan = testPlan;
    }

    public Build getBuilds() {
        return build;
    }

    public void setBuilds(Build build) {
        this.build = build;
    }

    public ManageUser getUser() {
        return user;
    }

    public void setUser(ManageUser user){
        this.user = user;
    }

    // Assign a user to the test suite
    public void assignUser(Integer userId) {
        if (!assignedUserIds.contains(userId)) {
            assignedUserIds.add(userId);
            userStatuses.put(userId, "Pending"); // Default status for a newly assigned user
        }
    }

    // Unassign a user from the test suite
    public void unassignUser(Integer userId) {
        assignedUserIds.remove(userId);
        userStatuses.remove(userId); // Remove the user's status as well
    }

    // Update the status of a specific user
    public void updateUserStatus(Integer userId, String status) {
        if (userStatuses.containsKey(userId)) {
            userStatuses.put(userId, status);
        } else {
            throw new IllegalArgumentException("User ID not assigned to the test suite");
        }
    }

    // Check if all users have a specific status (e.g., Completed)
    public boolean areAllUsersStatus(String status) {
        return userStatuses.values().stream().allMatch(s -> s.equals(status));
    }

    // Update overall status based on user statuses
    public void updateStatus() {
        if (areAllUsersStatus("Completed")) {
            status = "Completed";
        } else if (userStatuses.values().stream().anyMatch(s -> s.equals("In Progress"))) {
            status = "In Progress";
        } else {
            status = "Pending";
        }
    }

    // Debugging utility
    @Override
    public String toString() {
        return "TestSuite{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", importance='" + importance + '\'' +
                ", assignedUserIds=" + assignedUserIds +
                ", userStatuses=" + userStatuses +
                '}';
    }

    public void setAssignedUserIds(List<String> userIds) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setAssignedUserIds'");
    }

    public List<TestPlan> getAssignedTestPlans() {
        return testPlans; // Getter for assignedTestPlans
    }

    public void setAssignedTestPlans(List<TestPlan> assignedTestPlans) {
        this.testPlans = assignedTestPlans; // Assuming you store the assigned test plans in a list called 'testPlans'
    }

    public List<Build> getAssignedBuilds() {
        return builds; // Getter for assignedTestPlans
    }

    public void setAssignedBuilds(List<Build> assignedBuilds) {
        this.builds = assignedBuilds; // Assuming you store the assigned test plans in a list called 'testPlans'
    }

}