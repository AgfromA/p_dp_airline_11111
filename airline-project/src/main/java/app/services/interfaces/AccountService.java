package app.services.interfaces;

import app.entities.account.Account;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface AccountService {
    Account saveAccount(Account account);

    Account updateAccount(Long id, Account account);

    Page<Account> getAllAccounts(Integer page, Integer size);

    Optional<Account> getAccountById(Long id);

    Account getAccountByEmail(String email);

    void deleteAccountById(Long id);

}
