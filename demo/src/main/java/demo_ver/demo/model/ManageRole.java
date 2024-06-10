package demo_ver.demo.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Table;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@EntityScan
@Entity
@Table(name = "manage_role")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ManageRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("roleID")
    private int roleID;

    @JsonProperty("description")
    private String description;

    @JsonProperty("roleName")
    private String roleName;

    public ManageRole() {
    }

    public ManageRole(String roleName, String description) {
        this.roleName = roleName;
        this.description = description;
    }

    public ManageRole(int roleID, String roleName, String description) {
        this.roleID = roleID;
        this.roleName = roleName;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getRoleID() {
        return roleID;
    }

    public void setRoleID(int roleID) {
        this.roleID = roleID;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    // For role-based authorization
    public List<GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(roleName));
        return authorities;
    }
}
