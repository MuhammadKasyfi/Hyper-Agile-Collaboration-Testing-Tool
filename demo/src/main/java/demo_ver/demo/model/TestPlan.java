package demo_ver.demo.model;

import java.util.ArrayList;
import java.util.List;

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
        this.isActive = isActive;
        this.isPublic = isPublic;
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

    public String getIsActive() { // Changed return type to String
        return isActive;
    }

    public void setIsActive(String isActive) { // Changed parameter type to String
        this.isActive = isActive;
    }

    public String getIsPublic() { // Changed return type to String
        return isPublic;
    }

    public void setIsPublic(String publicStatus) { // Changed parameter type to String
        this.isPublic = publicStatus;
    }

    public List<TestSuite> getTestSuites() {
        return getTestSuites();
    }

    public void setTestSuites(List<TestSuite> testSuites) {
        this.testSuites = testSuites;
    }

    public void addTestSuite(TestSuite testSuite) {
        if (this.testSuites == null) {
            this.testSuites = new ArrayList<>(); // Initialize if null
        }
        this.testSuites.add(testSuite);
    }
}
