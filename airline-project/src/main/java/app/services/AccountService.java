package app.services;

import app.dto.AccountDto;
import app.dto.AccountUpdateDto;
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

    public List<AccountDto> getAllAccounts() {
        return accountMapper.toDtoList(accountRepository.findAll());
    }

    public Page<AccountDto> getAllAccounts(Integer page, Integer size) {
        return accountRepository.findAll(PageRequest.of(page, size))
                .map(accountMapper::toDto);
    }

    @Transactional
    public AccountDto createAccount(AccountDto accountDTO) {
        checkEmailUnique(accountDTO.getEmail());
        accountDTO.setId(null);
        accountDTO.setPassword(encoder.encode(accountDTO.getPassword()));
        accountDTO.setRoles(roleService.getRolesByName(accountDTO.getRoles()));
        if (accountDTO.getAnswerQuestion() != null) {
            accountDTO.setAnswerQuestion(encoder.encode(accountDTO.getAnswerQuestion()));
        }
        var account = accountMapper.toEntity(accountDTO);
        return accountMapper.toDto(accountRepository.saveAndFlush(account));
    }


    @Transactional
    public AccountDto updateAccount(Long id, AccountUpdateDto accountDTO) {
        var existingAccount = checkIfAccountSeatExist(id);
        if (accountDTO.getUsername() != null) {
            existingAccount.setUsername(accountDTO.getUsername());
        }
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
                    .toEntityList(new ArrayList<>(roleService.getRolesByName(accountDTO.getRoles())))));
        }

        return accountMapper.toDto(accountRepository.save(existingAccount));
    }

    public Optional<AccountDto> getAccountById(Long id) {
        return accountRepository.findById(id).map(accountMapper::toDto);
    }

    public AccountDto getAccountByEmail(String email) {
        return accountMapper.toDto(accountRepository.getAccountByEmail(email));
    }

    @Transactional
    public void deleteAccount(Long id) {
        checkIfAccountSeatExist(id);
        accountRepository.deleteById(id);
    }

    private void checkEmailUnique(String email) {
        Account existingAccount = accountRepository.getAccountByEmail(email);
        if (existingAccount != null) {
            throw new DuplicateFieldException("Email already exists");
        }
    }

    private Account checkIfAccountSeatExist(Long accountId) {
        return accountRepository.findById(accountId).orElseThrow(
                () -> new EntityNotFoundException("Operation was not finished because Account was not found with id = " + accountId)
        );
    }
}