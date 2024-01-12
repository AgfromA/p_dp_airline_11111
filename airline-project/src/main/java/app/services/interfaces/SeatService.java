package app.services.interfaces;

import app.dto.SeatDto;
import app.entities.Seat;
import app.exceptions.ViolationOfForeignKeyConstraintException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SeatService {
    List<SeatDto> getAllSeats();

    Seat saveSeat(SeatDto seatDTO);

    Seat getSeatById(long id);

    Seat editSeatById(Long id, SeatDto seatDTO);

    void deleteSeatById(Long id) throws ViolationOfForeignKeyConstraintException;

    Page<SeatDto> getPagesSeatsByAircraftId(Long id, Pageable pageable);

    List<SeatDto> generateSeatsDTOByAircraftId(long aircraftId);

    Page<SeatDto> getAllPagesSeats(Integer page, Integer size);
}
