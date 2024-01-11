package app.services.interfaces;

import app.dto.SeatDTO;
import app.entities.Seat;
import app.exceptions.ViolationOfForeignKeyConstraintException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SeatService {
    List<SeatDTO> getAllSeats();

    Seat saveSeat(SeatDTO seatDTO);

    Seat getSeatById(long id);

    Seat editSeatById(Long id, SeatDTO seatDTO);

    void deleteSeatById(Long id) throws ViolationOfForeignKeyConstraintException;

    Page<SeatDTO> getPagesSeatsByAircraftId(Long id, Pageable pageable);

    List<SeatDTO> generateSeatsDTOByAircraftId(long aircraftId);

    Page<SeatDTO> getAllPagesSeats(Integer page, Integer size);
}
