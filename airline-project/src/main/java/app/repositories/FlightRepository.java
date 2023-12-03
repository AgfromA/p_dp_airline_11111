package app.repositories;

import app.entities.Destination;
import app.entities.Flight;
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
            "WHERE (:cityFrom IS NULL OR f.from.cityName = :cityFrom OR :cityFrom = '') " +
            "AND (:cityTo IS NULL OR f.to.cityName = :cityTo OR :cityTo = '') " +
            "AND (:dateStart IS NULL OR concat(substring(cast(f.departureDateTime as string), 1, 10), 'T'," +
            "substring(cast(f.departureDateTime as string), 12)) = :dateStart OR :dateStart = '') " +
            "AND (:dateFinish IS NULL OR concat(substring(cast(f.arrivalDateTime as string), 1, 10), 'T', " +
            "substring(cast(f.arrivalDateTime as string), 12))  = :dateFinish OR :dateFinish = '') " +
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
            value = "with xx as (\n" +
                    "    select s1.id             as s1_id,\n" +
                    "           s1.code           as s1_code,\n" +
                    "           s1.flight_status  as s1_flight_status,\n" +
                    "           s1.departure_date as s1_departure_date,\n" +
                    "           s1.arrival_date   as s1_arrival_date,\n" +
                    "           s1.aircraft_id    as s1_aircraft_id,\n" +
                    "           s1.from_id        as s1_from,\n" +
                    "           s1.to_id          as s1_to,\n" +
                    "           s2.id             as s2_id,\n" +
                    "           s2.code           as s2_code,\n" +
                    "           s2.flight_status  as s2_flight_status,\n" +
                    "           s2.departure_date as s2_departure_date,\n" +
                    "           s2.arrival_date   as s2_arrival_date,\n" +
                    "           s2.aircraft_id    as s2_aircraft_id,\n" +
                    "           s2.from_id        as s2_from,\n" +
                    "           s2.to_id          as s2_to\n" +
                    "    from (\n" +
                    "             select *\n" +
                    "             from flights f1\n" +
                    "             where f1.from_id = ?1\n" +
                    "               and cast(f1.departure_date as date) = ?3) s1\n" +
                    "             join\n" +
                    "         (\n" +
                    "             select *\n" +
                    "             from flights f2\n" +
                    "             where f2.to_id = ?2\n" +
                    "               and cast(f2.departure_date as date) = ?3) s2\n" +
                    "         on s1.to_id = s2.from_id\n" +
                    "             and (s2.departure_date - s1.arrival_date between interval '2 hour' and interval '12 hour')\n" +
                    ")\n" +
                    "select s1_id             as id,\n" +
                    "       s1_code           as code,\n" +
                    "       s1_flight_status  as flight_status,\n" +
                    "       s1_departure_date as departure_date,\n" +
                    "       s1_arrival_date   as arrival_date,\n" +
                    "       s1_aircraft_id    as aircraft_id,\n" +
                    "       s1_from           as from_id,\n" +
                    "       s1_to             as to_id\n" +
                    "from xx\n" +
                    "union\n" +
                    "select s2_id             as id,\n" +
                    "       s2_code           as code,\n" +
                    "       s2_flight_status  as flight_status,\n" +
                    "       s2_departure_date as departure_date,\n" +
                    "       s2_arrival_date   as arrival_date,\n" +
                    "       s2_aircraft_id    as aircraft_id,\n" +
                    "       s2_from           as from_id,\n" +
                    "       s2_to             as to_id\n" +
                    "from xx\n" +
                    "order by departure_date asc, arrival_date asc, from_id asc, to_id asc\n" +
                    "\n",
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
