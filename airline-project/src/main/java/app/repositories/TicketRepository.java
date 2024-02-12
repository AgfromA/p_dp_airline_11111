package app.repositories;

import app.entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    @Query("SELECT ticket FROM Ticket ticket LEFT JOIN FETCH ticket.passenger passenger " +
            "LEFT JOIN FETCH ticket.flightSeat flightSeat " +
            "WHERE ticket.ticketNumber = ?1")
    Ticket findByTicketNumberContainingIgnoreCase(String ticketNumber);

    Ticket findTicketById(long id);

    @Query(value = "SELECT t.flightSeat.id FROM Ticket t WHERE t.passenger.id = :passengerId")
    long[] findArrayOfFlightSeatIdByPassengerId(@Param("passengerId") long passengerId);

    @Modifying
    @Query(value = "DELETE FROM Ticket t WHERE t.passenger.id = :passengerId")
    void deleteTicketByPassengerId(@Param("passengerId") long passengerId);

    @Query(value = "SELECT ticket FROM Ticket ticket LEFT JOIN FETCH ticket.flightSeat flightSeat " +
            "WHERE flightSeat IN (SELECT fs FROM FlightSeat fs LEFT JOIN fs.flight flight " +
            "WHERE flight.departureDateTime BETWEEN ?2 AND ?1)")
    List<Ticket> getAllTicketsForEmailNotification(LocalDateTime departureIn, LocalDateTime gap);

    boolean existsByTicketNumber(String ticketNumber);

    Optional<Ticket> findByBookingId(long bookingId);

}