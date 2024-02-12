package app.services;

import app.dto.AccountDto;
import app.dto.RoleDto;
import app.entities.Account;
import app.exceptions.DuplicateFieldException;
import app.exceptions.EntityNotFoundException;
import app.mappers.AccountMapper;
import app.repositories.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;
    @InjectMocks
    private AccountService accountService;
    @Mock
    private AccountMapper accountMapper;
    @Mock
    private PasswordEncoder encoder;
    @Mock
    private RoleService roleService;

    @Test
    void testGetAllAccount() {
        List<Account> accountList = new ArrayList<>();
        accountList.add(new Account());
        accountList.add(new Account());

        when(accountRepository.findAll()).thenReturn(accountList);

        List<AccountDto> expectedAccountDtoList = new ArrayList<>();
        expectedAccountDtoList.add(new AccountDto());
        expectedAccountDtoList.add(new AccountDto());

        when(accountMapper.toDtoList(accountList)).thenReturn(expectedAccountDtoList);

        List<AccountDto> actualAccountDtoList = accountService.findAll();

        assertNotNull(actualAccountDtoList);
        assertEquals(expectedAccountDtoList, actualAccountDtoList);
        verify(accountRepository, times(1)).findAll();
        verify(accountMapper, times(1)).toDtoList(accountList);
    }

    @Test
    void testGetAllAccountsPageable() {
        Account account = new Account();
        AccountDto accountDto = new AccountDto();
        when(accountMapper.toDto(any())).thenReturn(accountDto);
        when(accountRepository.findAll(any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(account)));

        Page<AccountDto> result = accountService.getPage(0, 10);

        assertNotNull(result);
        assertTrue(result.hasContent());
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        verify(accountRepository, times(1)).findAll(any(PageRequest.class));
    }

    @Test
    void testSaveAccount() {
        Account account = new Account();
        AccountDto accountDto = new AccountDto();
        accountDto.setPassword("Test#123");
        Set<RoleDto> rolesDto = new HashSet<>();
        rolesDto.add(new RoleDto());

        when(encoder.encode(accountDto.getPassword())).thenReturn(accountDto.getPassword());
        when(roleService.saveRolesToUser(accountDto)).thenReturn(rolesDto);
        when(accountMapper.toDto(account)).thenReturn(accountDto);
        when(accountMapper.toEntity(accountDto)).thenReturn(account);
        when(accountRepository.saveAndFlush(account)).thenReturn(account);

        AccountDto result = accountService.saveAccount(accountDto);

        assertEquals(accountDto, result);
        verify(accountRepository, times(1)).saveAndFlush(account);
        verify(encoder, times(1)).encode(accountDto.getPassword());
        verify(roleService, times(1)).saveRolesToUser(accountDto);
        assertNull(account.getId());
    }

    @Test
    void testSaveAccountWithDuplicateEmail_ThrowsException() {
        String email = "test@mail.com";
        Account account = new Account();
        AccountDto accountDto = new AccountDto();
        accountDto.setEmail(email);

        when(accountRepository.getAccountByEmail(email)).thenReturn(account);

        Exception exception = assertThrows(DuplicateFieldException.class, () -> {
            accountService.saveAccount(accountDto);
        });
        assertEquals("Email already exists", exception.getMessage());
    }

    @Test
    void testUpdateAccount() {
        Long id = 1L;
        AccountDto accountDto = new AccountDto();
        accountDto.setFirstName("first");
        accountDto.setLastName("last");
        accountDto.setEmail("test@example.com");
        accountDto.setBirthDate(LocalDate.of(1990, 5, 15));
        accountDto.setPhoneNumber("1234567890");
        accountDto.setPassword("Test#123");
        accountDto.setSecurityQuestion("qwe?");
        accountDto.setAnswerQuestion("qwe");

        Account existingAccount = new Account();
        existingAccount.setId(id);
        existingAccount.setEmail("email@example.com");

        when(accountRepository.findById(id)).thenReturn(Optional.of(existingAccount));
        when(accountRepository.save(existingAccount)).thenReturn(existingAccount);
        when(accountMapper.toDto(existingAccount)).thenReturn(accountDto);
        when(encoder.encode(anyString())).thenAnswer(invocation -> invocation.getArgument(0));

        AccountDto result = accountService.updateAccount(id, accountDto);

        assertNotNull(result);
        assertEquals(accountDto.getFirstName(), result.getFirstName());
        assertEquals(accountDto.getLastName(), result.getLastName());
        assertEquals(accountDto.getEmail(), result.getEmail());
        assertEquals(accountDto.getBirthDate(), result.getBirthDate());
        assertEquals(accountDto.getPhoneNumber(), result.getPhoneNumber());
        assertEquals(accountDto.getPassword(), result.getPassword());
        assertEquals(accountDto.getSecurityQuestion(), result.getSecurityQuestion());
        assertEquals(accountDto.getAnswerQuestion(), result.getAnswerQuestion());

        verify(accountRepository, times(1)).findById(id);
        verify(accountRepository, times(1)).save(existingAccount);
        verify(encoder, times(1)).encode(accountDto.getPassword());
        verify(encoder, times(1)).encode(accountDto.getAnswerQuestion());
        verify(accountMapper, times(1)).toDto(existingAccount);
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void testUpdateAccountById_AccountNotFound_ThrowsException() {
        Long id = 123L;
        AccountDto accountDto = new AccountDto();

        when(accountRepository.findById(id)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            accountService.updateAccount(id, accountDto);
        });
        assertEquals("Operation was not finished because Account was not found with id = " + id, exception.getMessage());
    }

    @Test
    void testUpdateAccountByIdWithDuplicateEmail_ThrowsException() {
        String emailNew = "test2@mail.com";
        Long id = 1L;
        Account existingAccount = new Account();
        AccountDto accountDto = new AccountDto();
        accountDto.setEmail(emailNew);

        when(accountRepository.findById(id)).thenReturn(Optional.of(existingAccount));
        when(accountRepository.getAccountByEmail(emailNew)).thenReturn(existingAccount);

        Exception exception = assertThrows(DuplicateFieldException.class, () -> {
            accountService.updateAccount(id, accountDto);
        });
        assertEquals("Email already exists", exception.getMessage());
    }


    @Test
    void testGetAccountById() {
        Long id = 1L;
        AccountDto accountDto = new AccountDto();
        accountDto.setId(id);
        Account account = new Account();
        account.setId(id);

        when(accountRepository.findById(id)).thenReturn(Optional.of(account));
        when(accountMapper.toDto(account)).thenReturn(accountDto);

        Optional<AccountDto> result = accountService.getAccountById(id);


        assertEquals(Optional.of(accountDto), result);
        verify(accountRepository, times(1)).findById(id);
    }

    @Test
    void testGetAccountById_AccountNotFound() {
        Long id = 123L;

        when(accountRepository.findById(id)).thenReturn(Optional.empty());

        Optional<AccountDto> result = accountService.getAccountById(id);

        assertFalse(result.isPresent());
        verify(accountRepository, times(1)).findById(id);
    }

    @Test
    void testGetAccountByEmail() {
        String email = "test@test.com";
        AccountDto accountDto = new AccountDto();
        accountDto.setEmail(email);
        Account account = new Account();
        account.setEmail(email);

        when(accountRepository.getAccountByEmail(email)).thenReturn(account);
        when(accountMapper.toDto(account)).thenReturn(accountDto);

        AccountDto result = accountService.getAccountByEmail(email);

        assertEquals(accountDto, result);
        verify(accountRepository, times(1)).getAccountByEmail(email);
    }

    @Test
    void testGetAccountByEmail_AccountNotFound() {
        String email = "test@test.com";

        when(accountRepository.getAccountByEmail(email)).thenReturn(null);

        AccountDto result = accountService.getAccountByEmail(email);

        assertNull(result);
        verify(accountRepository, times(1)).getAccountByEmail(email);
    }

    @Test
    void testDeleteAccountById() {
        Long id = 1L;
        AccountDto accountDto = new AccountDto();
        accountDto.setId(id);
        Account account = new Account();
        account.setId(id);

        when(accountRepository.findById(id)).thenReturn(Optional.of(account));
        when(accountMapper.toDto(account)).thenReturn(accountDto);

        Optional<AccountDto> result = accountService.deleteAccountById(id);

        assertEquals(Optional.of(accountDto), result);
        verify(accountRepository, times(1)).findById(id);
        verify(accountRepository, times(1)).deleteById(id);
    }

    @Test
    void testDeleteAccountById_AccountNotFound() {
        Long id = 123L;

        when(accountRepository.findById(id)).thenReturn(Optional.empty());

        Optional<AccountDto> result = accountService.deleteAccountById(id);

        assertFalse(result.isPresent());
        verify(accountRepository, times(1)).findById(id);
        verify(accountRepository, times(0)).deleteById(id);
    }
}
