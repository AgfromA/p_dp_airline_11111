package app.mappers;

import app.dto.AccountDTO;
import app.entities.account.Account;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AccountMapper {
    AccountDTO convertToAccountDTO(Account account);

    Account convertToAccount(AccountDTO accountDTO);
}
