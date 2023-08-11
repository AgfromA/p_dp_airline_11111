package app.controllers;

import app.dto.AccountDTO;
import app.repositories.AccountRepository;
import app.services.interfaces.AccountService;
import app.services.interfaces.RoleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testcontainers.shaded.org.hamcrest.MatcherAssert.assertThat;
import static org.testcontainers.shaded.org.hamcrest.Matchers.equalTo;

@Sql({"/sqlQuery/delete-from-tables.sql"})
@Sql(value = {"/sqlQuery/create-account-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class AccountControllerIT extends IntegrationTestBase {

    @Autowired
    private AccountService accountService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private AccountRepository accountRepository;

    @Test
    void shouldGetAllAccounts() throws Exception {
        mockMvc.perform(
                        get("http://localhost:8080/api/accounts"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetAccountById() throws Exception {
        var id = 3L;
        mockMvc.perform(
                        get("http://localhost:8080/api/accounts/{id}", id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(
                        new AccountDTO(accountService.getAccountById(id).get()))));
    }

    @Test
    void shouldGetNotExistedAccount() throws Exception {
        var id = 100L;
        mockMvc.perform(
                        get("http://localhost:8080/api/accounts/{id}", id))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldPostNewAccount() throws Exception {
        var accountDTO = new AccountDTO();
        accountDTO.setFirstName("Ivan");
        accountDTO.setLastName("Ivanov");
        accountDTO.setBirthDate(LocalDate.of(2023, 3, 23));
        accountDTO.setPhoneNumber("7933333333");
        accountDTO.setEmail("manager2@mail.ru");
        accountDTO.setPassword("Test123@");
        accountDTO.setSecurityQuestion("Test");
        accountDTO.setAnswerQuestion("Test");
        accountDTO.setRoles(Set.of(roleService.getRoleByName("ROLE_MANAGER")));
        mockMvc.perform(post("http://localhost:8080/api/accounts")
                        .content(objectMapper.writeValueAsString(accountDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldDeleteAccountById() throws Exception {
        var id = 3L;
        mockMvc.perform(delete("http://localhost:8080/api/accounts/{id}", id))
                .andDo(print())
                .andExpect(status().isOk());
        mockMvc.perform(get("http://localhost:8080/api/accounts/{id}", id))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Transactional
    @Test
    void shouldUpdateAccount() throws Exception {
        Long id = 2L;
        var updatableAccount = new AccountDTO(accountService.getAccountById(id).get());
        updatableAccount.setEmail("test@mail.ru");
        long numberOfAccounts = accountRepository.count();

        mockMvc.perform(patch("http://localhost:8080/api/accounts/{id}", id)
                        .content(objectMapper.writeValueAsString(updatableAccount))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@mail.ru"))
                .andExpect(result -> assertThat(accountRepository.count(), equalTo(numberOfAccounts)));
    }

}
