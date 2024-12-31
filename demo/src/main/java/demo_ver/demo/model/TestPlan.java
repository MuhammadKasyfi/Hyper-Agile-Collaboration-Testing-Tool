package demo_ver.demo.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TestPlan {
    private String id;
    private String name;
    private String description;
    private String isActive; // Changed from Boolean to String
    private String isPublic; // Changed from Boolean to String
    private List<TestSuite> testSuites;

    // Constructor
    public TestPlan(String id, String name, String description, String isActive, String isPublic) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.isActive = isActive != null ? isActive : "false";
        this.isPublic = isPublic != null ? isPublic : "false";
        this.testSuites = new ArrayList<>(); // Initialize the testSuites list
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

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive != null ? isActive : "false";
    }

    public String getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(String isPublic) {
        this.isPublic = isPublic != null ? isPublic : "false";
    }

    public List<TestSuite> getTestSuites() {
        return testSuites;
    }

    public void setTestSuites(List<TestSuite> testSuites) {
        this.testSuites = testSuites != null ? testSuites : new ArrayList<>();
    }

    // Method to add a single TestSuite to the list
    public void addTestSuite(TestSuite testSuite) {
        if (this.testSuites == null) {
            this.testSuites = new ArrayList<>(); // Initialize if null
        }
        if (!this.testSuites.contains(testSuite)) { // Avoid duplicates
            this.testSuites.add(testSuite);
        }
    }

    // Method to remove a TestSuite from the list
    public void removeTestSuite(TestSuite testSuite) {
        if (this.testSuites != null) {
            this.testSuites.remove(testSuite);
        }
    }

    // Overriding equals() and hashCode() for proper comparison and avoiding duplicates
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        TestPlan testPlan = (TestPlan) obj;
        return Objects.equals(id, testPlan.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // Overriding toString() for better debugging and logging
    @Override
    public String toString() {
        return "TestPlan{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", isActive='" + isActive + '\'' +
                ", isPublic='" + isPublic + '\'' +
                ", testSuites=" + testSuites +
                '}';
    }
}
