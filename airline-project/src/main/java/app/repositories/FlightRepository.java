package app.repositories;

import app.entities.Destination;
import app.entities.Flight;
import app.entities.Ticket;
import app.enums.Airport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {
    Flight getByCode(String code);

    @Query("SELECT flight FROM Flight flight LEFT JOIN FETCH flight.from from1 " +
            "LEFT JOIN FETCH flight.to to LEFT JOIN FETCH flight.aircraft aircraft " +
            "LEFT JOIN FETCH aircraft.seatSet seatSet " +
            "WHERE flight.code = ?1")
    Flight findByCodeWithLinkedEntities(String code);

    @Query("SELECT f FROM Flight f " +
            "WHERE (:cityFrom IS NULL OR f.from.cityName = :cityFrom) " +
            "AND (:cityTo IS NULL OR f.to.cityName = :cityTo) " +
            "AND (:dateStart IS NULL OR concat(substring(cast(f.departureDateTime as string), 1, 10), 'T'," +
                                              "substring(cast(f.departureDateTime as string), 12)) = :dateStart) " +
            "AND (:dateFinish IS NULL OR concat(substring(cast(f.arrivalDateTime as string), 1, 10), 'T', " +
                                               "substring(cast(f.arrivalDateTime as string), 12))  = :dateFinish) " +
            "ORDER BY f.id")
    Page<Flight> getAllFlightsByDestinationsAndDates(String cityFrom,
                                                     String cityTo,
                                                     String dateStart,
                                                     String dateFinish,
                                                     Pageable pageable);

    default List<Flight> getByFromAndToAndDepartureDate(Destination from, Destination to, LocalDate departureDate) {
        return findByFromAndToAndDepartureDateTimeBetween(from, to,
                departureDate.atStartOfDay(), departureDate.plusDays(1).atStartOfDay());
    }

    List<Flight> findByFromAndToAndDepartureDateTimeBetween(Destination from, Destination to,
                                                            LocalDateTime fromDate, LocalDateTime toDate);

    List<Flight> findByAircraft_Id(Long id);

    @Query(value = "select f\n" +
            "from Flight f\n" +
            "join Destination d on f.from.id = d.id\n" +
            "join Destination d2 on f.to.id = d2.id\n" +
            "where d.airportCode = ?1 AND d2.airportCode = ?2 AND cast(f.departureDateTime as date) = ?3")
    List<Flight> getListDirectFlightsByFromAndToAndDepartureDate(Airport airportCodeFrom, Airport airportCodeTo, Date departureDate);

    //поиск рейсов c одной пересадкой
    @Query(
            value = "with cf1 as (\n" +
                    "select f2.id as f2_id, f2.code as f2_code, f2.flight_status as f2_flight, f2.arrival_date as f2_arr, f2.departure_date as f2_dep, f2.aircraft_id as f2_air, f2.from_id as f2_from, f2.to_id as f2_to,\n" +
                    "f3.id as f3_id, f3.code as f3_code, f3.flight_status as f3_flight, f3.arrival_date as f3_arr, f3.departure_date as f3_dep, f3.aircraft_id as f3_air, f3.from_id f3_from, f3.to_id as f3_to\n" +
                    "from (select *\n" +
                    "from flights f\n" +
                    "where f.from_id = ?1 and f.to_id = ?2 and cast(f.departure_date as date) = ?3) fp\n" +
                    "left join flights f2 on (f2.from_id = fp.from_id and cast(f2.departure_date as date) = cast(fp.departure_date as date))\n" +
                    "join flights f3 on f3.from_id = f2.to_id \n" +
                    "and (f3.departure_date - f2.arrival_date between interval'2 hour' and interval'12 hour') \n" +
                    "and f3.to_id = fp.to_id)\n" +
                    "select f2_id as id, f2_code as code, f2_flight as flight_status, f2_arr as arrival_date, f2_dep as departure_date, f2_air as aircraft_id, f2_from as from_id, f2_to as to_id\n" +
                    "from cf1\n" +
                    "union\n" +
                    "select f3_id as id, f3_code as code, f3_flight as flight_status, f3_arr as arrival_date, f3_dep as departure_date, f3_air as aircraft_id, f3_from as from_id, f3_to as to_id\n" +
                    "from cf1",
            nativeQuery = true)
    List<Flight> getListNonDirectFlightsByFromAndToAndDepartureDate(int airportIdFrom, int airportIdTo, Date departureDate);

    @Query(value = "select f\n" +
            "from Flight f \n" +
            "where f.code = ?1\n" +
            "and cast(f.departureDateTime as date) = ?2\n" +
            "and cast(date_trunc('second',f.departureDateTime) as time) = ?3")
    Optional<Flight> getFlightByCodeAndDepartureDateAndTime(String flightCode, Date departureDate, Time departureTime);

    void deleteById(Long id);
}
