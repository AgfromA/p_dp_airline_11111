package app.services.interfaces;

import app.account.Account;
import app.account.Role;

import java.util.List;
import java.util.Set;

public interface RoleService {
    Role getRoleByName(String name);

    void saveRole(Role role);

    List<Role> getAllRoles();

    Set<Role> saveRolesToUser(Account user);
}
