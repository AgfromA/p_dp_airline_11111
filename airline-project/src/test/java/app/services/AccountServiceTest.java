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
        // Assuming you need to set roles in accountDto
        Set<RoleDto> rolesDto = new HashSet<>();
        RoleDto roleDto = new RoleDto();
        roleDto.setName("RoleName"); // Assuming RoleDto has a setName method
        rolesDto.add(roleDto);
        accountDto.setRoles(rolesDto); // Assuming AccountDto has a setRoles method

        when(encoder.encode(accountDto.getPassword())).thenReturn(accountDto.getPassword());
        when(roleService.getRolesByName(accountDto.getRoles())).thenReturn(rolesDto);
        when(accountMapper.toDto(account)).thenReturn(accountDto);
        when(accountMapper.toEntity(accountDto)).thenReturn(account);
        when(accountRepository.saveAndFlush(account)).thenReturn(account);

        AccountDto result = accountService.createAccount(accountDto);

        assertEquals(accountDto, result);
        verify(accountRepository, times(1)).saveAndFlush(account);
        verify(encoder, times(1)).encode(accountDto.getPassword());
        verify(roleService, times(1)).getRolesByName(rolesDto);
        // Adjust this assertion based on whether your implementation sets an ID
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

        AccountDto expectedAccountDto = new AccountDto();
        expectedAccountDto.setFirstName(accountUpdateDto.getFirstName());
        expectedAccountDto.setLastName(accountUpdateDto.getLastName());
        expectedAccountDto.setEmail(accountUpdateDto.getEmail());
        expectedAccountDto.setPhoneNumber(accountUpdateDto.getPhoneNumber());
        expectedAccountDto.setPassword(accountUpdateDto.getPassword());
        expectedAccountDto.setSecurityQuestion(accountUpdateDto.getSecurityQuestion());
        expectedAccountDto.setAnswerQuestion(accountUpdateDto.getAnswerQuestion());

        when(accountRepository.findById(id)).thenReturn(Optional.of(existingAccount));
        when(accountRepository.save(existingAccount)).thenReturn(existingAccount);
        when(accountMapper.toDto(existingAccount)).thenReturn(expectedAccountDto);
        when(encoder.encode(anyString())).thenAnswer(invocation -> invocation.getArgument(0));

        AccountDto result = accountService.updateAccount(id, accountUpdateDto);

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
        verify(accountMapper, times(1)).toDto(existingAccount);
        verify(accountRepository, times(1)).save(any(Account.class));


    }
//тест для проверки обновления поля email

    @Test
    void testUpdateAccountEmailField() throws Exception {
        // Подготовка данных
        Long id = 1L;
        AccountUpdateDto accountUpdateDto = new AccountUpdateDto();
        accountUpdateDto.setEmail("newemail@example.com"); // Устанавливаем только email

        Account existingAccount = new Account();
        existingAccount.setId(id);
        existingAccount.setEmail("oldEmail@example.com");

        AccountDto expectedAccountDto = new AccountDto();
        expectedAccountDto.setEmail(accountUpdateDto.getEmail()); // Ожидаемый email

        // Настройка моков
        when(accountRepository.findById(id)).thenReturn(Optional.of(existingAccount));
        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(accountMapper.toDto(any(Account.class))).thenReturn(expectedAccountDto);


        // Вызов тестируемого метода
        AccountDto result = accountService.updateAccount(id, accountUpdateDto);

        // Проверка результата
        assertEquals(accountUpdateDto.getEmail(), result.getEmail()); // Проверяем только email

        // Проверка вызовов моков
        verify(accountRepository, times(1)).findById(id);
        verify(accountRepository, times(1)).save(any(Account.class));
        verify(accountMapper, times(1)).toDto(any(Account.class));

    }
    //тест для проверки обновления поля FirstName
    @Test
    void testUpdateAccountFirstNameField() throws Exception {
        // Подготовка данных
        Long id = 1L;
        AccountUpdateDto accountUpdateDto = new AccountUpdateDto();
        accountUpdateDto.setFirstName("Mike"); // Устанавливаем только FirstName

        Account existingAccount = new Account();
        existingAccount.setId(id);
        existingAccount.setFirstName("Kit");

        AccountDto expectedAccountDto = new AccountDto();
        expectedAccountDto.setFirstName(accountUpdateDto.getFirstName()); // Ожидаемый FirstName

        // Настройка моков
        when(accountRepository.findById(id)).thenReturn(Optional.of(existingAccount));
        when(accountRepository.save(existingAccount)).thenReturn(existingAccount);
        when(accountMapper.toDto(existingAccount)).thenReturn(expectedAccountDto);


        // Вызов тестируемого метода
        AccountDto result = accountService.updateAccount(id, accountUpdateDto);

        // Проверка результата
        assertEquals(accountUpdateDto.getFirstName(), result.getFirstName()); // Проверяем только FirstName

        // Проверка вызовов моков
        verify(accountRepository, times(1)).findById(id);
        verify(accountRepository, times(1)).save(existingAccount);
        verify(accountMapper, times(1)).toDto(existingAccount);

    }
    //тест для проверки
    @Test
    void testUpdateAccountLastNameField() throws Exception {
        // Подготовка данных
        Long id = 1L;
        AccountUpdateDto accountUpdateDto = new AccountUpdateDto();
        accountUpdateDto.setLastName("Smith"); // Устанавливаем только LastName

        Account existingAccount = new Account();
        existingAccount.setId(id);
        existingAccount.setLastName("Kale");

        AccountDto expectedAccountDto = new AccountDto();
        expectedAccountDto.setLastName(accountUpdateDto.getLastName()); // Ожидаемый LastName

        // Настройка моков
        when(accountRepository.findById(id)).thenReturn(Optional.of(existingAccount));
        when(accountRepository.save(existingAccount)).thenReturn(existingAccount);
        when(accountMapper.toDto(existingAccount)).thenReturn(expectedAccountDto);


        // Вызов тестируемого метода
        AccountDto result = accountService.updateAccount(id, accountUpdateDto);

        // Проверка результата
        assertEquals(accountUpdateDto.getLastName(), result.getLastName()); // Проверяем только LastName

        // Проверка вызовов моков
        verify(accountRepository, times(1)).findById(id);
        verify(accountRepository, times(1)).save(existingAccount);
        verify(accountMapper, times(1)).toDto(existingAccount);

    }
    //тест для проверки обновления поля PhoneNumber
    @Test
    void testUpdateAccountPhoneNumberField() throws Exception {
        // Подготовка данных
        Long id = 1L;
        AccountUpdateDto accountUpdateDto = new AccountUpdateDto();
        accountUpdateDto.setPhoneNumber("8999999999"); // Устанавливаем только PhoneNumber

        Account existingAccount = new Account();
        existingAccount.setId(id);
        existingAccount.setPhoneNumber("8999999991");

        AccountDto expectedAccountDto = new AccountDto();
        expectedAccountDto.setPhoneNumber(accountUpdateDto.getPhoneNumber()); // Ожидаемый PhoneNumber

        // Настройка моков
        when(accountRepository.findById(id)).thenReturn(Optional.of(existingAccount));
        when(accountRepository.save(existingAccount)).thenReturn(existingAccount);
        when(accountMapper.toDto(existingAccount)).thenReturn(expectedAccountDto);


        // Вызов тестируемого метода
        AccountDto result = accountService.updateAccount(id, accountUpdateDto);

        // Проверка результата
        assertEquals(accountUpdateDto.getPhoneNumber(), result.getPhoneNumber()); // Проверяем только PhoneNumber

        // Проверка вызовов моков
        verify(accountRepository, times(1)).findById(id);
        verify(accountRepository, times(1)).save(existingAccount);
        verify(accountMapper, times(1)).toDto(existingAccount);
    }

    @Test
    void testUpdateAccountSecurityQuestionField() throws Exception {
        // Подготовка данных
        Long id = 1L;
        AccountUpdateDto accountUpdateDto = new AccountUpdateDto();
        accountUpdateDto.setSecurityQuestion("rly?"); // Устанавливаем только PhoneNumber

        Account existingAccount = new Account();
        existingAccount.setId(id);
        existingAccount.setSecurityQuestion("why?");

        AccountDto expectedAccountDto = new AccountDto();
        expectedAccountDto.setSecurityQuestion(accountUpdateDto.getSecurityQuestion()); // Ожидаемый PhoneNumber

        // Настройка моков
        when(accountRepository.findById(id)).thenReturn(Optional.of(existingAccount));
        when(accountRepository.save(existingAccount)).thenReturn(existingAccount);
        when(accountMapper.toDto(existingAccount)).thenReturn(expectedAccountDto);


        // Вызов тестируемого метода
        AccountDto result = accountService.updateAccount(id, accountUpdateDto);

        // Проверка результата
        assertEquals(accountUpdateDto.getSecurityQuestion(), result.getSecurityQuestion()); // Проверяем только PhoneNumber

        // Проверка вызовов моков
        verify(accountRepository, times(1)).findById(id);
        verify(accountRepository, times(1)).save(existingAccount);
        verify(accountMapper, times(1)).toDto(existingAccount);

    }

    //тест для проверки обновления пароля и ответа на секретный вопрос
        @Test
    void testUpdatePasswordAndAnswer() {
        Long id = 1L;
        AccountDto accountDto = new AccountDto();
        AccountUpdateDto accountUpdateDto = new AccountUpdateDto();
        accountUpdateDto.setPassword("NewPassword#123");
        accountUpdateDto.setAnswerQuestion("NewAnswer");
        Account existingAccount = new Account();
        existingAccount.setId(id);
        accountDto.setAnswerQuestion(accountUpdateDto.getSecurityQuestion());
        accountDto.setPassword(accountUpdateDto.getPassword());

        // Настройка моков
        when(accountRepository.findById(id)).thenReturn(Optional.of(existingAccount));
        when(accountRepository.save( existingAccount)).thenAnswer(invocation -> invocation.getArgument(0));
        when(encoder.encode(anyString())).thenAnswer(invocation -> invocation.getArgument(0));
        when(accountMapper.toDto( existingAccount)).thenReturn(accountDto);

        // Вызов метода обновления
        AccountDto result = accountService.updateAccount(id, accountUpdateDto);

        // Проверка результата
        assertEquals(accountDto.getPassword(), result.getPassword());
        assertEquals(accountDto.getAnswerQuestion(), result.getAnswerQuestion());

        // Проверка вызовов моков
        verify(accountRepository, times(1)).findById(id);
        verify(accountRepository, times(1)).save( existingAccount);
        verify(encoder, times(1)).encode(accountUpdateDto.getPassword());
        verify(encoder, times(1)).encode(accountUpdateDto.getAnswerQuestion());
        verify(accountMapper, times(1)).toDto( existingAccount);
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
