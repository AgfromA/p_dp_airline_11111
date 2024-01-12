package app.mappers;

import app.dto.RoleDto;
import app.entities.Role;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RoleMapper {

    RoleDto convertToRoleDto(Role role);

    Role convertToRole(RoleDto roleDTO);

    List<RoleDto> convertToRoleDtoList(List<Role> roleList);

    List<Role> convertToRoleList(List<RoleDto> roleDtoList);
}