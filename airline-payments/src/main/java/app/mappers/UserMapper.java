package app.mappers;

import app.dto.UserDTO;
import app.entities.account.User;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserMapper {

    public User convertToUser(UserDTO userDTO) {
        var user = new User();
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setPassword(user.getPassword());

        return user;
    }
}
