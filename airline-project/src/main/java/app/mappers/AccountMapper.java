package app.mappers;

import app.dto.AccountDto;
import app.dto.AccountUpdateDto;
import app.entities.Account;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AccountMapper {

    AccountDto toDto(Account account);

    Account toEntity(AccountDto accountDto);

    List<AccountDto> toDtoList(List<Account> accounts);

    List<Account> toEntityList(List<AccountDto> accountDtos);
}