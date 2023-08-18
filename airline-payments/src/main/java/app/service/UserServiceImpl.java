package app.service;

import app.entities.account.User;
import app.repositories.UserRepository;
import app.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    @Override
    public User saveUser(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepository.saveAndFlush(user);
    }

    @Override
    public User updateUser(Long id, User user) {
        var editUser = userRepository.getUserById(id);
        if (!user.getPassword().equals(editUser.getPassword())) {
            editUser.setPassword(encoder.encode(user.getPassword()));
        }
        editUser.setEmail(user.getEmail());
        editUser.setFirstName(user.getFirstName());
        editUser.setLastName(user.getLastName());
        return userRepository.saveAndFlush(editUser);
    }

    @Override
    public Page<User> getAllUser(Integer page, Integer size) {
        return userRepository.findAll(PageRequest.of(page, size));
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
}
