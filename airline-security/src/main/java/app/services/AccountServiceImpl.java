package app.services;

import app.account.Account;
import app.repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountServiceImpl {

    private final AccountRepository accountRepository;

    @Transactional(readOnly = true)
    public Account getAccountByEmail(String email) {
        return accountRepository.getAccountByEmail(email);
    }
}