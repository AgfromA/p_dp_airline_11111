package app.mappers;

import app.dto.RoleDto;
import app.entities.Role;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RoleMapper {

    RoleDto toDto(Role role);

    Role toEntity(RoleDto roleDTO);

    List<RoleDto> toDtoList(List<Role> roleList);

    List<Role> toEntityList(List<RoleDto> roleDtoList);
}