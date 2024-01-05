package app.mappers;


import app.dto.RoleDTO;
import app.entities.Role;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RoleMapper {

    RoleDTO convertToRoleDTO(Role role);

    Role convertToRole(RoleDTO roleDTO);
    List<RoleDTO> convertToRoleDTOList(List<Role> roleList);
    List<Role> convertToRoleList(List<RoleDTO> roleDTOList);
}
