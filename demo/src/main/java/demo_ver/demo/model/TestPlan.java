package demo_ver.demo.model;

public class TestPlan {
    private Long id;
    private String name;
    private String description;
    private String isActive;  // Changed from Boolean to String
    private String isPublic;  // Changed from Boolean to String

    // Constructor
    public TestPlan(Long id, String name, String description, String isActive, String isPublic) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.isActive = isActive;
        this.isPublic = isPublic;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public String getIsActive() {  // Changed return type to String
        return isActive;
    }

    public void setIsActive(String isActive) {  // Changed parameter type to String
        this.isActive = isActive;
    }

    public String getIsPublic() {  // Changed return type to String
        return isPublic;
    }

    public void setIsPublic(String isPublic) {  // Changed parameter type to String
        this.isPublic = isPublic;
    }
}
