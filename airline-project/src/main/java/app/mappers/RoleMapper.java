package app.mappers;


import app.dto.RoleDTO;
import app.entities.Role;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RoleMapper {

    RoleDTO convertToRoleDTO(Role role);

    Role convertToRole(RoleDTO roleDTO);
}
