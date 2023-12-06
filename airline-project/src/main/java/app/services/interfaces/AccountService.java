package app.services.interfaces;

import app.dto.AccountDTO;
import app.entities.Account;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface AccountService {
    Account saveAccount(AccountDTO accountDTO);

    Account updateAccount(Long id, AccountDTO accountDTO);

    Page<Account> getAllAccounts(Integer page, Integer size);

    Optional<Account> getAccountById(Long id);

    Account getAccountByEmail(String email);

    void deleteAccountById(Long id);

}
