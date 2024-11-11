package demo_ver.demo.model;

public class TestPlan {
    private Long id;
    private String name;
    private String description;
    private Boolean isActive;  // Can be used for "active" checkbox
    private Boolean isPublic;  // New field for "public" checkbox

    // Constructor, getters, setters, etc.
    public TestPlan(Long id, String name, String description, Boolean isActive, Boolean isPublic) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.isActive = isActive;
        this.isPublic = isPublic;  // Initialize the "public" field
    }

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

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

    public void setActive(boolean isActive2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setActive'");
    }
}
