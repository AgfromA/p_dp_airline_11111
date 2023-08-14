package app.services;

import app.entities.account.Account;
import app.repositories.AccountRepository;
import app.services.interfaces.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder encoder;
    private final RoleServiceImpl roleService;

    @Override
    public Account saveAccount(Account account) {
        account.setPassword(encoder.encode(account.getPassword()));
        account.setRoles(roleService.saveRolesToUser(account));
        if (account.getAnswerQuestion() != null) {
            account.setAnswerQuestion(encoder.encode(account.getAnswerQuestion()));
        }
        return accountRepository.saveAndFlush(account);
    }

    @Override
    public Account updateAccount(Long id, Account account) {
        var editAccount = accountRepository.getAccountById(id);
        if (!account.getPassword().equals(editAccount.getPassword())) {
            editAccount.setPassword(encoder.encode(account.getPassword()));
        }
        if (!account.getAnswerQuestion()
                .equals(accountRepository.findById(id).orElse(null).getAnswerQuestion())) {
            editAccount.setAnswerQuestion(encoder.encode(account.getAnswerQuestion()));
        }

        editAccount.setRoles(roleService.saveRolesToUser(account));
        editAccount.setEmail(account.getEmail());
        editAccount.setFirstName(account.getFirstName());
        editAccount.setLastName(account.getLastName());
        editAccount.setBirthDate(account.getBirthDate());
        editAccount.setPhoneNumber(account.getPhoneNumber());

        return accountRepository.saveAndFlush(editAccount);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Account> getAllAccounts(Integer page, Integer size) {
        return accountRepository.findAll(PageRequest.of(page, size));
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Account> getAccountById(Long id) {
        return accountRepository.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public Account getAccountByEmail(String email) {
        return accountRepository.getAccountByEmail(email);
    }

    @Override
    public void deleteAccountById(Long id) {
        accountRepository.deleteById(id);
    }
}
