package app.controllers.rest;

import app.controllers.api.rest.AccountRestApi;
import app.dto.AccountDto;
import app.dto.RoleDto;
import app.security.JwtProviderLite;
import app.services.AccountService;
import app.services.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AccountRestController implements AccountRestApi {

    private final AccountService accountService;
    private final RoleService roleService;
    private final JwtProviderLite jwtProvider;

    @Override
    public ResponseEntity<List<AccountDto>> getAllAccounts(Integer page, Integer size) {
        log.info("getAll: get all Accounts");
        if (page == null || size == null) {
            log.info("getAll: get all List Accounts");
            return createUnPagedResponse();
        }

        var accountPage = accountService.getPage(page, size);

        return accountPage.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(accountPage.getContent(), HttpStatus.OK);
    }

    private ResponseEntity<List<AccountDto>> createUnPagedResponse() {
        var account = accountService.findAll();
        if (account.isEmpty()) {
            log.info("getAll: Accounts not found");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            log.info("getAll: found {} Accounts", account.size());
            return new ResponseEntity<>(account, HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<AccountDto> getAccount(Long id) {
        log.info("getById: get Account by id. id = {}", id);
        var account = accountService.getAccountById(id);
        return account.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(account.get(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<AccountDto> getAuthenticatedAccount(HttpServletRequest request) {
        log.info("getAuthenticatedAccount: get currently authenticated Account");
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            String username = jwtProvider.extractClaims(token).get().getSubject();
            var authAccount = accountService.getAccountByEmail(username);
            return ResponseEntity.ok(authAccount);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @Override
    public ResponseEntity<AccountDto> createAccount(AccountDto accountDTO) {
        log.info("create: create new Account with email={}", accountDTO.getEmail());
        return ResponseEntity.ok((accountService.saveAccount(accountDTO)));
    }

    @Override
    public ResponseEntity<AccountDto> updateAccount(Long id, AccountDto accountDTO) {
        log.info("update: update Account with id = {}", id);
        return new ResponseEntity<>(accountService.updateAccount(id, accountDTO).get(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> deleteAccount(Long id) {
        log.info("deleteAircraftById: deleteAircraftById Account with id = {}", id);
        var user = accountService.getAccountById(id);
        if (user.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        accountService.deleteAccountById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Set<RoleDto>> getAllRoles() {
        var allRolesFromDb = roleService.getAllRoles();
        if (allRolesFromDb.isEmpty()) {
            return new ResponseEntity<>(Collections.emptySet(), HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(allRolesFromDb);
    }
}