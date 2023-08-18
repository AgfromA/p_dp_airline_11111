package app.controllers.api;

import app.dto.UserDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Api(tags = "User REST")
@Tag(name = "User REST", description = "API для операций с пользователем")
@RequestMapping("/api/users")
public interface UserControllerApi {
    @GetMapping
    @ApiOperation(value = "Get list of all Users")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Users found"),
            @ApiResponse(code = 204, message = "Users not found")})
    ResponseEntity<Page> getAllUsersPages(@PageableDefault(sort = {"id"})
                                             @RequestParam(value = "page", defaultValue = "0") @Min(0) Integer page,
                                             @RequestParam(value = "size", defaultValue = "10") @Min(1) @Max(10) Integer size);

    @GetMapping("/{id}")
    @ApiOperation(value = "Get User by \"id\"")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User found"),
            @ApiResponse(code = 404, message = "User not found")})
    ResponseEntity<UserDTO> getUserDTOById(
            @ApiParam(
                    name = "id",
                    value = "User.id"
            )
            @PathVariable Long id);

    @PostMapping
    @ApiOperation(value = "Create User")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "User created")})
    ResponseEntity<UserDTO> createUserDTO(
            @ApiParam(
                    name = "user",
                    value = "User model"
            )
            @RequestBody @Valid UserDTO userDTO);

    @PatchMapping("/{id}")
    @ApiOperation(value = "Update User by \"id\"")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "USer updated"),
            @ApiResponse(code = 404, message = "User not found")})
    ResponseEntity<UserDTO> updateUserDTOById(
            @ApiParam(
                    name = "id",
                    value = "User.id"
            )
            @PathVariable("id") Long id,
            @ApiParam(
                    name = "userDTO",
                    value = "User model"
            )
            @RequestBody UserDTO userDTO);
}
