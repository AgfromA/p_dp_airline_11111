package app.controllers.api.rest;

import app.dto.AccountDto;
import app.dto.AccountUpdateDto;
import app.dto.RoleDto;
import io.swagger.annotations.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

@Api(tags = "Account API")
@Tag(name = "Account API", description = "API для операций с пользователем сайта")
public interface AccountRestApi {

    @RequestMapping(value = "/api/accounts", method = RequestMethod.GET)
    @Operation(summary = "Получение всех сущностей с пагинацией/без пагинации")
    ResponseEntity<Page<AccountDto>> getAllAccounts(
            @Parameter(description = "Номер страницы") @RequestParam(value = "page", required = false) Integer page,
            @Parameter(description = "Количество элементов на странице") @RequestParam(value = "size", required = false) Integer size);

    @RequestMapping(value = "/api/accounts/{id}", method = RequestMethod.GET)
    @Operation(summary = "Получение сущности")
    ResponseEntity<AccountDto> getAccount(@Parameter(description = "ID сущности") @PathVariable Long id);

    @RequestMapping(value = "/api/accounts", method = RequestMethod.POST)
    @Operation(summary = "Создание сущности")
    ResponseEntity<AccountDto> createAccount(@Parameter(description = "Пользователь") @Valid @RequestBody AccountDto accountDTO);

    @RequestMapping(value = "/api/accounts/{id}", method = RequestMethod.PATCH)
    @Operation(summary = "Изменение сущности")
    ResponseEntity<AccountUpdateDto> updateAccount(
            @Parameter(description = "ID сущности") @PathVariable Long id,
            @Parameter(description = "Пользователь") @Valid @RequestBody AccountUpdateDto accountDTO);

    @RequestMapping(value = "/api/accounts/{id}", method = RequestMethod.DELETE)
    @Operation(summary = "Удаление сущности")
    ResponseEntity<Void> deleteAccount(@Parameter(description = "ID сущности") @PathVariable Long id);

    @RequestMapping(value = "/api/accounts/roles", method = RequestMethod.GET)
    @Operation(summary = "Получение всех возможных ролей пользователя сайта")
    ResponseEntity<Set<RoleDto>> getAllRoles();
}