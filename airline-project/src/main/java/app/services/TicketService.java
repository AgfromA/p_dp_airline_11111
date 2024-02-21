package app.services;

import app.dto.TicketDto;
import app.entities.Ticket;

import app.enums.BookingStatus;
import app.exceptions.*;
import app.mappers.TicketMapper;
import app.repositories.TicketRepository;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;
    private final PassengerService passengerService;
    private final FlightService flightService;
    private final FlightSeatService flightSeatService;
    private final BookingService bookingService;
    private final Random random = new Random();

    public List<TicketDto> getAllTickets() {
        return ticketMapper.toDtoList(ticketRepository.findAll());
    }

    public Page<TicketDto> getAllTickets(int page, int size) {
        return ticketRepository.findAll(PageRequest.of(page, size))
                .map(ticketMapper::toDto);
    }

    public Ticket getTicketByTicketNumber(String ticketNumber) {
        return ticketRepository.findByTicketNumberContainingIgnoreCase(ticketNumber);
    }

    @Transactional
    public void deleteTicketById(Long id) {
        checkIfTicketExist(id);
        ticketRepository.deleteById(id);
    }

    @Transactional
    public Ticket saveTicket(TicketDto ticketDto) {

        passengerService.checkIfPassengerExists(ticketDto.getPassengerId());
        flightSeatService.checkIfFlightSeatExist(ticketDto.getFlightSeatId());
        var booking = bookingService.checkIfBookingExist(ticketDto.getBookingId());

        var existingTicket = ticketRepository.findByBookingId(ticketDto.getBookingId());
        if (existingTicket.isPresent()) {
            throw new DuplicateFieldException("Ticket with bookingId " + ticketDto.getBookingId() + " already exists!");
        }
        if (!booking.getFlightSeat().getId().equals(ticketDto.getFlightSeatId())) {
            throw new WrongArgumentException("Ticket should have the same flightSeatId as Booking with bookingId " + ticketDto.getBookingId());
        }
        if (!booking.getPassenger().getId().equals(ticketDto.getPassengerId())) {
            throw new WrongArgumentException("Ticket should have the same passengerId as Booking with bookingId " + ticketDto.getBookingId());
        }
        if (booking.getBookingStatus() != BookingStatus.PAID) {
            throw new FlightSeatNotPaidException(ticketDto.getFlightSeatId());
        }
        if (ticketDto.getTicketNumber() != null && ticketRepository.existsByTicketNumber(ticketDto.getTicketNumber())) {
            throw new TicketNumberException(ticketDto.getTicketNumber());
        } else {
            ticketDto.setTicketNumber(generateTicketNumber());
        }

        var ticket = ticketMapper.toEntity(ticketDto, passengerService, flightService, flightSeatService, bookingService);
        return ticketRepository.save(ticket);
    }

    @Transactional
    public Ticket generatePaidTicket(Long bookingId) {
        var existingTicket = ticketRepository.findByBookingId(bookingId);
        if (existingTicket.isPresent()) {
            return existingTicket.get();
        }
        var booking = bookingService.checkIfBookingExist(bookingId);

        if (booking.getBookingStatus() != BookingStatus.PAID) {
            throw new UnPaidBookingException(bookingId);
        } else {
            var ticket = new Ticket();
            ticket.setBooking(booking);
            ticket.setPassenger(booking.getPassenger());
            ticket.setFlightSeat(booking.getFlightSeat());
            ticket.setTicketNumber(generateTicketNumber());
            return ticketRepository.save(ticket);
        }
    }

    @Transactional
    public Ticket updateTicketById(Long id, TicketDto ticketDto) {
        var existingTicket = checkIfTicketExist(id);
        var existingBooking = existingTicket.getBooking();

        if (ticketDto.getBookingId() != existingBooking.getId()) {
            throw new WrongArgumentException("Ticket's Booking can't be changed");
        }
        if (ticketDto.getPassengerId() != null
                && !ticketDto.getPassengerId().equals(existingTicket.getPassenger().getId())
                && ticketDto.getPassengerId().equals(existingBooking.getPassenger().getId())) {
            // FIXME надо бы эксепшн выбраисывать, если пришедший айди пассажира не совпадает с айди пассажира у связанного бронирования
            existingTicket.setPassenger(existingBooking.getPassenger());
        }
        if (ticketDto.getFlightSeatId() != null
                && !ticketDto.getFlightSeatId().equals(existingTicket.getFlightSeat().getId())
                && ticketDto.getFlightSeatId().equals(existingBooking.getFlightSeat().getId())) {
            // FIXME надо бы эксепшн выбраисывать, если пришедший айди сиденья не совпадает с айди сиденья у связанного бронирования
            existingTicket.setFlightSeat(existingBooking.getFlightSeat());
        }
        if (ticketDto.getTicketNumber() != null
                && !ticketDto.getTicketNumber().equals(existingTicket.getTicketNumber())
                && !ticketRepository.existsByTicketNumber(ticketDto.getTicketNumber())) {
            existingTicket.setTicketNumber(ticketDto.getTicketNumber());
        }
        return ticketRepository.save(existingTicket);
    }

    public long[] getFlightSeatIdsByPassengerId(long passengerId) {
        return ticketRepository.findArrayOfFlightSeatIdByPassengerId(passengerId);
    }

    @Transactional
    public void deleteTicketByPassengerId(long passengerId) {
        ticketRepository.deleteTicketByPassengerId(passengerId);
    }

    public String generateTicketNumber() {
        StringBuilder ticketNumberBuilder;
        do {
            ticketNumberBuilder = new StringBuilder();

            for (int i = 0; i < 2; i++) {
                char letter = (char) (random.nextInt(26) + 'A');
                ticketNumberBuilder.append(letter);
            }

            ticketNumberBuilder.append("-");

            for (int i = 0; i < 4; i++) {
                int digit = random.nextInt(10);
                ticketNumberBuilder.append(digit);
            }
        } while (ticketRepository.existsByTicketNumber(ticketNumberBuilder.toString()));
        return ticketNumberBuilder.toString();
    }

    public List<Ticket> getAllTicketsForEmailNotification(LocalDateTime departureIn, LocalDateTime gap) {
        return ticketRepository.getAllTicketsForEmailNotification(departureIn, gap);
    }

    public Ticket checkIfTicketExist(Long ticketId) {
        return ticketRepository.findTicketById(ticketId).orElseThrow(
                () -> new EntityNotFoundException("Operation was not finished because Ticket was not found with id = " + ticketId)
        );
    }

    public String getTicketPdfByTicketNumber(String ticketNumber) {
        var ticket = ticketRepository.findByTicketNumberContainingIgnoreCase(ticketNumber);
        String pathToPdf =
                "airline-project\\src\\main\\resources\\ticketsPdf\\ticket" + ticket.getTicketNumber() + ".pdf";

        try {
            Rectangle pageSize = new Rectangle(PageSize.A4);
            pageSize.setBackgroundColor(new BaseColor(173, 216, 230));
            Document document = new Document(pageSize);
            PdfWriter.getInstance(document, new FileOutputStream(pathToPdf));
            document.open();

            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
            headerFont.setSize(35);
            headerFont.setColor(BaseColor.GREEN);
            Paragraph header = new Paragraph("S7 Airline Ticket", headerFont);
            header.setAlignment(Element.ALIGN_CENTER);
            document.add(header);

            Font flightInfoFont = FontFactory.getFont(FontFactory.HELVETICA);
            flightInfoFont.setColor(BaseColor.ORANGE);
            flightInfoFont.setSize(18);
            Font flightDetailsFont = FontFactory.getFont(FontFactory.HELVETICA);
            flightDetailsFont.setColor(BaseColor.BLACK);
            flightDetailsFont.setSize(16);

            Chunk flightNumberChunk = new Chunk("Flight Number:  ", flightInfoFont);
            var flightNumberInDb = ticket.getFlightSeat().getFlight().getCode();
            Chunk flightNumberValue = new Chunk(flightNumberInDb, flightDetailsFont);
            Paragraph flightNumber = new Paragraph(60);
            flightNumber.setIndentationLeft(50);
            flightNumber.add(flightNumberChunk);
            flightNumber.add(flightNumberValue);
            document.add(flightNumber);

            Chunk departureAirportChunk = new Chunk("Departure Airport:  ", flightInfoFont);
            var departureAirportInDb = ticket.getFlightSeat().getFlight().getFrom().getAirportCode().toString();
            Chunk departureAirportValue = new Chunk(departureAirportInDb, flightDetailsFont);
            Paragraph departureAirport = new Paragraph(30);
            departureAirport.setIndentationLeft(50);
            departureAirport.add(departureAirportChunk);
            departureAirport.add(departureAirportValue);
            document.add(departureAirport);

            Chunk departureTimeChunk = new Chunk("Departure time:  ", flightInfoFont);
            var departureTimeInDb = ticket.getFlightSeat().getFlight().getDepartureDateTime().toString();
            Chunk departureTimeValue = new Chunk(departureTimeInDb, flightDetailsFont);
            Paragraph departureTime = new Paragraph(30);
            departureTime.setIndentationLeft(50);
            departureTime.add(departureTimeChunk);
            departureTime.add(departureTimeValue);
            document.add(departureTime);

            Chunk arrivalAirportChunk = new Chunk("Arrival Airport:  ", flightInfoFont);
            var arrivalAirportInDb = ticket.getFlightSeat().getFlight().getTo().getAirportCode().toString();
            Chunk arrivalAirportValue = new Chunk(arrivalAirportInDb, flightDetailsFont);
            Paragraph arrivalAirport = new Paragraph(30);
            arrivalAirport.setIndentationLeft(50);
            arrivalAirport.add(arrivalAirportChunk);
            arrivalAirport.add(arrivalAirportValue);
            document.add(arrivalAirport);

            Chunk arrivalTimeChunk = new Chunk("Arrival time:  ", flightInfoFont);
            var arrivalTimeInDb = ticket.getFlightSeat().getFlight().getArrivalDateTime().toString();
            Chunk arrivalTimeValue = new Chunk(arrivalTimeInDb, flightDetailsFont);
            Paragraph arrivalTime = new Paragraph(30);
            arrivalTime.setIndentationLeft(50);
            arrivalTime.add(arrivalTimeChunk);
            arrivalTime.add(arrivalTimeValue);
            document.add(arrivalTime);

            Chunk passengerNameChunk = new Chunk("Passenger name:  ", flightInfoFont);
            var passengerNameInDb = ticket.getPassenger().getFirstName();
            Chunk passengerNameValue = new Chunk(passengerNameInDb, flightDetailsFont);
            Paragraph passengerName = new Paragraph(30);
            passengerName.setIndentationLeft(50);
            passengerName.add(passengerNameChunk);
            passengerName.add(passengerNameValue);
            document.add(passengerName);

            Chunk passengerSurnameChunk = new Chunk("Passenger surname:  ", flightInfoFont);
            var passengerSurnameInDb = ticket.getPassenger().getPassport().getMiddleName();
            Chunk passengerSurnameValue = new Chunk(passengerSurnameInDb, flightDetailsFont);
            Paragraph passengerSurname = new Paragraph(30);
            passengerSurname.setIndentationLeft(50);
            passengerSurname.add(passengerSurnameChunk);
            passengerSurname.add(passengerSurnameValue);
            document.add(passengerSurname);

            Chunk passengerPassportDetailsChunk = new Chunk("Passport:  ", flightInfoFont);
            var passengerPassportInDb = ticket.getPassenger().getPassport().getSerialNumberPassport();
            Chunk passengerPassportValue = new Chunk(passengerPassportInDb, flightDetailsFont);
            Paragraph passengerPassport = new Paragraph(30);
            passengerPassport.setIndentationLeft(50);
            passengerPassport.add(passengerPassportDetailsChunk);
            passengerPassport.add(passengerPassportValue);
            document.add(passengerPassport);

            Chunk seatNumberChunk = new Chunk("Seat Number:  ", flightInfoFont);
            var seatNumberInDb = ticket.getFlightSeat().getSeat().getSeatNumber();
            Chunk seatNumberValue = new Chunk(seatNumberInDb, flightDetailsFont);
            Paragraph seatNumber = new Paragraph(30);
            seatNumber.setIndentationLeft(50);
            seatNumber.add(seatNumberChunk);
            seatNumber.add(seatNumberValue);
            document.add(seatNumber);

            Chunk ticketPriceChunk = new Chunk("Ticket Price:  ", flightInfoFont);
            var ticketPriceInDb = ticket.getFlightSeat().getFare().toString();
            Chunk ticketPriceValue = new Chunk(ticketPriceInDb, flightDetailsFont);
            Paragraph ticketPrice = new Paragraph(30);
            ticketPrice.setIndentationLeft(50);
            ticketPrice.add(ticketPriceChunk);
            ticketPrice.add(ticketPriceValue);
            document.add(ticketPrice);

            document.close();
            deletePdfTicketInServer(pathToPdf);

        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
        return pathToPdf;
    }

    private void deletePdfTicketInServer(String pathToPdfTicket) {
        File fileToDelete = new File(pathToPdfTicket);
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

        executor.schedule(() -> {
            try {
                Files.deleteIfExists(fileToDelete.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                executor.shutdown();
            }
        },  1, TimeUnit.MINUTES);
    }
}