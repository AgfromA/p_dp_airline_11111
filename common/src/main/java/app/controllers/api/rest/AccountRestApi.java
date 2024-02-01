package app.controllers.api.rest;

import app.dto.AccountDto;
import app.dto.RoleDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

@Api(tags = "Account REST")
@Tag(name = "Account REST", description = "API для операций с пользователем")
public interface AccountRestApi {

    @RequestMapping(value = "/api/accounts", method = RequestMethod.GET)
    @ApiOperation(value = "Get list of all Accounts")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Accounts found"),
            @ApiResponse(code = 204, message = "Accounts not found")})
    ResponseEntity<Page<AccountDto>> getAllAccounts(@ApiParam(name = "page")
                                                    @RequestParam(value = "page", required = false) Integer page,
                                                    @ApiParam(name = "size")
                                                    @RequestParam(value = "size", required = false) Integer size);

    @RequestMapping(value = "/api/accounts/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Get Account by \"id\"")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Account found"),
            @ApiResponse(code = 404, message = "Account not found")})
    ResponseEntity<AccountDto> getAccount(
            @ApiParam(
                    name = "id",
                    value = "Account.id"
            )
            @PathVariable(value = "id") Long id);

    @RequestMapping(value = "/api/accounts/auth", method = RequestMethod.GET)
    @ApiOperation(value = "Get authenticated Account")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Account found")})
    ResponseEntity<AccountDto> getAuthenticatedAccount(HttpServletRequest request);

    @RequestMapping(value = "/api/accounts", method = RequestMethod.POST)
    @ApiOperation(value = "Create Account")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Account created"),
            @ApiResponse(code = 500, message = "Server error")})
    ResponseEntity<AccountDto> createAccount(
            @ApiParam(
                    name = "account",
                    value = "Account model"
            )
            @RequestBody
            AccountDto accountDTO);

    @RequestMapping(value = "/api/accounts/{id}", method = RequestMethod.PATCH)
    @ApiOperation(value = "Update Account by \"id\"")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Account updated"),
            @ApiResponse(code = 404, message = "Account not found")})
    ResponseEntity<AccountDto> updateAccount(
            @ApiParam(
                    name = "id",
                    value = "Account.id"
            )
            @PathVariable(value = "id") Long id,
            @ApiParam(
                    name = "account",
                    value = "Account"
            )
            @RequestBody
            AccountDto accountDTO);

    @RequestMapping(value = "/api/accounts/{id}", method = RequestMethod.DELETE)
    @ApiOperation(value = "Delete Account by \"id\"")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Account deleted"),
            @ApiResponse(code = 404, message = "Account not found")})
    ResponseEntity<Void> deleteAccount(
            @ApiParam(
                    name = "id",
                    value = "Account.id"
            )
            @PathVariable(value = "id") Long id);

    @RequestMapping(value = "/api/accounts/roles", method = RequestMethod.GET)
    @ApiOperation(value = "Get all existing roles")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Roles found"),
            @ApiResponse(code = 204, message = "No Role saved")})
    ResponseEntity<Set<RoleDto>> getAllRoles();
}