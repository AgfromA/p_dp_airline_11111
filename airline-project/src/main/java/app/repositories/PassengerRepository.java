package app.repositories;

import app.entities.Passenger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface PassengerRepository extends JpaRepository<Passenger, Long> {

    Page<Passenger> findAll(Pageable pageable);

    @Query(value = "SELECT p from Passenger p WHERE p.passport.serialNumberPassport = ?1")
    Page<Passenger> findByPassportSerialNumber(Pageable pageable, String passportSerialNumber);

    @Query(value = "SELECT passengers FROM Passenger passengers WHERE passengers.firstName LIKE %?1% and passengers.lastName LIKE %?2%")
    Page<Passenger> findByFirstNameAndLastName(Pageable pageable, String firstName, String lastName);

    @Query(value = "SELECT passengers FROM Passenger passengers WHERE passengers.lastName LIKE %?1%")
    Page<Passenger> findByLastName(Pageable pageable, String lastName);

    @Query(value = "SELECT passengers FROM Passenger passengers WHERE passengers.firstName LIKE %?1%")
    Page<Passenger> findAllByFirstName(Pageable pageable, String firstName);

    @Query(value = "SELECT passengers FROM Passenger passengers WHERE passengers.passport.middleName = :middleName")
    List<Passenger> findByMiddleName(String middleName);

    @Query("SELECT p FROM Passenger p WHERE p.lastName = ?1 OR p.firstName = :name OR p.passport.middleName = :name")
    List<Passenger> findByAnyName(String name);

    //    List<Passenger> findAllByFirstNameOrLastNameOrPassport_MiddleName(List<Passenger> passengersNames);
    @Query(value = "SELECT passengers FROM Passenger passengers WHERE passengers.email LIKE %?1%")
    Page<Passenger> findByEmail(Pageable pageable, String email);

    @Query("SELECT p FROM Passenger p WHERE p.email = :email")
    List<Passenger> findByEmail(@Param("email") String email);
}
