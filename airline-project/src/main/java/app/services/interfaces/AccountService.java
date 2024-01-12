package app.services.interfaces;

import app.dto.AccountDto;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface AccountService {

    List<AccountDto> findAll();

    AccountDto saveAccount(AccountDto accountDTO);

    Optional<AccountDto> updateAccount(Long id, AccountDto accountDTO);

    Page<AccountDto> getPage(Integer page, Integer size);

    Optional<AccountDto> getAccountById(Long id);

    AccountDto getAccountByEmail(String email);

    Optional<AccountDto> deleteAccountById(Long id);
}