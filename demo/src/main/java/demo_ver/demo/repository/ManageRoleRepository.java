package demo_ver.demo.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import demo_ver.demo.model.ManageRole;

@Repository
public interface ManageRoleRepository extends CrudRepository<ManageRole, Integer> {
    // Custom query methods (if needed) can be defined here
    boolean existsByRoleName(String roleName);
    boolean existsByRoleNameAndRoleIDNot(String roleName, int roleId);
}
