package app.services;


import app.dto.RoleDto;
import app.mappers.RoleMapper;
import app.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public RoleDto getRoleByName(String name) {
        return roleMapper.toDto(roleRepository.findByName(name));
    }



   public Set<RoleDto> getRolesByName(Set<RoleDto> roles) {
        var userRoles = new HashSet<RoleDto>();
        roles.forEach(a -> {
            var roleFromDb = getRoleByName(a.getName());
            if (roleFromDb == null) {
                throw new EntityNotFoundException("role not found");
            }
            userRoles.add(roleFromDb);
        });
        return userRoles;
    }

    public Set<RoleDto> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(roleMapper::toDto)
                .collect(Collectors.toSet());
    }
}