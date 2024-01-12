package app.services;

import app.account.Account;
import app.account.Role;
import app.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class RoleServiceImpl {

    private final RoleRepository roleRepository;

    @Transactional(readOnly = true)
    public Role getRoleByName(String name) {
        return roleRepository.findByName(name);
    }

    public Set<Role> saveRolesToUser(Account user) {
        Set<Role> userRoles = new HashSet<>();
        user.getRoles().stream().forEach(a -> {
            var roleFromDb = getRoleByName(a.getName());
            if (roleFromDb == null) {
                throw new RuntimeException("role not found");
            }
            userRoles.add(roleFromDb);
        });
        return userRoles;
    }

    public void saveRole(Role role) {
        roleRepository.save(role);
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }
}