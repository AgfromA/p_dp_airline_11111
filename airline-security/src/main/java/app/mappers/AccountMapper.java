package app.mappers;

import app.dto.AccountDTO;
import app.account.Account;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AccountMapper {
    AccountDTO convertToAccountDTO(Account account);

    Account convertToAccount(AccountDTO accountDTO);
}