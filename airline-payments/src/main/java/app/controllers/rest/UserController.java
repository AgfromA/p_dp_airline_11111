package app.controllers.rest;

import app.controllers.api.UserControllerApi;
import app.dto.UserDTO;
import app.mappers.UserMapper;
import app.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController implements UserControllerApi {

    private final UserService userService;
    private final UserMapper userMapper;
    @Override
    public ResponseEntity<Page> getAllUsersPages(Integer page, Integer size) {
        log.info("getAll: get all Accounts");
        var users = userService.getAllUser(page, size);
        return users.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(users, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<UserDTO> getUserDTOById(Long id) {
        log.info("getById: get Account by id. id = {}", id);
        var user = userService.getUserById(id);
        return user.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(new UserDTO(user.get()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<UserDTO> createUserDTO(UserDTO userDTO)  {
        log.info("create: create new Account with email={}", userDTO.getEmail());
        return ResponseEntity.ok(new UserDTO(userService.saveUser(userMapper.convertToUser(userDTO))));
    }

    @Override
    public ResponseEntity<UserDTO> updateUserDTOById(Long id, UserDTO userDTO)  {
        log.info("update: update Account with id = {}", id);
        return new ResponseEntity<>(new UserDTO( userService.updateUser(id,
                userMapper.convertToUser(userDTO))), HttpStatus.OK);
    }
}
