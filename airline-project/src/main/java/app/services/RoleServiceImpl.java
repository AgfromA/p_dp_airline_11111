package app.services;

import app.dto.AccountDto;
import app.dto.RoleDto;
import app.mappers.RoleMapper;
import app.repositories.RoleRepository;
import app.services.interfaces.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Override
    @Transactional(readOnly = true)
    public RoleDto getRoleByName(String name) {
        return roleMapper.convertToRoleDto(roleRepository.findByName(name));
    }

    @Override
    public Set<RoleDto> saveRolesToUser(AccountDto user) {
        Set<RoleDto> userRoles = new HashSet<>();
        user.getRoles().stream().forEach(a -> {
            var roleFromDb = getRoleByName(a.getName());
            if (roleFromDb == null) {
                throw new RuntimeException("role not found");
            }
            userRoles.add(roleFromDb);
        });
        return userRoles;
    }
    @Override
    public void saveRole(RoleDto role) {
        roleRepository.save(roleMapper.convertToRole(role));
    }

    @Override
    public Set<RoleDto> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(roleMapper::convertToRoleDto)
                .collect(Collectors.toSet());
    }
}