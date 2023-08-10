package app.util.mappers;

import app.dto.AccountDTO;

import app.entities.account.Account;
import org.springframework.stereotype.Component;

@Deprecated
@Component
public class AccountMapper {
    public AccountDTO convertToAccountDTO(Account account) {
        var accountDTO = new AccountDTO();

        accountDTO.setId(account.getId());
        accountDTO.setFirstName(account.getFirstName());
        accountDTO.setLastName(account.getLastName());
        accountDTO.setBirthDate(account.getBirthDate());
        accountDTO.setPhoneNumber(account.getPhoneNumber());
        accountDTO.setEmail(account.getEmail());
        accountDTO.setPassword(account.getPassword());
        accountDTO.setAnswerQuestion(account.getAnswerQuestion());
        accountDTO.setSecurityQuestion(account.getSecurityQuestion());
        accountDTO.setRoles(account.getRoles());

        return accountDTO;
    }

    public Account convertToAccount(AccountDTO accountDTO) {
        var account = new Account();

        account.setId(accountDTO.getId());
        account.setFirstName(accountDTO.getFirstName());
        account.setLastName(accountDTO.getLastName());
        account.setBirthDate(accountDTO.getBirthDate());
        account.setPhoneNumber(accountDTO.getPhoneNumber());
        account.setEmail(accountDTO.getEmail());
        account.setPassword(accountDTO.getPassword());
        account.setAnswerQuestion(accountDTO.getAnswerQuestion());
        account.setSecurityQuestion(accountDTO.getSecurityQuestion());
        account.setRoles(accountDTO.getRoles());

        return account;
    }
}
