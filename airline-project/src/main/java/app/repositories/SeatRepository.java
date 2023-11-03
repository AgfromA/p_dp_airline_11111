package app.repositories;

import app.entities.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {

    @Query(value = "select seat from Seat seat where seat.aircraft.id = (:id)")
    Page<Seat> findByAircraftId(@Param("id") Long id, Pageable pageable);

    @Query(value = "select seat from Seat seat join seat.aircraft a where a.id = :id")
    Set<Seat> findByAircraftId(Long id);

}
