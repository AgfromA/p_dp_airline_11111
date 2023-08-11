package app.repositories;

import app.entities.Aircraft;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AircraftRepository extends JpaRepository<Aircraft, Long> {

    Aircraft findByAircraftNumber(String aircraftNumber);

//    @Query(
//            value = "SELECT aircraft FROM Aircraft aircraft LEFT JOIN FETCH aircraft.seatSet WHERE aircraft.id > 0",
//            countQuery = "select count(aircraft) from Aircraft aircraft left join aircraft.seatSet where aircraft.id > 0"
//    )
    Page<Aircraft> findAll(Pageable pageable);
}
