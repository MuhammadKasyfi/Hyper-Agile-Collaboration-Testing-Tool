package demo_ver.demo.model;

public class Build {
    private String bId; // Unique Build ID
    private String buildTitle; // Title of the Build
    private String buildDescription; // Description of the Build
    private String isBuildActive; // Status: Active or Inactive
    private String isBuildOpen; // Status: Open or Closed
    private String buildReleaseDate; // Release Date
    private String buildVersion; // Version of the Build

    // Full constructor with all properties
    // Constructor
    public Build(String bId, String buildTitle, String buildDescription, String buildReleaseDate,
            String isBuildActive, String isBuildOpen, String buildVersion) {
        this.bId = bId;
        this.buildTitle = buildTitle;
        this.buildDescription = buildDescription;
        this.buildReleaseDate = buildReleaseDate;
        this.isBuildActive = isBuildActive;
        this.isBuildOpen = isBuildOpen;
        this.buildVersion = buildVersion;
    }

    public Build() {
        // You can set default values here if necessary
        this.buildTitle = "";
        this.buildDescription = "";
        this.buildReleaseDate = "";
        this.isBuildActive = "Inactive"; // Default value
        this.isBuildOpen = "Closed"; // Default value
        this.buildVersion = "";
        // this.version = "1.0"; // Default version
    }
    // Getters and Setters
    public String getBId() {
        return bId;
    }

    public void setBId(String bId) {
        this.bId = bId;
    }

    public String getBuildTitle() {
        return buildTitle;
    }

    public void setBuildTitle(String buildTitle) {
        this.buildTitle = buildTitle;
    }

    public String getBuildDescription() {
        return buildDescription;
    }

    public void setBuildDescription(String buildDescription) {
        this.buildDescription = buildDescription;
    }

    public String getIsBuildActive() {
        return isBuildActive;
    }

    public void setIsBuildActive(String isBuildActive) {
        this.isBuildActive = isBuildActive;
    }

    public String getIsBuildOpen() {
        return isBuildOpen;
    }

    public void setIsBuildOpen(String isBuildOpen) {
        this.isBuildOpen = isBuildOpen;
    }

    public String getBuildReleaseDate() {
        return buildReleaseDate;
    }

    public void setBuildReleaseDate(String buildReleaseDate) {
        this.buildReleaseDate = buildReleaseDate;
    }

    public String getBuildVersion() {
        return buildVersion;
    }

    public void setBuildVersion(String buildVersion) {
        this.buildVersion = buildVersion;
    }

    public Object getFeatures() {
        // Placeholder method for features
        throw new UnsupportedOperationException("Unimplemented method 'getFeatures'");
    }
}
