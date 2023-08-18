package app.service.interfaces;

import app.entities.account.Account;
import app.entities.account.User;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface UserService {
    User saveUser(User user);

    User updateUser(Long id, User user);

    Page<User> getAllUser(Integer page, Integer size);

    Optional<User> getUserById(Long id);

}
