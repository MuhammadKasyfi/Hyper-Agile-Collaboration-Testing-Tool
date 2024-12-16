package demo_ver.demo.model;

public class Build {
    private String bId; // Unique Build ID
    private String testPlanId; // Reference to Test Plan ID
    private String buildTitle; // Title of the Build
    private String buildDescription; // Description of the Build
    private String isBuildActive; // Status: Active or Inactive
    private String isBuildOpen; // Status: Open or Closed
    private String buildReleaseDate; // Release Date
    private String copyTesterAssignmentsFlag; // Flag to copy tester assignments

    public Build(String bId, String testPlanId, String buildTitle, String buildDescription,
            String isBuildActive, String isBuildOpen, String buildReleaseDate,
            String copyTesterAssignmentsFlag) {
        this.bId = bId;
        this.testPlanId = testPlanId;
        this.buildTitle = buildTitle;
        this.buildDescription = buildDescription;
        this.isBuildActive = isBuildActive;
        this.isBuildOpen = isBuildOpen;
        this.buildReleaseDate = buildReleaseDate;
        this.copyTesterAssignmentsFlag = copyTesterAssignmentsFlag;
    }

    // Getters and Setters
    public String getBId() {
        return bId;
    }

    public void setBId(String bId) {
        this.bId = bId;
    }

    public String getTestPlanId() {
        return testPlanId;
    }

    public void setTestPlanId(String testPlanId) {
        this.testPlanId = testPlanId;
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

    public String getCopyTesterAssignmentsFlag() {
        return copyTesterAssignmentsFlag;
    }

    public void setCopyTesterAssignmentsFlag(String copyTesterAssignmentsFlag) {
        this.copyTesterAssignmentsFlag = copyTesterAssignmentsFlag;
    }
}
