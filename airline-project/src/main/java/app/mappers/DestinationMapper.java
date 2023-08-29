package app.mappers;

import app.dto.DestinationDTO;
import app.entities.Destination;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface DestinationMapper {



    DestinationDTO convertToDestinationDTOEntity(Destination destination);//
    //перенос данных из Destination в DestinationDTO
    Destination convertToDestinationEntity(DestinationDTO dto);
    //перенос данных из DestinationDTO в Destination


}