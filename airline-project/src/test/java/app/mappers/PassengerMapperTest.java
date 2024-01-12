package app.mappers;

import app.dto.PassengerDto;
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

        PassengerDto passengerDTO = passengerMapper.toDto(passenger);

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
        PassengerDto passengerDTO = new PassengerDto();
        passengerDTO.setId(1L);
        passengerDTO.setFirstName("Ivan");
        passengerDTO.setLastName("Ivanov");
        passengerDTO.setPhoneNumber("+79999999999");
        passengerDTO.setEmail("example@email.com");
        passengerDTO.setPassport(passport);
        passengerDTO.setBirthDate(LocalDate.MIN);

        Passenger passenger = passengerMapper.toEntity(passengerDTO);

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


        List<PassengerDto> passengerDtoList = passengerMapper.toDtoList(passengerList);

        assertEquals(passengerList.size(), passengerDtoList.size());
        assertEquals(passengerList.get(0).getId(), passengerDtoList.get(0).getId());
        assertEquals(passengerList.get(0).getFirstName(), passengerDtoList.get(0).getFirstName());
        assertEquals(passengerList.get(0).getLastName(), passengerDtoList.get(0).getLastName());
        assertEquals(passengerList.get(0).getPhoneNumber(), passengerDtoList.get(0).getPhoneNumber());
        assertEquals(passengerList.get(0).getPhoneNumber(), passengerDtoList.get(0).getPhoneNumber());
        assertEquals(passengerList.get(0).getEmail(), passengerDtoList.get(0).getEmail());
        assertEquals(passengerList.get(0).getPassport(), passengerDtoList.get(0).getPassport());

        assertEquals(passengerList.get(1).getId(), passengerDtoList.get(1).getId());
        assertEquals(passengerList.get(1).getFirstName(), passengerDtoList.get(1).getFirstName());
        assertEquals(passengerList.get(1).getLastName(), passengerDtoList.get(1).getLastName());
        assertEquals(passengerList.get(1).getPhoneNumber(), passengerDtoList.get(1).getPhoneNumber());
        assertEquals(passengerList.get(1).getPhoneNumber(), passengerDtoList.get(1).getPhoneNumber());
        assertEquals(passengerList.get(1).getEmail(), passengerDtoList.get(1).getEmail());
        assertEquals(passengerList.get(1).getPassport(), passengerDtoList.get(1).getPassport());
    }

    @Test
    void testConvertToPassengerList() {
        List<PassengerDto> passengerDtoList = new ArrayList<>();

        PassengerDto passengerDto1 = new PassengerDto();
        Passport passport1 = new Passport();
        passport1.setGender(Gender.MALE);
        passport1.setPassportIssuingCountry("Russia");
        passport1.setPassportIssuingDate(LocalDate.MIN);
        passport1.setMiddleName("Ivanovich");
        passport1.setSerialNumberPassport("999999");
        passengerDto1.setId(1L);
        passengerDto1.setFirstName("Ivan");
        passengerDto1.setLastName("Ivanov");
        passengerDto1.setEmail("example@email.com");
        passengerDto1.setBirthDate(LocalDate.EPOCH);
        passengerDto1.setPhoneNumber("+79999999999");
        passengerDto1.setPassport(passport1);

        PassengerDto passengerDto2 = new PassengerDto();
        Passport passport2 = new Passport();
        passport2.setGender(Gender.FEMALE);
        passport2.setPassportIssuingCountry("Russia");
        passport2.setPassportIssuingDate(LocalDate.MIN);
        passport2.setMiddleName("Ivanovich2");
        passport2.setSerialNumberPassport("999998");
        passengerDto2.setId(2L);
        passengerDto2.setFirstName("Olga");
        passengerDto2.setLastName("Ivanova");
        passengerDto2.setEmail("example1@email.com");
        passengerDto2.setBirthDate(LocalDate.EPOCH);
        passengerDto2.setPhoneNumber("+79999999998");
        passengerDto2.setPassport(passport2);

        passengerDtoList.add(passengerDto1);
        passengerDtoList.add(passengerDto2);

        List<Passenger> passengerList = passengerMapper.toEntityList(passengerDtoList);

        assertEquals(passengerDtoList.size(), passengerList.size());
        assertEquals(passengerDtoList.get(0).getId(), passengerList.get(0).getId());
        assertEquals(passengerDtoList.get(0).getFirstName(), passengerList.get(0).getFirstName());
        assertEquals(passengerDtoList.get(0).getLastName(), passengerList.get(0).getLastName());
        assertEquals(passengerDtoList.get(0).getPhoneNumber(), passengerList.get(0).getPhoneNumber());
        assertEquals(passengerDtoList.get(0).getPhoneNumber(), passengerList.get(0).getPhoneNumber());
        assertEquals(passengerDtoList.get(0).getEmail(), passengerList.get(0).getEmail());
        assertEquals(passengerDtoList.get(0).getPassport(), passengerList.get(0).getPassport());

        assertEquals(passengerDtoList.get(1).getId(), passengerList.get(1).getId());
        assertEquals(passengerDtoList.get(1).getFirstName(), passengerList.get(1).getFirstName());
        assertEquals(passengerDtoList.get(1).getLastName(), passengerList.get(1).getLastName());
        assertEquals(passengerDtoList.get(1).getPhoneNumber(), passengerList.get(1).getPhoneNumber());
        assertEquals(passengerDtoList.get(1).getPhoneNumber(), passengerList.get(1).getPhoneNumber());
        assertEquals(passengerDtoList.get(1).getEmail(), passengerList.get(1).getEmail());
        assertEquals(passengerDtoList.get(1).getPassport(), passengerList.get(1).getPassport());
    }
}
