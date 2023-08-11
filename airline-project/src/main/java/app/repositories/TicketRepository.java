package app.repositories;

import app.entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    @Query("SELECT ticket FROM Ticket ticket LEFT JOIN FETCH ticket.passenger passenger " +
            "LEFT JOIN FETCH ticket.flight flight LEFT JOIN FETCH ticket.flightSeat flightSeat " +
            "LEFT JOIN FETCH flight.aircraft aircraft LEFT JOIN FETCH aircraft.seatSet seatSet " +
            "WHERE ticket.ticketNumber = ?1")
    Ticket findByTicketNumberContainingIgnoreCase(String ticketNumber);


    Ticket findTicketById(long id);

    @Query(value = "SELECT t.flightSeat.id FROM Ticket t WHERE t.passenger.id = :passengerId")
    long [] findArrayOfFlightSeatIdByPassengerId(@Param("passengerId") long passengerId);

    @Modifying
    @Query(value = "DELETE FROM Ticket t WHERE t.passenger.id = :passengerId")
    void deleteTicketByPassengerId(@Param("passengerId") long passengerId);
}