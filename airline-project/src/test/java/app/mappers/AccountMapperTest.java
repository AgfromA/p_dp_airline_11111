package app.mappers;

import app.dto.AccountDto;
import app.dto.RoleDto;
import app.entities.Account;
import app.entities.Role;
import app.services.RoleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.when;


class AccountMapperTest {

    private AccountMapper accountMapper = Mappers.getMapper(AccountMapper.class);
    @Mock
    private RoleService roleServiceMock = Mockito.mock(RoleService.class);

    @Test
    public void shouldConvertAccountToAccountDTO() throws Exception {
        Role role = new Role();
        role.setId(1L);
        role.setName("ROLE_MANAGER");
        Set<Role> roles = new HashSet<>();
        roles.add(role);

        Account account = new Account();

        account.setId(1L);
        account.setFirstName("Ivan");
        account.setLastName("Ivanov");
        account.setBirthDate(LocalDate.of(2023, 8, 3));
        account.setPhoneNumber("7933333333");
        account.setEmail("manager2@mail.ru");
        account.setPassword("Test123@");
        account.setAnswerQuestion("Test");
        account.setSecurityQuestion("Test");
        account.setRoles(roles);

        AccountDto accountDTO = accountMapper.toDto(account);

        Assertions.assertEquals(account.getId(), accountDTO.getId());
        Assertions.assertEquals(account.getFirstName(), accountDTO.getFirstName());
        Assertions.assertEquals(account.getLastName(), accountDTO.getLastName());
        Assertions.assertEquals(account.getBirthDate(), accountDTO.getBirthDate());
        Assertions.assertEquals(account.getPhoneNumber(), accountDTO.getPhoneNumber());
        Assertions.assertEquals(account.getEmail(), accountDTO.getEmail());
        Assertions.assertEquals(account.getPassword(), accountDTO.getPassword());
        Assertions.assertEquals(account.getAnswerQuestion(), accountDTO.getAnswerQuestion());
        Assertions.assertEquals(account.getSecurityQuestion(), accountDTO.getSecurityQuestion());
        Assertions.assertEquals(account.getRoles().iterator().next().getName(), accountDTO.getRoles().iterator().next().getName());

    }

    @Test
    public void shouldConvertAccountDTOToAccount() throws Exception {
        RoleDto role = new RoleDto();
        role.setId(1L);
        role.setName("ROLE_MANAGER");

        when(roleServiceMock.getRoleByName("ROLE_MANAGER")).thenReturn(role);

        AccountDto accountDTO = new AccountDto();

        accountDTO.setId(1L);
        accountDTO.setFirstName("Ivan");
        accountDTO.setLastName("Ivanov");
        accountDTO.setBirthDate(LocalDate.of(2023, 8, 3));
        accountDTO.setPhoneNumber("7933333333");
        accountDTO.setEmail("manager2@mail.ru");
        accountDTO.setPassword("Test123@");
        accountDTO.setAnswerQuestion("Test");
        accountDTO.setSecurityQuestion("Test");
        accountDTO.setRoles(Set.of(roleServiceMock.getRoleByName("ROLE_MANAGER")));

        Account account = accountMapper.toEntity(accountDTO);

        Assertions.assertEquals(accountDTO.getId(), account.getId());
        Assertions.assertEquals(accountDTO.getFirstName(), account.getFirstName());
        Assertions.assertEquals(accountDTO.getLastName(), account.getLastName());
        Assertions.assertEquals(accountDTO.getBirthDate(), account.getBirthDate());
        Assertions.assertEquals(accountDTO.getPhoneNumber(), account.getPhoneNumber());
        Assertions.assertEquals(accountDTO.getEmail(), account.getEmail());
        Assertions.assertEquals(accountDTO.getPassword(), account.getPassword());
        Assertions.assertEquals(accountDTO.getAnswerQuestion(), account.getAnswerQuestion());
        Assertions.assertEquals(accountDTO.getSecurityQuestion(), account.getSecurityQuestion());
        Assertions.assertEquals(accountDTO.getRoles().iterator().next().getName(), account.getRoles().iterator().next().getName());

    }

    @Test
    public void shouldConvertAccountListToAccountDTOList() throws Exception {
        List<Account> accountList = new ArrayList<>();
        Role role = new Role();
        role.setId(1L);
        role.setName("ROLE_MANAGER");
        Set<Role> roles = new HashSet<>();
        roles.add(role);

        Account accountOne = new Account();

        accountOne.setId(1L);
        accountOne.setFirstName("Ivan");
        accountOne.setLastName("Ivanov");
        accountOne.setBirthDate(LocalDate.of(2023, 8, 3));
        accountOne.setPhoneNumber("7933333333");
        accountOne.setEmail("manager2@mail.ru");
        accountOne.setPassword("Test123@");
        accountOne.setAnswerQuestion("Test");
        accountOne.setSecurityQuestion("Test");
        accountOne.setRoles(roles);

        Account accountTwo = new Account();

        accountTwo.setId(2L);
        accountTwo.setFirstName("Petr");
        accountTwo.setLastName("Petrov");
        accountTwo.setBirthDate(LocalDate.of(2022, 10, 4));
        accountTwo.setPhoneNumber("7933333335");
        accountTwo.setEmail("manager5@mail.ru");
        accountTwo.setPassword("Test125@");
        accountTwo.setAnswerQuestion("Test5");
        accountTwo.setSecurityQuestion("Test5");
        accountTwo.setRoles(roles);

        accountList.add(accountOne);
        accountList.add(accountTwo);

        List<AccountDto> accountDtoList = accountMapper.toDtoList(accountList);
        Assertions.assertEquals(accountList.size(), accountDtoList.size());

        Assertions.assertEquals(accountList.get(0).getId(), accountDtoList.get(0).getId());
        Assertions.assertEquals(accountList.get(0).getFirstName(), accountDtoList.get(0).getFirstName());
        Assertions.assertEquals(accountList.get(0).getLastName(), accountDtoList.get(0).getLastName());
        Assertions.assertEquals(accountList.get(0).getBirthDate(), accountDtoList.get(0).getBirthDate());
        Assertions.assertEquals(accountList.get(0).getPhoneNumber(), accountDtoList.get(0).getPhoneNumber());
        Assertions.assertEquals(accountList.get(0).getEmail(), accountDtoList.get(0).getEmail());
        Assertions.assertEquals(accountList.get(0).getPassword(), accountDtoList.get(0).getPassword());
        Assertions.assertEquals(accountList.get(0).getAnswerQuestion(), accountDtoList.get(0).getAnswerQuestion());
        Assertions.assertEquals(accountList.get(0).getSecurityQuestion(), accountDtoList.get(0).getSecurityQuestion());
        Assertions.assertEquals(accountList.get(0).getRoles().iterator().next().getName(), accountDtoList.get(0).getRoles().iterator().next().getName());

        Assertions.assertEquals(accountList.get(1).getId(), accountDtoList.get(1).getId());
        Assertions.assertEquals(accountList.get(1).getFirstName(), accountDtoList.get(1).getFirstName());
        Assertions.assertEquals(accountList.get(1).getLastName(), accountDtoList.get(1).getLastName());
        Assertions.assertEquals(accountList.get(1).getBirthDate(), accountDtoList.get(1).getBirthDate());
        Assertions.assertEquals(accountList.get(1).getPhoneNumber(), accountDtoList.get(1).getPhoneNumber());
        Assertions.assertEquals(accountList.get(1).getEmail(), accountDtoList.get(1).getEmail());
        Assertions.assertEquals(accountList.get(1).getPassword(), accountDtoList.get(1).getPassword());
        Assertions.assertEquals(accountList.get(1).getAnswerQuestion(), accountDtoList.get(1).getAnswerQuestion());
        Assertions.assertEquals(accountList.get(1).getSecurityQuestion(), accountDtoList.get(1).getSecurityQuestion());
        Assertions.assertEquals(accountList.get(1).getRoles().iterator().next().getName(), accountDtoList.get(1).getRoles().iterator().next().getName());
    }

    @Test
    public void shouldConvertAccountDTOListToAccountList() throws Exception {
        List<AccountDto> accountDtoList = new ArrayList<>();
        RoleDto role = new RoleDto();
        role.setId(1L);
        role.setName("ROLE_MANAGER");

        when(roleServiceMock.getRoleByName("ROLE_MANAGER")).thenReturn(role);

        AccountDto accountDtoOne = new AccountDto();

        accountDtoOne.setId(1L);
        accountDtoOne.setFirstName("Ivan");
        accountDtoOne.setLastName("Ivanov");
        accountDtoOne.setBirthDate(LocalDate.of(2023, 8, 3));
        accountDtoOne.setPhoneNumber("7933333333");
        accountDtoOne.setEmail("manager2@mail.ru");
        accountDtoOne.setPassword("Test123@");
        accountDtoOne.setAnswerQuestion("Test");
        accountDtoOne.setSecurityQuestion("Test");
        accountDtoOne.setRoles(Set.of(roleServiceMock.getRoleByName("ROLE_MANAGER")));

        AccountDto accountDtoTwo = new AccountDto();

        accountDtoTwo.setId(2L);
        accountDtoTwo.setFirstName("Petr");
        accountDtoTwo.setLastName("Petrov");
        accountDtoTwo.setBirthDate(LocalDate.of(2022, 10, 4));
        accountDtoTwo.setPhoneNumber("7933333335");
        accountDtoTwo.setEmail("manager5@mail.ru");
        accountDtoTwo.setPassword("Test125@");
        accountDtoTwo.setAnswerQuestion("Test5");
        accountDtoTwo.setSecurityQuestion("Test5");
        accountDtoTwo.setRoles(Set.of(roleServiceMock.getRoleByName("ROLE_MANAGER")));

        accountDtoList.add(accountDtoOne);
        accountDtoList.add(accountDtoTwo);

        List<Account> accountList = accountMapper.toEntityList(accountDtoList);
        Assertions.assertEquals(accountList.size(), accountDtoList.size());

        Assertions.assertEquals(accountDtoList.get(0).getId(), accountList.get(0).getId());
        Assertions.assertEquals(accountDtoList.get(0).getFirstName(), accountList.get(0).getFirstName());
        Assertions.assertEquals(accountDtoList.get(0).getLastName(), accountList.get(0).getLastName());
        Assertions.assertEquals(accountDtoList.get(0).getBirthDate(), accountList.get(0).getBirthDate());
        Assertions.assertEquals(accountDtoList.get(0).getPhoneNumber(), accountList.get(0).getPhoneNumber());
        Assertions.assertEquals(accountDtoList.get(0).getEmail(), accountList.get(0).getEmail());
        Assertions.assertEquals(accountDtoList.get(0).getPassword(), accountList.get(0).getPassword());
        Assertions.assertEquals(accountDtoList.get(0).getAnswerQuestion(), accountList.get(0).getAnswerQuestion());
        Assertions.assertEquals(accountDtoList.get(0).getSecurityQuestion(), accountList.get(0).getSecurityQuestion());
        Assertions.assertEquals(accountDtoList.get(0).getRoles().iterator().next().getName(), accountList.get(0).getRoles().iterator().next().getName());

        Assertions.assertEquals(accountDtoList.get(1).getId(), accountList.get(1).getId());
        Assertions.assertEquals(accountDtoList.get(1).getFirstName(), accountList.get(1).getFirstName());
        Assertions.assertEquals(accountDtoList.get(1).getLastName(), accountList.get(1).getLastName());
        Assertions.assertEquals(accountDtoList.get(1).getBirthDate(), accountList.get(1).getBirthDate());
        Assertions.assertEquals(accountDtoList.get(1).getPhoneNumber(), accountList.get(1).getPhoneNumber());
        Assertions.assertEquals(accountDtoList.get(1).getEmail(), accountList.get(1).getEmail());
        Assertions.assertEquals(accountDtoList.get(1).getPassword(), accountList.get(1).getPassword());
        Assertions.assertEquals(accountDtoList.get(1).getAnswerQuestion(), accountList.get(1).getAnswerQuestion());
        Assertions.assertEquals(accountDtoList.get(1).getSecurityQuestion(), accountList.get(1).getSecurityQuestion());
        Assertions.assertEquals(accountDtoList.get(1).getRoles().iterator().next().getName(), accountList.get(1).getRoles().iterator().next().getName());


    }

}
