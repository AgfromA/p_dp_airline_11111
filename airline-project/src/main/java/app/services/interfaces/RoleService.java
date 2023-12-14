package app.services.interfaces;

import app.dto.AccountDTO;
import app.dto.RoleDTO;

import java.util.Set;

public interface RoleService {
    RoleDTO getRoleByName(String name);

    Set<RoleDTO> saveRolesToUser(AccountDTO user);

    void saveRole(RoleDTO role);

    Set<RoleDTO> getAllRoles();
}
