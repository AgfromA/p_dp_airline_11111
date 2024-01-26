package app.services;

import app.dto.AccountDto;
import app.mappers.AccountMapper;
import app.repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder encoder;
    private final RoleService roleService;
    private final AccountMapper accountMapper;

    public List<AccountDto> findAll() {
        return accountMapper.toDtoList(accountRepository.findAll());
    }

    public AccountDto saveAccount(AccountDto accountDTO) {
        accountDTO.setPassword(encoder.encode(accountDTO.getPassword()));
        accountDTO.setRoles(roleService.saveRolesToUser(accountDTO));
        if (accountDTO.getAnswerQuestion() != null) {
            accountDTO.setAnswerQuestion(encoder.encode(accountDTO.getAnswerQuestion()));
        }
        var account = accountMapper.toEntity(accountDTO);
        return accountMapper.toDto(accountRepository.saveAndFlush(account));
    }

    public Optional<AccountDto> updateAccount(Long id, AccountDto accountDTO) {
        var optionalSavedAccount = getAccountById(id);
        AccountDto savedAccount;
        if (optionalSavedAccount.isEmpty()) {
            return optionalSavedAccount;
        } else {
            savedAccount = optionalSavedAccount.get();
        }
        savedAccount.setFirstName(accountDTO.getFirstName());
        savedAccount.setLastName(accountDTO.getLastName());
        savedAccount.setBirthDate(accountDTO.getBirthDate());
        savedAccount.setPhoneNumber(accountDTO.getPhoneNumber());
        savedAccount.setEmail(accountDTO.getEmail());
        savedAccount.setRoles(roleService.saveRolesToUser(accountDTO));
        savedAccount.setSecurityQuestion(accountDTO.getSecurityQuestion());
        if (!accountDTO.getPassword().equals(savedAccount.getPassword())) {
            savedAccount.setPassword(encoder.encode(accountDTO.getPassword()));
        }
        if (!accountDTO.getAnswerQuestion().equals(savedAccount.getAnswerQuestion())) {
            savedAccount.setAnswerQuestion(encoder.encode(accountDTO.getAnswerQuestion()));
        }
        return Optional.of(accountMapper
                .toDto(accountRepository
                        .save(accountMapper.toEntity(savedAccount))));
    }

    @Transactional(readOnly = true)
    public Page<AccountDto> getPage(Integer page, Integer size) {
        var pageRequest = PageRequest.of(page, size);
        return accountRepository.findAll(pageRequest).map(accountMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Optional<AccountDto> getAccountById(Long id) {
        return accountRepository.findById(id).map(accountMapper::toDto);
    }

    @Transactional(readOnly = true)
    public AccountDto getAccountByEmail(String email) {
        return accountMapper.toDto(accountRepository.getAccountByEmail(email));
    }

    public Optional<AccountDto> deleteAccountById(Long id) {
        var optionalSavedAccount = getAccountById(id);
        if (optionalSavedAccount.isEmpty()) {
            return optionalSavedAccount;
        } else {
            accountRepository.deleteById(id);
            return optionalSavedAccount;
        }
    }
}