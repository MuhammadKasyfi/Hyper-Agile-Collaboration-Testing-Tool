package demo_ver.demo.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import demo_ver.demo.model.ManageRole;
import demo_ver.demo.repository.ManageRoleRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class ManageRoleService {

    @Autowired
    private ManageRoleRepository manageRoleRepository;

    public ManageRoleService(ManageRoleRepository manageRoleRepository) {
        this.manageRoleRepository = manageRoleRepository;
    }

    public List<ManageRole> getAllRoles() {
        return StreamSupport
                .stream(manageRoleRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public List<ManageRole> apiGetAllRoles() {
        return getAllRoles();
    }

    // public void addRole(ManageRole newRole) {
    // if (!manageRoleRepository.existsById(newRole.getRoleID())) {
    // manageRoleRepository.save(newRole);
    // } else {
    // System.out.println("Role with roleID " + newRole.getRoleID() + " already
    // exists.");
    // }
    // }

    public void apiAddRole(ManageRole manageRole) {
        try {
            // Ensure role name is prefixed with "ROLE_"
            String roleName = "ROLE_" + manageRole.getRoleName();

            if (manageRoleRepository.existsByRoleName(roleName)) {
                System.out.println("Role with roleName " + roleName + " already exists.");
                return;
            }

            if (manageRoleRepository.existsById(manageRole.getRoleID())) {
                System.out.println("Role with roleID " + manageRole.getRoleID() + " already exists.");
                return;
            }
            // Assign the prefixed roleName back to the ManageRole object
            manageRole.setRoleName(roleName);
            manageRoleRepository.save(manageRole);
            System.out.println("Role added successfully: " + manageRole);
        } catch (RestClientException e) {
            System.out.println("Failed to add role due to exception: " + e.getMessage());
        }
    }

    public ManageRole apiFindById(int id) {
        Optional<ManageRole> role = manageRoleRepository.findById(id);
        return role.orElse(null);
    }

    // public void deleteRole(int id) {
    // manageRoleRepository.deleteById(id);
    // }

    public void apiDeleteRole(int id) {
        try {
            if (manageRoleRepository.existsById(id)) {
                manageRoleRepository.deleteById(id);
                System.out.println("Role with ID " + id + " deleted successfully.");
            } else {
                System.out.println("Failed to delete role with ID " + id + ". Role not found.");
            }
        } catch (RestClientException e) {
            System.out.println("Failed to delete role due to exception: " + e.getMessage());
        }
    }

    public ResponseEntity<String> apiUpdateManageRole(ManageRole manageRole) {
        try {
            String roleName = "ROLE_" + manageRole.getRoleName();
            boolean roleNameExists = manageRoleRepository.existsByRoleNameAndRoleIDNot(roleName,
                    manageRole.getRoleID());

            if (roleNameExists) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Role with roleName " + roleName + " already exists.");
            }
            manageRole.setRoleName(roleName);
            manageRoleRepository.save(manageRole);
            return ResponseEntity.ok("Role updated successfully.");
        } catch (RestClientException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update role: " + e.getMessage());
        }
    }

    public boolean isRoleNameExists(String roleName) {
        return manageRoleRepository.findAll().iterator().hasNext() &&
                StreamSupport.stream(manageRoleRepository.findAll().spliterator(), false)
                        .anyMatch(role -> role.getRoleName().equalsIgnoreCase("ROLE_" + roleName));
    }

    public String getRoleNameByIdString(int id) {
        Optional<ManageRole> role = manageRoleRepository.findById(id);
        return role.map(ManageRole::getRoleName).orElse(null);
    }
}
