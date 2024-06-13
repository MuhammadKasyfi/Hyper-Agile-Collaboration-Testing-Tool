package demo_ver.demo.dto;

//Data Transfer Object for user class to get role name
public class UserWithRoleDTO {
    private int userID;
    private String email;
    private String username;
    private String roleName;

    public UserWithRoleDTO(int userID, String email, String username, String roleName) {
        this.userID = userID;
        this.email = email;
        this.username = username;
        this.roleName = roleName;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}