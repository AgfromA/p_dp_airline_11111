package app.services;

import app.dto.AccountDTO;
import app.dto.RoleDTO;
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
    public RoleDTO getRoleByName(String name) {
        return roleMapper.convertToRoleDTO(roleRepository.findByName(name));
    }

    @Override
    public Set<RoleDTO> saveRolesToUser(AccountDTO user) {
        Set<RoleDTO> userRoles = new HashSet<>();
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
    public void saveRole(RoleDTO role) {
        roleRepository.save(roleMapper.convertToRole(role));
    }

    @Override
    public Set<RoleDTO> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(roleMapper::convertToRoleDTO)
                .collect(Collectors.toSet());
    }

}
