package app.services;

import app.account.Account;
import app.repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import app.services.interfaces.AccountService;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Transactional(readOnly = true)
    @Override
    public Account getAccountByEmail(String email) {
        return accountRepository.getAccountByEmail(email);
    }
}