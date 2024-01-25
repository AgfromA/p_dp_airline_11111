package app.services;


import app.dto.FlightSeatDto;
import app.dto.SeatDto;
import app.entities.*;
import app.exceptions.EntityNotFoundException;
import app.mappers.FlightSeatMapper;
import app.repositories.FlightSeatRepository;
import app.repositories.SeatRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class FlightSeatServiceTest {
    @Mock
    private FlightSeatRepository flightSeatRepository;
    @Mock
    private SeatRepository seatRepository;
    @Mock
    private SeatService seatService;
    @Mock
    private FlightService flightService;
    @Mock
    private FlightSeatMapper flightSeatMapper;
    @InjectMocks
    private FlightSeatService flightSeatService;

    @Test
    void testGetAllFlightSeatsWithReturnList() {
        List<FlightSeat> flightSeatList = new ArrayList<>();
        flightSeatList.add(new FlightSeat());
        flightSeatList.add(new FlightSeat());

        when(flightSeatRepository.findAll()).thenReturn(flightSeatList);

        List<FlightSeatDto> expectedFlightSeatDtoList = new ArrayList<>();
        expectedFlightSeatDtoList.add(new FlightSeatDto());
        expectedFlightSeatDtoList.add(new FlightSeatDto());

        when(flightSeatMapper.toDtoList(flightSeatList, flightService)).thenReturn(expectedFlightSeatDtoList);

        List<FlightSeatDto> actualFlightSeatDtoList = flightSeatService.getAllFlightSeats();

        assertNotNull(actualFlightSeatDtoList);
        assertEquals(expectedFlightSeatDtoList, actualFlightSeatDtoList);
        verify(flightSeatRepository, times(1)).findAll();
        verify(flightSeatMapper, times(1)).toDtoList(flightSeatList, flightService);
    }

    @Test
    void testGetAllFlightSeats() {
        FlightSeat flightSeat = new FlightSeat();
        FlightSeatDto flightSeatDto = new FlightSeatDto();
        when(flightSeatMapper.toDto(any(), any())).thenReturn(flightSeatDto);
        when(flightSeatRepository.findAll(any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(flightSeat)));

        Page<FlightSeatDto> result = flightSeatService.getAllFlightSeats(0, 10);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertTrue(result.hasContent());
        verify(flightSeatRepository, times(1)).findAll(any(PageRequest.class));
    }

//    public Page<FlightSeatDto> getAllFlightSeatsFiltered(Integer page, Integer size,
//    Long flightId, Boolean isSold, Boolean isRegistered) {
//        Pageable pageable = PageRequest.of(page, size);
//        if (Boolean.FALSE.equals(isSold) && Boolean.FALSE.equals(isRegistered)) {
//            return flightSeatRepository
//                .findFlightSeatByFlightIdAndIsSoldFalseAndIsRegisteredFalseAndIsBookedFalse(flightId, pageable)
//                .map(entity -> flightSeatMapper.toDto(entity, flightService));
//        } else if (Boolean.FALSE.equals(isSold)) {
//            return flightSeatRepository.findAllFlightsSeatByFlightIdAndIsSoldFalse(flightId, pageable)
//                .map(entity -> flightSeatMapper.toDto(entity, flightService));
//        } else if (Boolean.FALSE.equals(isRegistered)) {
//            return flightSeatRepository.findAllFlightsSeatByFlightIdAndIsRegisteredFalse(flightId, pageable)
//                .map(entity -> flightSeatMapper.toDto(entity, flightService));
//        } else {
//            return flightSeatRepository.findFlightSeatsByFlightId(flightId, pageable)
//                .map(entity -> flightSeatMapper.toDto(entity, flightService));
//        }
//    }
//  Не работает из-за использования приватных методов. Не видит их в этом классе


//    @Test
//    void testGetAllFlightSeatsFiltered_NotSoldAndNotRegisteredSeats() {
//        // Arrange
//        Integer page = 0;
//        Integer size = 10;
//        Long flightId = 1L;
//        Boolean isSold = false;
//        Boolean isRegistered = false;
//
//        Pageable pageable = PageRequest.of(page, size);
//        Page<FlightSeat> flightSeatPage = new PageImpl<>(Collections.emptyList());
//        when(flightSeatRepository
//                .findFlightSeatByFlightIdAndIsSoldFalseAndIsRegisteredFalseAndIsBookedFalse(flightId, pageable))
//                .thenReturn(flightSeatPage);
//
//        // Act
//        Page<FlightSeatDto> result = flightSeatService.getAllFlightSeatsFiltered(page, size, flightId, isSold, isRegistered);
//
//        // Assert
//        assertEquals(flightSeatPage, result);
//        verify(flightSeatRepository, times(1))
//                .findFlightSeatByFlightIdAndIsSoldFalseAndIsRegisteredFalseAndIsBookedFalse(flightId, pageable);
//        verify(flightSeatRepository, never())
//                .findAllFlightsSeatByFlightIdAndIsSoldFalse(anyLong(), any(Pageable.class));
//        verify(flightSeatRepository, never())
//                .findAllFlightsSeatByFlightIdAndIsRegisteredFalse(anyLong(), any(Pageable.class));
//        verify(flightSeatRepository, never())
//                .findFlightSeatsByFlightId(anyLong(), any(Pageable.class));
//    }
//
//    @Test
//    void testGetAllFlightSeatsFiltered_NotSoldSeats() {
//        // Arrange
//        Integer page = 0;
//        Integer size = 10;
//        Long flightId = 1L;
//        Boolean isSold = false;
//        Boolean isRegistered = true;
//
//        Pageable pageable = PageRequest.of(page, size);
//        Page<FlightSeat> flightSeatPage = new PageImpl<>(Collections.emptyList());
//        when(flightSeatRepository.findAllFlightsSeatByFlightIdAndIsSoldFalse(flightId, pageable))
//                .thenReturn(flightSeatPage);
//
//        // Act
//        Page<FlightSeatDto> result = flightSeatService.getAllFlightSeatsFiltered(page, size, flightId, isSold, isRegistered);
//
//        // Assert
//        assertEquals(flightSeatPage, result);
//        verify(flightSeatRepository, never())
//                .findFlightSeatByFlightIdAndIsSoldFalseAndIsRegisteredFalseAndIsBookedFalse(anyLong(), any(Pageable.class));
//        verify(flightSeatRepository, times(1))
//                .findAllFlightsSeatByFlightIdAndIsSoldFalse(flightId, pageable);
//        verify(flightSeatRepository, never())
//                .findAllFlightsSeatByFlightIdAndIsRegisteredFalse(anyLong(), any(Pageable.class));
//        verify(flightSeatRepository, never())
//                .findFlightSeatsByFlightId(anyLong(), any(Pageable.class));
//    }
//
//    @Test
//    void testGetAllFlightSeatsFiltered_NotRegisteredSeats() {
//        // Arrange
//        Integer page = 0;
//        Integer size = 10;
//        Long flightId = 1L;
//        Boolean isSold = true;
//        Boolean isRegistered = false;
//
//        Pageable pageable = PageRequest.of(page, size);
//        Page<FlightSeat> flightSeatPage = new PageImpl<>(Collections.emptyList());
//        when(flightSeatRepository.findAllFlightsSeatByFlightIdAndIsRegisteredFalse(flightId, pageable))
//                .thenReturn(flightSeatPage);
//
//        // Act
//        Page<FlightSeatDto> result = flightSeatService.getAllFlightSeatsFiltered(page, size, flightId, isSold, isRegistered);
//
//        // Assert
//        assertEquals(flightSeatPage, result);
//        verify(flightSeatRepository, never())
//                .findFlightSeatByFlightIdAndIsSoldFalseAndIsRegisteredFalseAndIsBookedFalse(anyLong(), any(Pageable.class));
//        verify(flightSeatRepository, never())
//                .findAllFlightsSeatByFlightIdAndIsSoldFalse(anyLong(), any(Pageable.class));
//        verify(flightSeatRepository, times(1))
//                .findAllFlightsSeatByFlightIdAndIsRegisteredFalse(flightId, pageable);
//        verify(flightSeatRepository, never())
//                .findFlightSeatsByFlightId(anyLong(), any(Pageable.class));
//
//    }


    @Test
    void testGetFlightSeat() {
        FlightSeat flightSeat = new FlightSeat();
        Optional<FlightSeat> expectedFlightSeat = Optional.of(flightSeat);
        when(flightSeatRepository.findById(anyLong())).thenReturn(expectedFlightSeat);

        Optional<FlightSeat> result = flightSeatService.getFlightSeat(123L);

        assertTrue(result.isPresent());
        assertEquals(expectedFlightSeat, result);
        verify(flightSeatRepository, times(1)).findById(anyLong());
    }

    @Test
    void testGetFlightSeatDto() {
        FlightSeat flightSeat = new FlightSeat();
        FlightSeatDto expectedFlightSeatDto = new FlightSeatDto();
        when(flightSeatRepository.findById(anyLong())).thenReturn(Optional.of(flightSeat));
        when(flightSeatMapper.toDto(flightSeat, flightService)).thenReturn(expectedFlightSeatDto);

        Optional<FlightSeatDto> result = flightSeatService.getFlightSeatDto(anyLong());

        assertTrue(result.isPresent());
        assertEquals(expectedFlightSeatDto, result.get());
        verify(flightSeatRepository, times(1)).findById(anyLong());
        verify(flightSeatMapper, times(1)).toDto(flightSeat, flightService);
    }


    @Test
    void getFlightSeatsByFlightId() {
        FlightSeat flightSeat = new FlightSeat();
        FlightSeatDto flightSeatDto = new FlightSeatDto();
        when(flightSeatRepository.findFlightSeatsByFlightId(anyLong()))
                .thenReturn(new HashSet<>(List.of(flightSeat)));
        when(flightSeatMapper.toDto(any(), any())).thenReturn(flightSeatDto);

        List<FlightSeatDto> result = flightSeatService.getFlightSeatsByFlightId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(flightSeatRepository, times(1)).findFlightSeatsByFlightId(anyLong());
        verify(flightSeatMapper, times(1)).toDto(any(), any());
    }

    //    7) public FlightSeatDto createFlightSeat(FlightSeatDto flightSeatDto) {
//        var flightSeat = flightSeatMapper.toEntity(flightSeatDto, flightService, seatService);
//        var flight = checkIfFlightExists(flightSeatDto.getFlightId());
//        flightSeat.setFlight(flight);
//
//        var seat = seatService.getSeat(flightSeatDto.getSeat().getId());
//        if (seat == null) {
//            throw new EntityNotFoundException("Operation was not finished because Seat was not found with id = " + flightSeatDto.getFlightId());
//        }
//        flightSeat.setSeat(seat);
//
//        flightSeat.setId(null);
//        return flightSeatMapper.toDto(flightSeatRepository.save(flightSeat), flightService);
//    }
    @Test
    void testCreateFlightSeat() {
        // Arrange
        Seat seat = new Seat();
        seat.setId(1L);

        SeatDto seatDto = flightSeatMapper.toSeatDto(seat);

        Flight flight = new Flight();
        flight.setId(1L);

        FlightSeat flightSeat = new FlightSeat();
        flightSeat.setId(1L);
        flightSeat.setFlight(flight);
        flightSeat.setSeat(seat);

        FlightSeatDto flightSeatDto = new FlightSeatDto();
        flightSeatDto.setFlightId(1L);
        flightSeatDto.setSeat(seatDto);



        when(flightSeatMapper.toEntity(any(), flightService, seatService)).thenReturn(flightSeat);
        when(seatService.getSeat(flightSeatDto.getSeat().getId())).thenReturn(seat);
        when(flightSeatRepository.save(any())).thenReturn(flightSeat);
        when(flightSeatMapper.toDto(flightSeat, flightService)).thenReturn(flightSeatDto);

        // Act
        FlightSeatDto result = flightSeatService.createFlightSeat(flightSeatDto);

        // Assert
        assertNotNull(result);
        assertEquals(flightSeatDto, result);
        verify(flightSeatMapper, times(1)).toEntity(flightSeatDto, flightService, seatService);
        verify(seatService, times(1)).getSeat(flightSeatDto.getSeat().getId());
        verify(flightSeatRepository, times(1)).save(flightSeat);
        verify(flightSeatMapper, times(1)).toDto(flightSeat, flightService);
    }





















    @Test
    void testEditFlightSeat_WhenFlightSeatExists() {
        Long id = 1L;
        FlightSeatDto flightSeatDto = new FlightSeatDto();
        flightSeatDto.setFare(15_000);
        flightSeatDto.setIsSold(true);
        flightSeatDto.setIsBooked(false);
        flightSeatDto.setIsRegistered(true);

        FlightSeat existingFlightSeat = new FlightSeat();
        existingFlightSeat.setId(id);
        existingFlightSeat.setFare(10_000);
        existingFlightSeat.setIsSold(false);
        existingFlightSeat.setIsBooked(true);
        existingFlightSeat.setIsRegistered(false);

        when(flightSeatRepository.findById(id)).thenReturn(Optional.of(existingFlightSeat));
        when(flightSeatMapper.toDto(any(), any())).thenReturn(flightSeatDto);
        when(flightSeatRepository.save(existingFlightSeat)).thenReturn(existingFlightSeat);

        FlightSeatDto result = flightSeatService.editFlightSeat(id, flightSeatDto);

        assertNotNull(result);
        assertEquals(flightSeatDto.getFare(), result.getFare());
        assertEquals(flightSeatDto.getIsSold(), result.getIsSold());
        assertEquals(flightSeatDto.getIsBooked(), result.getIsBooked());
        assertEquals(flightSeatDto.getIsRegistered(), result.getIsRegistered());
        verify(flightSeatRepository, times(1)).findById(id);
        verify(flightSeatRepository, times(1)).save(existingFlightSeat);
        verify(flightSeatMapper, times(1)).toDto(existingFlightSeat, flightService);
    }

    @Test
    void testEditFlightSeat_WhenFlightSeatDoesNotExist() {
        Long id = 1L;
        FlightSeatDto flightSeatDto = new FlightSeatDto();

        when(flightSeatRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> flightSeatService.editFlightSeat(id, flightSeatDto));
        verify(flightSeatRepository, times(1)).findById(id);
        verify(flightSeatRepository, never()).save(any());
        verify(flightSeatMapper, never()).toDto(any(), any());
    }

    @Test
    void testGetNumberOfFreeSeatOnFlight1() {
        Flight flight = new Flight();
        flight.setId(1L);
        List<FlightSeat> flightSeats = List.of(new FlightSeat());
        flight.setSeats(flightSeats);
        when(flightSeatRepository
                .findFlightSeatByFlightIdAndIsSoldFalseAndIsRegisteredFalseAndIsBookedFalse(flight.getId()))
                .thenReturn(new HashSet<>(flightSeats));

        int numberOfFreeSeats = flightSeatService.getNumberOfFreeSeatOnFlight(flight);

        assertEquals(1, numberOfFreeSeats);
        verify(flightSeatRepository, times(1))
                .findFlightSeatByFlightIdAndIsSoldFalseAndIsRegisteredFalseAndIsBookedFalse(anyLong());
    }

    @Test
    void testDeleteFlightSeatById() {
        flightSeatService.deleteFlightSeatById(anyLong());

        verify(flightSeatRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void testMakeFlightSeatNotSold() {
        long[] flightSeatId = {1L, 2L, 3L};

        flightSeatService.makeFlightSeatNotSold(flightSeatId);

        verify(flightSeatRepository, times(1)).editIsSoldToFalseByFlightSeatId(flightSeatId);
    }

    @Test
    void testFindByFlightId() {
        Flight flight = new Flight();
        flight.setSeats(List.of(new FlightSeat(), new FlightSeat(), new FlightSeat()));
        flight.setId(1L);
        List<FlightSeat> expectedFlightSeats = flight.getSeats();
        when(flightSeatRepository.findByFlightId(flight.getId())).thenReturn(expectedFlightSeats);


        List<FlightSeat> actualFlightSeats = flightSeatService.findByFlightId(flight.getId());

        assertNotNull(actualFlightSeats);
        assertEquals(expectedFlightSeats.size(), actualFlightSeats.size());
        assertEquals(expectedFlightSeats, actualFlightSeats);
        verify(flightSeatRepository, times(1)).findByFlightId(flight.getId());
    }

//    13)
//    public List<FlightSeatDto> generateFlightSeats(Long flightId) {
//        var flight = checkIfFlightExists(flightId);
//        var flightSeats = getFlightSeatsByFlightId(flightId);
//        if (!flightSeats.isEmpty()) {
//            return flightSeats;
//        }
//        List<FlightSeat> newFlightSeats = new ArrayList<>();
//        var seats = seatRepository.findByAircraftId(flight.getAircraft().getId());
//        for (Seat seat : seats) {
//            newFlightSeats.add(generateFlightSeat(seat, flight));
//        }
//        flightSeatRepository.saveAll(newFlightSeats);
//        return flightSeatMapper.toDtoList(newFlightSeats, flightService);
//    }
//    Нужно доработать!!

//    @Test
//    void testGenerateFlightSeats() {
//        // Arrange
//        Long flightId = 1L;
//        Flight flight = new Flight();
//        flight.setId(flightId);
//        Aircraft aircraft = new Aircraft();
//        aircraft.setId(1L);
//        flight.setAircraft(aircraft);
//        List<FlightSeat> flightSeats = List.of(new FlightSeat());
//        List<Seat> seats = new ArrayList<>();
//        Seat seat1 = new Seat();
//        seat1.setId(1L);
//        seats.add(seat1);
//        when(flightSeatService.checkIfFlightExists(flightId)).thenReturn(flight);
//        when(flightSeatService.getFlightSeatsByFlightId(flightId)).thenReturn(flightSeats);
//        when(seatRepository.findByAircraftId(aircraft.getId())).thenReturn(seats);
//        FlightSeat newFlightSeat = new FlightSeat();
//        newFlightSeat.setId(1L);
//        when(flightSeatService.generateFlightSeat(seat1, flight)).thenReturn(newFlightSeat);
//        List<FlightSeatDto> expectedFlightSeatDtoList = new ArrayList<>();
//        expectedFlightSeatDtoList.add(new FlightSeatDto());
//        when(flightSeatMapper.toDtoList(anyList(), any())).thenReturn(expectedFlightSeatDtoList);
//
//        // Act
//        List<FlightSeatDto> result = flightSeatService.generateFlightSeats(flightId);
//
//        // Assert
//        assertNotNull(result);
//        assertEquals(expectedFlightSeatDtoList, result);
//        verify(flightSeatRepository, times(1)).saveAll(anyList());
//        verify(flightSeatMapper, times(1)).toDtoList(anyList(), any());
//    }
//


}













