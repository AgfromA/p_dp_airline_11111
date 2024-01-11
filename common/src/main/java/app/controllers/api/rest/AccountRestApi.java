package app.controllers.api.rest;

import app.dto.AccountDTO;
import app.dto.RoleDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;

@Api(tags = "Account REST")
@Tag(name = "Account REST", description = "API для операций с пользователем")
public interface AccountRestApi {

    @RequestMapping(value = "/api/accounts", method = RequestMethod.GET)
    @ApiOperation(value = "Get list of all Accounts")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Accounts found"),
            @ApiResponse(code = 204, message = "Accounts not found")})
    ResponseEntity<List<AccountDTO>> getPage(@ApiParam(name = "page")
                                             @RequestParam(value = "page", required = false) Integer page,
                                             @ApiParam(name = "size")
                                             @RequestParam(value = "size", required = false) Integer size);

    @RequestMapping(value = "/api/accounts/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Get Account by \"id\"")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Account found"),
            @ApiResponse(code = 404, message = "Account not found")})
    ResponseEntity<AccountDTO> getAccountDTOById(
            @ApiParam(
                    name = "id",
                    value = "Account.id"
            )
            @PathVariable(value = "id") Long id);

    @RequestMapping(value = "/api/accounts/auth", method = RequestMethod.GET)
    @ApiOperation(value = "Get authenticated Account")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Account found")})
    ResponseEntity<AccountDTO> getAuthenticatedAccount(HttpServletRequest request);

    @RequestMapping(value = "/api/accounts", method = RequestMethod.POST)
    @ApiOperation(value = "Create Account")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Account created"),
            @ApiResponse(code = 500, message = "Server error")})
    ResponseEntity<AccountDTO> createAccountDTO(
            @ApiParam(
                    name = "account",
                    value = "Account model"
            )
            @RequestBody
            //@Valid fix it
            AccountDTO accountDTO);

    @RequestMapping(value = "/api/accounts/{id}", method = RequestMethod.PATCH)
    @ApiOperation(value = "Update Account by \"id\"")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Account updated"),
            @ApiResponse(code = 404, message = "Account not found")})
    ResponseEntity<AccountDTO> updateAccountDTOById(
            @ApiParam(
                    name = "id",
                    value = "Account.id"
            )
            @PathVariable(value = "id") Long id,
            @ApiParam(
                    name = "account",
                    value = "AccountDto"
            )
            @RequestBody
            //@Valid
            AccountDTO accountDTO);

    @RequestMapping(value = "/api/accounts/{id}", method = RequestMethod.DELETE)
    @ApiOperation(value = "Delete Account by \"id\"")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Account deleted"),
            @ApiResponse(code = 404, message = "Account not found")})
    ResponseEntity<Void> deleteAccountById(
            @ApiParam(
                    name = "id",
                    value = "Account.id"
            )
            @PathVariable(value = "id") Long id);

    @RequestMapping(value = "/api/accounts/all-roles", method = RequestMethod.GET)
    @ApiOperation(value = "Get all existing roles")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Roles found"),
            @ApiResponse(code = 204, message = "No Role saved")})
    ResponseEntity<Set<RoleDTO>> getAllRoles();
}