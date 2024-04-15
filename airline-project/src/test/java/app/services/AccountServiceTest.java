package app.services;

import app.dto.AccountDto;
import app.dto.AccountUpdateDto;
import app.dto.RoleDto;
import app.entities.Account;
import app.exceptions.DuplicateFieldException;
import app.exceptions.EntityNotFoundException;
import app.mappers.AccountMapper;
import app.repositories.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Optional;
import java.util.stream.Stream;

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

        List<AccountDto> actualAccountDtoList = accountService.getAllAccounts();

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

        Page<AccountDto> result = accountService.getAllAccounts(0, 10);

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

        AccountDto result = accountService.createAccount(accountDto);

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
            accountService.createAccount(accountDto);
        });
        assertEquals("Email already exists", exception.getMessage());
    }

    @Test
    void testUpdateAccount() {
        Long id = 1L;
        AccountUpdateDto accountUpdateDto = new AccountUpdateDto();
        accountUpdateDto.setFirstName("first");
        accountUpdateDto.setLastName("last");
        accountUpdateDto.setEmail("test@example.com");
        accountUpdateDto.setPhoneNumber("1234567890");
        accountUpdateDto.setPassword("Test#123");
        accountUpdateDto.setSecurityQuestion("qwe?");
        accountUpdateDto.setAnswerQuestion("qwe");
        Account existingAccount = new Account();
        existingAccount.setId(id);
        existingAccount.setEmail("email@example.com");

        when(accountRepository.findById(id)).thenReturn(Optional.of(existingAccount));
        when(accountRepository.save(existingAccount)).thenReturn(existingAccount);
        when(accountMapper.toUpdateDto(existingAccount)).thenReturn(accountUpdateDto);
        when(encoder.encode(anyString())).thenAnswer(invocation -> invocation.getArgument(0));

        AccountUpdateDto result = accountService.updateAccount(id, accountUpdateDto);

        assertNotNull(result);
        assertEquals(accountUpdateDto.getFirstName(), result.getFirstName());
        assertEquals(accountUpdateDto.getLastName(), result.getLastName());
        assertEquals(accountUpdateDto.getEmail(), result.getEmail());
        assertEquals(accountUpdateDto.getPhoneNumber(), result.getPhoneNumber());
        assertEquals(accountUpdateDto.getPassword(), result.getPassword());
        assertEquals(accountUpdateDto.getSecurityQuestion(), result.getSecurityQuestion());
        assertEquals(accountUpdateDto.getAnswerQuestion(), result.getAnswerQuestion());



        verify(accountRepository, times(1)).findById(id);
        verify(accountRepository, times(1)).save(existingAccount);
        verify(encoder, times(1)).encode(accountUpdateDto.getPassword());
        verify(encoder, times(1)).encode(accountUpdateDto.getAnswerQuestion());
        verify(accountMapper, times(1)).toUpdateDto(existingAccount);
        verify(accountRepository, times(1)).save(any(Account.class));


    }


        // Метод для предоставления данных для параметризированного теста
        private static Stream<Arguments> accountUpdateData() {
            return Stream.of(
                    Arguments.of("firstName", "newFirstName"),
                    Arguments.of("lastName", "newLastName"),
                    Arguments.of("email", "newemail@example.com"),
                    Arguments.of("phoneNumber", "0987654321"),
                    Arguments.of("securityQuestion", "newQuestion?")
            );
        }

    //Тест для проверки обновления отдельных полей кроме пароля и секретного вопроса
        @ParameterizedTest
        @MethodSource("accountUpdateData")
        void testUpdateAccountField(String fieldName, String newValue) throws Exception {
            Long id = 1L;
            AccountUpdateDto accountUpdateDto = new AccountUpdateDto();
            Account existingAccount = new Account();
            existingAccount.setId(id);

            // Настройка моков
            when(accountRepository.findById(id)).thenReturn(Optional.of(existingAccount));
            when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));
            when(accountMapper.toUpdateDto(any(Account.class))).thenReturn(accountUpdateDto);

            // Установка нового значения для тестируемого поля
            Field field = AccountUpdateDto.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(accountUpdateDto, newValue);

            // Вызов метода обновления
            AccountUpdateDto result = accountService.updateAccount(id, accountUpdateDto);

            // Проверка результата
            Field resultField = AccountUpdateDto.class.getDeclaredField(fieldName);
            resultField.setAccessible(true);
            Object resultValue = resultField.get(result);
            assertEquals(newValue, resultValue);

            // Проверка вызовов моков
            verify(accountRepository, times(1)).findById(id);
            verify(accountRepository, times(1)).save(any(Account.class));
            verify(accountMapper, times(1)).toUpdateDto(any(Account.class));
        }
    //тест для проверки обновления пароля и секретного вопроса
        @Test
    void testUpdatePasswordAndAnswer() {
        Long id = 1L;
        AccountUpdateDto accountUpdateDto = new AccountUpdateDto();
        accountUpdateDto.setPassword("NewPassword#123");
        accountUpdateDto.setAnswerQuestion("NewAnswer");
        Account existingAccount = new Account();
        existingAccount.setId(id);

        // Настройка моков
        when(accountRepository.findById(id)).thenReturn(Optional.of(existingAccount));
        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(encoder.encode(anyString())).thenAnswer(invocation -> invocation.getArgument(0));
        when(accountMapper.toUpdateDto(any(Account.class))).thenReturn(accountUpdateDto);

        // Вызов метода обновления
        AccountUpdateDto result = accountService.updateAccount(id, accountUpdateDto);

        // Проверка результата
        assertEquals(accountUpdateDto.getPassword(), result.getPassword());
        assertEquals(accountUpdateDto.getAnswerQuestion(), result.getAnswerQuestion());

        // Проверка вызовов моков
        verify(accountRepository, times(1)).findById(id);
        verify(accountRepository, times(1)).save(any(Account.class));
        verify(encoder, times(1)).encode(accountUpdateDto.getPassword());
        verify(encoder, times(1)).encode(accountUpdateDto.getAnswerQuestion());
        verify(accountMapper, times(1)).toUpdateDto(any(Account.class));
    }



    @Test
    void testUpdateAccountById_AccountNotFound_ThrowsException() {
        Long id = 123L;
        AccountUpdateDto accountUpdateDto = new AccountUpdateDto();

        when(accountRepository.findById(id)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            accountService.updateAccount(id, accountUpdateDto);
        });
        assertEquals("Operation was not finished because Account was not found with id = " + id, exception.getMessage());
    }

    @Test
    void testUpdateAccountByIdWithDuplicateEmail_ThrowsException() {
        String emailNew = "test2@mail.com";
        Long id = 1L;
        Account existingAccount = new Account();
        AccountUpdateDto accountUpdateDto = new AccountUpdateDto();
        accountUpdateDto.setEmail(emailNew);

        when(accountRepository.findById(id)).thenReturn(Optional.of(existingAccount));
        when(accountRepository.getAccountByEmail(emailNew)).thenReturn(existingAccount);

        Exception exception = assertThrows(DuplicateFieldException.class, () -> {
            accountService.updateAccount(id, accountUpdateDto);
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

        accountService.deleteAccount(id);

        verify(accountRepository, times(1)).findById(id);
        verify(accountRepository, times(1)).deleteById(id);
    }

    @Test
    void testDeleteAccountById_AccountNotFound() {
        Long id = 123L;

        when(accountRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> accountService.deleteAccount(id));

        verify(accountRepository, times(1)).findById(id);
        verify(accountRepository, times(0)).deleteById(id);
    }
}
