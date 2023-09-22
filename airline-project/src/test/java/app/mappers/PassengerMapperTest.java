package app.mappers;

import app.dto.PassengerDTO;
import app.entities.Passenger;
import app.entities.Passport;
import app.enums.Gender;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PassengerMapperTest {

    private final PassengerMapper passengerMapper = Mappers.getMapper(PassengerMapper.class);

   @Test
    void testConvertToPassengerDTO() {
       Passenger passenger = new Passenger();
       Passport passport = new Passport();
       passport.setGender(Gender.MALE);
       passport.setPassportIssuingCountry("Russia");
       passport.setPassportIssuingDate(LocalDate.MIN);
       passport.setMiddleName("Ivanovich");
       passport.setSerialNumberPassport("999999");
       passenger.setId(1L);
       passenger.setFirstName("Ivan");
       passenger.setLastName("Ivanov");
       passenger.setEmail("example@email.com");
       passenger.setBirthDate(LocalDate.EPOCH);
       passenger.setPhoneNumber("+79999999999");
       passenger.setPassport(passport);

       PassengerDTO passengerDTO = passengerMapper.convertToPassengerDTO(passenger);

       assertEquals(passenger.getId(), passengerDTO.getId());
       assertEquals(passenger.getFirstName(), passengerDTO.getFirstName());
       assertEquals(passenger.getLastName(), passengerDTO.getLastName());
       assertEquals(passenger.getPhoneNumber(), passengerDTO.getPhoneNumber());
       assertEquals(passenger.getPhoneNumber(), passengerDTO.getPhoneNumber());
       assertEquals(passenger.getEmail(), passengerDTO.getEmail());
       assertEquals(passenger.getPassport(), passengerDTO.getPassport());

   }

   @Test
   void testConvertToPassenger() {
       Passport passport = new Passport();
       passport.setGender(Gender.MALE);
       passport.setPassportIssuingCountry("Russia");
       passport.setPassportIssuingDate(LocalDate.MIN);
       passport.setMiddleName("Ivanovich");
       passport.setSerialNumberPassport("999999");
       PassengerDTO passengerDTO = new PassengerDTO();
       passengerDTO.setId(1L);
       passengerDTO.setFirstName("Ivan");
       passengerDTO.setLastName("Ivanov");
       passengerDTO.setPhoneNumber("+79999999999");
       passengerDTO.setEmail("example@email.com");
       passengerDTO.setPassport(passport);
       passengerDTO.setBirthDate(LocalDate.MIN);

       Passenger passenger = passengerMapper.convertToPassenger(passengerDTO);

       assertEquals(passenger.getId(), passengerDTO.getId());
       assertEquals(passenger.getFirstName(), passengerDTO.getFirstName());
       assertEquals(passenger.getLastName(), passengerDTO.getLastName());
       assertEquals(passenger.getPhoneNumber(), passengerDTO.getPhoneNumber());
       assertEquals(passenger.getPhoneNumber(), passengerDTO.getPhoneNumber());
       assertEquals(passenger.getEmail(), passengerDTO.getEmail());
       assertEquals(passenger.getPassport(), passengerDTO.getPassport());
   }
}
