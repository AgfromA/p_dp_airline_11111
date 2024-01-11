package app.services.interfaces;

import app.dto.AccountDTO;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface AccountService {

    List<AccountDTO> findAll();

    AccountDTO saveAccount(AccountDTO accountDTO);

    Optional<AccountDTO> updateAccount(Long id, AccountDTO accountDTO);

    Page<AccountDTO> getPage(Integer page, Integer size);

    Optional<AccountDTO> getAccountById(Long id);

    AccountDTO getAccountByEmail(String email);

    Optional<AccountDTO> deleteAccountById(Long id);
}