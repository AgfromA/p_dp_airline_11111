package app.mappers;

import app.dto.AccountDTO;
import app.entities.Account;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AccountMapper {
    AccountDTO convertToAccountDTO(Account account);

    Account convertToAccount(AccountDTO accountDTO);

    List<AccountDTO> convertToAccountDTOList(List<Account> accounts);

    List<Account> convertToAccountList(List<AccountDTO> accountDTOs);
}
