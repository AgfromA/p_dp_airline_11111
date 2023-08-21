package app.services.interfaces;

import app.dto.SeatDTO;
import app.entities.Seat;
import app.exceptions.ViolationOfForeignKeyConstraintException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SeatService {
    Seat saveSeat(Seat seat);
    Seat getSeatById(long id);
    Seat editSeatById(Long id, Seat Seat);
    void deleteSeatById(Long id) throws ViolationOfForeignKeyConstraintException;
    Page<Seat> getPagesSeatsByAircraftId(Long id, Pageable pageable);
    List<SeatDTO> generateSeatsDTOByAircraftId(long aircraftId);
    Page<Seat> getAllPagesSeats(Integer page, Integer size);
}
