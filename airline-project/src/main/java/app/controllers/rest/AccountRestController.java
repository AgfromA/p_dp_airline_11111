package app.controllers.rest;

import app.controllers.api.rest.AccountRestApi;
import app.dto.AccountDTO;;
import app.entities.account.Role;
import app.services.interfaces.AccountService;
import app.services.interfaces.RoleService;
import app.util.mappers.AccountMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MethodNotSupportedException;
import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AccountRestController implements AccountRestApi {

    private final AccountService accountService;
    private final RoleService roleService;
    private final AccountMapper accountMapper;

    @Override
    public ResponseEntity<Page> getAllAccountsPages(Pageable pageable) {
        log.info("getAll: get all Accounts");
        var users = accountService.getAllAccounts(pageable);
        return users.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(users, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<AccountDTO> getAccountDTOById(Long id) {
        log.info("getById: get Account by id. id = {}", id);
        var account = accountService.getAccountById(id);
        return account.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(new AccountDTO(account.get()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<AccountDTO> getAuthenticatedAccount() {
        log.info("getAuthenticatedAccount: get currently authenticated Account");
        var authAccount = accountService.getAccountByEmail(
                SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        return ResponseEntity.ok(new AccountDTO(authAccount));
    }

    @Override
    public ResponseEntity<AccountDTO> createAccountDTO(AccountDTO accountDTO)
            throws MethodNotSupportedException {
        log.info("create: create new Account with email={}", accountDTO.getEmail());
        return ResponseEntity.ok(new AccountDTO(accountService.saveAccount(accountMapper.convertToAccount(accountDTO))));
    }

    @Override
    public ResponseEntity<AccountDTO> updateAccountDTOById(Long id, AccountDTO accountDTO)
            throws MethodNotSupportedException {
        log.info("update: update Account with id = {}", id);
        return new ResponseEntity<>(new AccountDTO( accountService.updateAccount(id,
                accountMapper.convertToAccount(accountDTO))), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> deleteAccountById(Long id) {
        log.info("deleteAircraftById: deleteAircraftById Account with id = {}", id);
        var user = accountService.getAccountById(id);
        if (user.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        accountService.deleteAccountById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Role>> getAllRoles() {
        var allRolesFromDb = roleService.getAllRoles();
        if (allRolesFromDb.isEmpty()) {
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(allRolesFromDb);
    }
}