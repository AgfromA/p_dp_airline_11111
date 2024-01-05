package app.mappers;

import app.dto.AccountDTO;
import app.dto.RoleDTO;
import app.entities.Account;
import app.entities.Role;
import app.services.interfaces.RoleService;
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

        AccountDTO accountDTO = accountMapper.convertToAccountDTO(account);

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
        RoleDTO role = new RoleDTO();
        role.setId(1L);
        role.setName("ROLE_MANAGER");

        when(roleServiceMock.getRoleByName("ROLE_MANAGER")).thenReturn(role);

        AccountDTO accountDTO = new AccountDTO();

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

        Account account = accountMapper.convertToAccount(accountDTO);

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

        List<AccountDTO> accountDTOList = accountMapper.convertToAccountDTOList(accountList);
        Assertions.assertEquals(accountList.size(), accountDTOList.size());

        Assertions.assertEquals(accountList.get(0).getId(), accountDTOList.get(0).getId());
        Assertions.assertEquals(accountList.get(0).getFirstName(), accountDTOList.get(0).getFirstName());
        Assertions.assertEquals(accountList.get(0).getLastName(), accountDTOList.get(0).getLastName());
        Assertions.assertEquals(accountList.get(0).getBirthDate(), accountDTOList.get(0).getBirthDate());
        Assertions.assertEquals(accountList.get(0).getPhoneNumber(), accountDTOList.get(0).getPhoneNumber());
        Assertions.assertEquals(accountList.get(0).getEmail(), accountDTOList.get(0).getEmail());
        Assertions.assertEquals(accountList.get(0).getPassword(), accountDTOList.get(0).getPassword());
        Assertions.assertEquals(accountList.get(0).getAnswerQuestion(), accountDTOList.get(0).getAnswerQuestion());
        Assertions.assertEquals(accountList.get(0).getSecurityQuestion(), accountDTOList.get(0).getSecurityQuestion());
        Assertions.assertEquals(accountList.get(0).getRoles().iterator().next().getName(), accountDTOList.get(0).getRoles().iterator().next().getName());

        Assertions.assertEquals(accountList.get(1).getId(), accountDTOList.get(1).getId());
        Assertions.assertEquals(accountList.get(1).getFirstName(), accountDTOList.get(1).getFirstName());
        Assertions.assertEquals(accountList.get(1).getLastName(), accountDTOList.get(1).getLastName());
        Assertions.assertEquals(accountList.get(1).getBirthDate(), accountDTOList.get(1).getBirthDate());
        Assertions.assertEquals(accountList.get(1).getPhoneNumber(), accountDTOList.get(1).getPhoneNumber());
        Assertions.assertEquals(accountList.get(1).getEmail(), accountDTOList.get(1).getEmail());
        Assertions.assertEquals(accountList.get(1).getPassword(), accountDTOList.get(1).getPassword());
        Assertions.assertEquals(accountList.get(1).getAnswerQuestion(), accountDTOList.get(1).getAnswerQuestion());
        Assertions.assertEquals(accountList.get(1).getSecurityQuestion(), accountDTOList.get(1).getSecurityQuestion());
        Assertions.assertEquals(accountList.get(1).getRoles().iterator().next().getName(), accountDTOList.get(1).getRoles().iterator().next().getName());
    }
    @Test
    public void shouldConvertAccountDTOListToAccountList() throws Exception {
        List<AccountDTO> accountDTOList = new ArrayList<>();
        RoleDTO role = new RoleDTO();
        role.setId(1L);
        role.setName("ROLE_MANAGER");

        when(roleServiceMock.getRoleByName("ROLE_MANAGER")).thenReturn(role);

        AccountDTO accountDTOOne = new AccountDTO();

        accountDTOOne.setId(1L);
        accountDTOOne.setFirstName("Ivan");
        accountDTOOne.setLastName("Ivanov");
        accountDTOOne.setBirthDate(LocalDate.of(2023, 8, 3));
        accountDTOOne.setPhoneNumber("7933333333");
        accountDTOOne.setEmail("manager2@mail.ru");
        accountDTOOne.setPassword("Test123@");
        accountDTOOne.setAnswerQuestion("Test");
        accountDTOOne.setSecurityQuestion("Test");
        accountDTOOne.setRoles(Set.of(roleServiceMock.getRoleByName("ROLE_MANAGER")));

        AccountDTO accountDTOTwo = new AccountDTO();

        accountDTOTwo.setId(2L);
        accountDTOTwo.setFirstName("Petr");
        accountDTOTwo.setLastName("Petrov");
        accountDTOTwo.setBirthDate(LocalDate.of(2022, 10, 4));
        accountDTOTwo.setPhoneNumber("7933333335");
        accountDTOTwo.setEmail("manager5@mail.ru");
        accountDTOTwo.setPassword("Test125@");
        accountDTOTwo.setAnswerQuestion("Test5");
        accountDTOTwo.setSecurityQuestion("Test5");
        accountDTOTwo.setRoles(Set.of(roleServiceMock.getRoleByName("ROLE_MANAGER")));

        accountDTOList.add(accountDTOOne);
        accountDTOList.add(accountDTOTwo);

        List<Account> accountList = accountMapper.convertToAccountList(accountDTOList);
        Assertions.assertEquals(accountList.size(), accountDTOList.size());

        Assertions.assertEquals(accountDTOList.get(0).getId(), accountList.get(0).getId());
        Assertions.assertEquals(accountDTOList.get(0).getFirstName(), accountList.get(0).getFirstName());
        Assertions.assertEquals(accountDTOList.get(0).getLastName(), accountList.get(0).getLastName());
        Assertions.assertEquals(accountDTOList.get(0).getBirthDate(), accountList.get(0).getBirthDate());
        Assertions.assertEquals(accountDTOList.get(0).getPhoneNumber(), accountList.get(0).getPhoneNumber());
        Assertions.assertEquals(accountDTOList.get(0).getEmail(), accountList.get(0).getEmail());
        Assertions.assertEquals(accountDTOList.get(0).getPassword(), accountList.get(0).getPassword());
        Assertions.assertEquals(accountDTOList.get(0).getAnswerQuestion(), accountList.get(0).getAnswerQuestion());
        Assertions.assertEquals(accountDTOList.get(0).getSecurityQuestion(), accountList.get(0).getSecurityQuestion());
        Assertions.assertEquals(accountDTOList.get(0).getRoles().iterator().next().getName(), accountList.get(0).getRoles().iterator().next().getName());

        Assertions.assertEquals(accountDTOList.get(1).getId(), accountList.get(1).getId());
        Assertions.assertEquals(accountDTOList.get(1).getFirstName(), accountList.get(1).getFirstName());
        Assertions.assertEquals(accountDTOList.get(1).getLastName(), accountList.get(1).getLastName());
        Assertions.assertEquals(accountDTOList.get(1).getBirthDate(), accountList.get(1).getBirthDate());
        Assertions.assertEquals(accountDTOList.get(1).getPhoneNumber(), accountList.get(1).getPhoneNumber());
        Assertions.assertEquals(accountDTOList.get(1).getEmail(), accountList.get(1).getEmail());
        Assertions.assertEquals(accountDTOList.get(1).getPassword(), accountList.get(1).getPassword());
        Assertions.assertEquals(accountDTOList.get(1).getAnswerQuestion(), accountList.get(1).getAnswerQuestion());
        Assertions.assertEquals(accountDTOList.get(1).getSecurityQuestion(), accountList.get(1).getSecurityQuestion());
        Assertions.assertEquals(accountDTOList.get(1).getRoles().iterator().next().getName(), accountList.get(1).getRoles().iterator().next().getName());


    }

}
