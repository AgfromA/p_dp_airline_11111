package app.services.interfaces;

import app.dto.AccountDTO;
import app.entities.account.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface AccountService {
    Account saveAccount(AccountDTO accountDTO);

    Account updateAccount(Long id, AccountDTO accountDTO);

    Page<Account> getAllAccounts(Pageable pageable);

    Optional<Account> getAccountById(Long id);

    Account getAccountByEmail(String email);

    void deleteAccountById(Long id);

}
