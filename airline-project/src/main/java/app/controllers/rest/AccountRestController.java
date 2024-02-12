package app.controllers.rest;

import app.controllers.api.rest.AccountRestApi;
import app.dto.AccountDto;
import app.dto.RoleDto;
import app.security.JwtProviderLite;
import app.services.AccountService;
import app.services.RoleService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AccountRestController implements AccountRestApi {

    private final AccountService accountService;
    private final RoleService roleService;
    private final JwtProviderLite jwtProvider;

    @Override
    public ResponseEntity<Page<AccountDto>> getAllAccounts(Integer page, Integer size) {
        log.info("getAll: get all Accounts");
        if (page == null || size == null) {
            log.info("getAll: get all List Accounts");
            return createUnPagedResponse();
        }
        var accounts = accountService.getPage(page, size);
        return accounts.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : createPagedResponse(accounts);
    }

    private ResponseEntity<Page<AccountDto>> createUnPagedResponse() {
        var account = accountService.findAll();
        if (account.isEmpty()) {
            log.info("getAll: Accounts not found");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            log.info("getAll: found {} Accounts", account.size());
            return ResponseEntity.ok(new PageImpl<>(new ArrayList<>(account)));
        }
    }

    private ResponseEntity<Page<AccountDto>> createPagedResponse(Page<AccountDto> accountPage) {
        var accountDTOPage = new PageImpl<>(
                new ArrayList<>(accountPage.getContent()),
                accountPage.getPageable(),
                accountPage.getTotalElements()
        );
        return ResponseEntity.ok(accountDTOPage);
    }

    @Override
    public ResponseEntity<AccountDto> getAccount(Long id) {
        log.info("getAccount: by id: {}", id);
        var account = accountService.getAccountById(id);
        return account.map(accountDto -> new ResponseEntity<>(accountDto, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Override
    public ResponseEntity<AccountDto> getAuthenticatedAccount(HttpServletRequest request) {
        log.info("getAuthenticatedAccount: get currently authenticated Account");
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            Optional<Claims> claimsOptional = jwtProvider.extractClaims(token);
            if (claimsOptional.isPresent()) {
                String username = claimsOptional.get().getSubject();
                var authAccount = accountService.getAccountByEmail(username);
                return ResponseEntity.ok(authAccount);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @Override
    public ResponseEntity<AccountDto> createAccount(AccountDto accountDTO) {
        log.info("createAccount: with email: {}", accountDTO.getEmail());
        return new ResponseEntity<>(accountService.saveAccount(accountDTO), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<AccountDto> updateAccount(Long id, AccountDto accountDTO) {
        log.info("updateAccount: by id: {}", id);
        return new ResponseEntity<>(accountService.updateAccount(id, accountDTO), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> deleteAccount(Long id) {
        log.info("deleteAccount: by id: {}", id);
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
        return new ResponseEntity<>(allRolesFromDb, HttpStatus.OK);
    }
}