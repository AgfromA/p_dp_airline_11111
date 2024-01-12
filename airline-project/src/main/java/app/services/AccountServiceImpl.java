package app.services;

import app.dto.AccountDTO;
import app.mappers.AccountMapper;
import app.repositories.AccountRepository;
import app.services.interfaces.AccountService;
import app.services.interfaces.RoleService;
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
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder encoder;
    private final RoleService roleService;
    private final AccountMapper accountMapper;

    @Override
    public List<AccountDTO> findAll() {
        return accountMapper.convertToAccountDTOList(accountRepository.findAll());
    }

    @Override
    public AccountDTO saveAccount(AccountDTO accountDTO) {
        accountDTO.setPassword(encoder.encode(accountDTO.getPassword()));
        accountDTO.setRoles(roleService.saveRolesToUser(accountDTO));
        if (accountDTO.getAnswerQuestion() != null) {
            accountDTO.setAnswerQuestion(encoder.encode(accountDTO.getAnswerQuestion()));
        }
        var account = accountMapper.convertToAccount(accountDTO);
        return accountMapper.convertToAccountDTO(accountRepository.saveAndFlush(account));
    }

    @Override
    public Optional<AccountDTO> updateAccount(Long id, AccountDTO accountDTO) {
        Optional<AccountDTO> optionalSavedAccount = getAccountById(id);
        AccountDTO savedAccount;
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
                .convertToAccountDTO(accountRepository
                        .save(accountMapper.convertToAccount(savedAccount))));
    }

    @Transactional(readOnly = true)
    @Override
    public Page<AccountDTO> getPage(Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return accountRepository.findAll(pageRequest).map(accountMapper::convertToAccountDTO);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<AccountDTO> getAccountById(Long id) {
        return accountRepository.findById(id).map(accountMapper::convertToAccountDTO);
    }

    @Transactional(readOnly = true)
    @Override
    public AccountDTO getAccountByEmail(String email) {
        return accountMapper.convertToAccountDTO(accountRepository.getAccountByEmail(email));
    }

    @Override
    public Optional<AccountDTO> deleteAccountById(Long id) {

        Optional<AccountDTO> optionalSavedAccount = getAccountById(id);
        if (optionalSavedAccount.isEmpty()) {
            return optionalSavedAccount;
        } else {
            accountRepository.deleteById(id);
            return optionalSavedAccount;
        }
    }
}