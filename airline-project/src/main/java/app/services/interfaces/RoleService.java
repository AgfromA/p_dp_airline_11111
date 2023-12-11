package app.services.interfaces;

import app.entities.Role;

import java.util.List;

public interface RoleService {
    Role getRoleByName(String name);

    void saveRole(Role role);

    List<Role> getAllRoles();
}
