package app.mappers;

import app.dto.AccountDto;
import app.entities.Account;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AccountMapper {

    AccountDto convertToAccountDto(Account account);

    Account convertToAccount(AccountDto accountDto);

    List<AccountDto> convertToAccountDtoList(List<Account> accounts);

    List<Account> convertToAccountList(List<AccountDto> accountDtos);
}