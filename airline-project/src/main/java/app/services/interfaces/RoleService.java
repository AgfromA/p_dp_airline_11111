package app.services.interfaces;

import app.dto.AccountDto;
import app.dto.RoleDto;

import java.util.Set;

public interface RoleService {
    RoleDto getRoleByName(String name);

    Set<RoleDto> saveRolesToUser(AccountDto user);

    void saveRole(RoleDto role);

    Set<RoleDto> getAllRoles();
}
