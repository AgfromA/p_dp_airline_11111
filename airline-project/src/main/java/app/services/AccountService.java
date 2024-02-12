package app.services;

import app.dto.AccountDto;
import app.entities.Account;
import app.exceptions.DuplicateFieldException;
import app.exceptions.EntityNotFoundException;
import app.mappers.AccountMapper;
import app.mappers.RoleMapper;
import app.repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder encoder;
    private final RoleService roleService;
    private final AccountMapper accountMapper;
    private final RoleMapper roleMapper;

    public List<AccountDto> findAll() {
        return accountMapper.toDtoList(accountRepository.findAll());
    }

    @Transactional
    public AccountDto saveAccount(AccountDto accountDTO) {
        checkEmailUnique(accountDTO.getEmail());
        accountDTO.setPassword(encoder.encode(accountDTO.getPassword()));
        accountDTO.setRoles(roleService.saveRolesToUser(accountDTO));
        if (accountDTO.getAnswerQuestion() != null) {
            accountDTO.setAnswerQuestion(encoder.encode(accountDTO.getAnswerQuestion()));
        }
        var account = accountMapper.toEntity(accountDTO);
        account.setId(null);
        return accountMapper.toDto(accountRepository.saveAndFlush(account));
    }

    @Transactional
    public AccountDto updateAccount(Long id, AccountDto accountDTO) {
        var existingAccount = accountRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Operation was not finished because Account was not found with id = " + id)
        );
        if (accountDTO.getFirstName() != null) {
            existingAccount.setFirstName(accountDTO.getFirstName());
        }
        if (accountDTO.getLastName() != null) {
            existingAccount.setLastName(accountDTO.getLastName());
        }
        if (accountDTO.getEmail() != null && !accountDTO.getEmail().equals(existingAccount.getEmail())) {
            checkEmailUnique(accountDTO.getEmail());
            existingAccount.setEmail(accountDTO.getEmail());
        }
        if (accountDTO.getBirthDate() != null) {
            existingAccount.setBirthDate(accountDTO.getBirthDate());
        }
        if (accountDTO.getPhoneNumber() != null) {
            existingAccount.setPhoneNumber(accountDTO.getPhoneNumber());
        }
        if (accountDTO.getPassword() != null) {
            existingAccount.setPassword(encoder.encode(accountDTO.getPassword()));
        }
        if (accountDTO.getSecurityQuestion() != null) {
            existingAccount.setSecurityQuestion(accountDTO.getSecurityQuestion());
        }
        if (accountDTO.getAnswerQuestion() != null) {
            existingAccount.setAnswerQuestion(encoder.encode(accountDTO.getAnswerQuestion()));
        }
        if (accountDTO.getRoles() != null) {
            existingAccount.setRoles(new HashSet<>(roleMapper
                    .toEntityList(new ArrayList<>(roleService.saveRolesToUser(accountDTO)))));
        }
        return accountMapper.toDto(accountRepository.save(existingAccount));
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

    @Transactional
    public Optional<AccountDto> deleteAccountById(Long id) {
        var optionalSavedAccount = getAccountById(id);
        if (optionalSavedAccount.isPresent()) {
            accountRepository.deleteById(id);
        }
        return optionalSavedAccount;
    }

    private void checkEmailUnique(String email) {
        Account existingAccount = accountRepository.getAccountByEmail(email);
        if (existingAccount != null) {
            throw new DuplicateFieldException("Email already exists");
        }
    }
}