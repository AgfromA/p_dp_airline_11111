package app.mappers;

import app.dto.AccountDTO;
import app.entities.account.Account;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AccountMapper {

    AccountMapper INSTANCE = Mappers.getMapper( AccountMapper.class );

    AccountDTO convertToAccountDTO(Account account);
    Account convertToAccount(AccountDTO accountDTO);
}
