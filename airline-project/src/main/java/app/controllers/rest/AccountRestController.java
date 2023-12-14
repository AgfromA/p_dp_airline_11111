package app.controllers.rest;

import app.controllers.api.rest.AccountRestApi;
import app.dto.AccountDTO;
import app.dto.RoleDTO;
import app.mappers.AccountMapper;
import app.security.JwtProviderLite;
import app.services.interfaces.AccountService;
import app.services.interfaces.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AccountRestController implements AccountRestApi {

    private final AccountService accountService;
    private final RoleService roleService;
    private final JwtProviderLite jwtProvider;

    private final AccountMapper accountMapper = Mappers.getMapper(AccountMapper.class);

    @Override
    public ResponseEntity<Page<AccountDTO>> getPage(Integer page, Integer size) {
        log.info("getAll: get all Accounts");
        if (page == null || size == null) {
            return createUnPagedResponse();
        }
        if (page < 0 || size < 1) {
            return ResponseEntity.noContent().build();
        }

        var accountPage = accountService.getPage(page, size);
        if (accountPage.getContent().isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return createPagedResponse(accountPage);
        }
    }

    private ResponseEntity<Page<AccountDTO>> createUnPagedResponse() {
        var account = accountService.findAll();
        if (account.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(new PageImpl<>(account.stream().collect(Collectors.toList())));
    }

    private ResponseEntity<Page<AccountDTO>> createPagedResponse(Page<AccountDTO> accountPage) {
        var accountDTOPage = new PageImpl<>(
                accountPage.getContent().stream().collect(Collectors.toList()),
                accountPage.getPageable(),
                accountPage.getTotalElements()
        );
        return ResponseEntity.ok(accountDTOPage);
    }

    @Override
    public ResponseEntity<AccountDTO> getAccountDTOById(Long id) {
        log.info("getById: get Account by id. id = {}", id);
        var account = accountService.getAccountById(id);
        return account.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(account.get(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<AccountDTO> getAuthenticatedAccount(HttpServletRequest request) {
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
    public ResponseEntity<AccountDTO> createAccountDTO(AccountDTO accountDTO) {
        log.info("create: create new Account with email={}", accountDTO.getEmail());
        return ResponseEntity.ok(accountMapper.convertToAccountDTO((accountService.saveAccount(accountDTO))));
    }

    @Override
    public ResponseEntity<AccountDTO> updateAccountDTOById(Long id, AccountDTO accountDTO) {
        log.info("update: update Account with id = {}", id);
        return new ResponseEntity<>( accountService.updateAccount(id,accountDTO).get(), HttpStatus.OK);
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
    public ResponseEntity<Set<RoleDTO>> getAllRoles() {
        var allRolesFromDb = roleService.getAllRoles();
        if (allRolesFromDb.isEmpty()) {
            return new ResponseEntity<>(Collections.emptySet(), HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(allRolesFromDb);
    }
}