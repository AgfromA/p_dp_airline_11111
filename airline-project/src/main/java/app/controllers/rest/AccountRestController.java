package app.controllers.rest;

import app.controllers.api.rest.AccountRestApi;
import app.dto.AccountDto;
import app.dto.RoleDto;
import app.services.AccountService;
import app.services.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AccountRestController implements AccountRestApi {

    private final AccountService accountService;
    private final RoleService roleService;

    @Override
    public ResponseEntity<Page<AccountDto>> getAllAccounts(Integer page, Integer size) {
        log.info("getAllAccounts:");
        if (page == null || size == null) {
            return createUnPagedResponse();
        }
        var accounts = accountService.getAllAccounts(page, size);
        return accounts.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : ResponseEntity.ok(accounts);
    }

    private ResponseEntity<Page<AccountDto>> createUnPagedResponse() {
        var account = accountService.getAllAccounts();
        if (account.isEmpty()) {
            return ResponseEntity.ok(new PageImpl<>(new ArrayList<>(account)));
        } else {
            log.info("getAllAccounts: count {}", account.size());
            return ResponseEntity.ok(new PageImpl<>(new ArrayList<>(account)));
        }
    }

    @Override
    public ResponseEntity<AccountDto> getAccount(Long id) {
        log.info("getAccount: by id: {}", id);
        var account = accountService.getAccountById(id);
        return account.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }


    @Override
    public ResponseEntity<AccountDto> createAccount(AccountDto accountDTO) {
        log.info("createAccount:");
        return new ResponseEntity<>(accountService.createAccount(accountDTO), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<AccountDto> updateAccount(Long id, AccountDto accountDTO) {
        log.info("updateAccount: by id: {}", id);
        return ResponseEntity.ok(accountService.updateAccount(id, accountDTO));
    }

    @Override
    public ResponseEntity<Void> deleteAccount(Long id) {
        log.info("deleteAccount: by id: {}", id);
        accountService.deleteAccount(id);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Set<RoleDto>> getAllRoles() {
        var allRolesFromDb = roleService.getAllRoles();
        if (allRolesFromDb.isEmpty()) {
            return new ResponseEntity<>(Collections.emptySet(), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(allRolesFromDb, HttpStatus.OK);
    }
}