package app.mappers;

import app.dto.PassengerDTO;
import app.entities.Passenger;
import app.entities.Passport;
import app.enums.Gender;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    @Test
    void testConvertToPassengerDTOList() {
        List<Passenger> passengerList = new ArrayList<>();

        Passenger passenger1 = new Passenger();
        Passport passport1 = new Passport();
        passport1.setGender(Gender.MALE);
        passport1.setPassportIssuingCountry("Russia");
        passport1.setPassportIssuingDate(LocalDate.MIN);
        passport1.setMiddleName("Ivanovich");
        passport1.setSerialNumberPassport("999999");
        passenger1.setId(1L);
        passenger1.setFirstName("Ivan");
        passenger1.setLastName("Ivanov");
        passenger1.setEmail("example@email.com");
        passenger1.setBirthDate(LocalDate.EPOCH);
        passenger1.setPhoneNumber("+79999999999");
        passenger1.setPassport(passport1);

        Passenger passenger2 = new Passenger();
        Passport passport2 = new Passport();
        passport2.setGender(Gender.FEMALE);
        passport2.setPassportIssuingCountry("Russia");
        passport2.setPassportIssuingDate(LocalDate.MIN);
        passport2.setMiddleName("Ivanovich2");
        passport2.setSerialNumberPassport("999998");
        passenger2.setId(2L);
        passenger2.setFirstName("Olga");
        passenger2.setLastName("Ivanova");
        passenger2.setEmail("example1@email.com");
        passenger2.setBirthDate(LocalDate.EPOCH);
        passenger2.setPhoneNumber("+79999999998");
        passenger2.setPassport(passport2);

        passengerList.add(passenger1);
        passengerList.add(passenger2);


        List<PassengerDTO> passengerDTOList = passengerMapper.convertToPassengerDTOList(passengerList);

        assertEquals(passengerList.size(), passengerDTOList.size());
        assertEquals(passengerList.get(0).getId(), passengerDTOList.get(0).getId());
        assertEquals(passengerList.get(0).getFirstName(), passengerDTOList.get(0).getFirstName());
        assertEquals(passengerList.get(0).getLastName(), passengerDTOList.get(0).getLastName());
        assertEquals(passengerList.get(0).getPhoneNumber(), passengerDTOList.get(0).getPhoneNumber());
        assertEquals(passengerList.get(0).getPhoneNumber(), passengerDTOList.get(0).getPhoneNumber());
        assertEquals(passengerList.get(0).getEmail(), passengerDTOList.get(0).getEmail());
        assertEquals(passengerList.get(0).getPassport(), passengerDTOList.get(0).getPassport());

        assertEquals(passengerList.get(1).getId(), passengerDTOList.get(1).getId());
        assertEquals(passengerList.get(1).getFirstName(), passengerDTOList.get(1).getFirstName());
        assertEquals(passengerList.get(1).getLastName(), passengerDTOList.get(1).getLastName());
        assertEquals(passengerList.get(1).getPhoneNumber(), passengerDTOList.get(1).getPhoneNumber());
        assertEquals(passengerList.get(1).getPhoneNumber(), passengerDTOList.get(1).getPhoneNumber());
        assertEquals(passengerList.get(1).getEmail(), passengerDTOList.get(1).getEmail());
        assertEquals(passengerList.get(1).getPassport(), passengerDTOList.get(1).getPassport());
    }

    @Test
    void testConvertToPassengerList() {
        List<PassengerDTO> passengerDTOList = new ArrayList<>();

        PassengerDTO passengerDTO1 = new PassengerDTO();
        Passport passport1 = new Passport();
        passport1.setGender(Gender.MALE);
        passport1.setPassportIssuingCountry("Russia");
        passport1.setPassportIssuingDate(LocalDate.MIN);
        passport1.setMiddleName("Ivanovich");
        passport1.setSerialNumberPassport("999999");
        passengerDTO1.setId(1L);
        passengerDTO1.setFirstName("Ivan");
        passengerDTO1.setLastName("Ivanov");
        passengerDTO1.setEmail("example@email.com");
        passengerDTO1.setBirthDate(LocalDate.EPOCH);
        passengerDTO1.setPhoneNumber("+79999999999");
        passengerDTO1.setPassport(passport1);

        PassengerDTO passengerDTO2 = new PassengerDTO();
        Passport passport2 = new Passport();
        passport2.setGender(Gender.FEMALE);
        passport2.setPassportIssuingCountry("Russia");
        passport2.setPassportIssuingDate(LocalDate.MIN);
        passport2.setMiddleName("Ivanovich2");
        passport2.setSerialNumberPassport("999998");
        passengerDTO2.setId(2L);
        passengerDTO2.setFirstName("Olga");
        passengerDTO2.setLastName("Ivanova");
        passengerDTO2.setEmail("example1@email.com");
        passengerDTO2.setBirthDate(LocalDate.EPOCH);
        passengerDTO2.setPhoneNumber("+79999999998");
        passengerDTO2.setPassport(passport2);

        passengerDTOList.add(passengerDTO1);
        passengerDTOList.add(passengerDTO2);

        List<Passenger> passengerList = passengerMapper.convertToPassengerList(passengerDTOList);

        assertEquals(passengerDTOList.size(), passengerList.size());
        assertEquals(passengerDTOList.get(0).getId(), passengerList.get(0).getId());
        assertEquals(passengerDTOList.get(0).getFirstName(), passengerList.get(0).getFirstName());
        assertEquals(passengerDTOList.get(0).getLastName(), passengerList.get(0).getLastName());
        assertEquals(passengerDTOList.get(0).getPhoneNumber(), passengerList.get(0).getPhoneNumber());
        assertEquals(passengerDTOList.get(0).getPhoneNumber(), passengerList.get(0).getPhoneNumber());
        assertEquals(passengerDTOList.get(0).getEmail(), passengerList.get(0).getEmail());
        assertEquals(passengerDTOList.get(0).getPassport(), passengerList.get(0).getPassport());

        assertEquals(passengerDTOList.get(1).getId(), passengerList.get(1).getId());
        assertEquals(passengerDTOList.get(1).getFirstName(), passengerList.get(1).getFirstName());
        assertEquals(passengerDTOList.get(1).getLastName(), passengerList.get(1).getLastName());
        assertEquals(passengerDTOList.get(1).getPhoneNumber(), passengerList.get(1).getPhoneNumber());
        assertEquals(passengerDTOList.get(1).getPhoneNumber(), passengerList.get(1).getPhoneNumber());
        assertEquals(passengerDTOList.get(1).getEmail(), passengerList.get(1).getEmail());
        assertEquals(passengerDTOList.get(1).getPassport(), passengerList.get(1).getPassport());
    }
}
