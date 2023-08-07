package app.controllers.api.rest;

import app.dto.BookingDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@RequestMapping("/api/bookings")
@Api(tags = "Booking REST")
@Tag(name = "Booking REST", description = "API для операций с бронированием")
public interface BookingRestApi {

    @GetMapping
    @ApiOperation(value = "Get list of all Bookings")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Bookings found"),
            @ApiResponse(code = 204, message = "Bookings not found")})
    ResponseEntity<Page<BookingDTO>> getAllPagesBookingsDTO(Pageable pageable);

    @GetMapping("/{id}")
    @ApiOperation(value = "Get Booking by \"id\"")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Booking found"),
            @ApiResponse(code = 404, message = "Booking not found")
    })
    ResponseEntity<BookingDTO> getBookingDTOById(
            @ApiParam(
                    name = "id",
                    value = "Booking.id"
            )
            @PathVariable("id") Long id);

    @GetMapping("/number")
    @ApiOperation(value = "Get Booking by bookingNumber")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Booking found"),
            @ApiResponse(code = 404, message = "Booking not found")
    })
    ResponseEntity<BookingDTO> getBookingDTOByBookingNumber(
            @ApiParam(
                    value = "bookingNumber",
                    example = "SV-221122",
                    required = true)
            @RequestParam(value = "bookingNumber") String bookingNumber);

    @PostMapping
    @ApiOperation(value = "Create new Booking")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Booking created"),
            @ApiResponse(code = 400, message = "Bad request")
    })
    ResponseEntity<BookingDTO> createBookingDTO(
            @ApiParam(
                    name = "booking",
                    value = "Booking model"
            )
            @RequestBody @Valid BookingDTO bookingDTO);

    @PatchMapping("/{id}")
    @ApiOperation(value = "Update Booking by \"id\"")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Booking updated"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 404, message = "Booking not found")
    })
    ResponseEntity<BookingDTO> updateBookingDTOById(
            @ApiParam(
                    name = "id",
                    value = "Booking.id"
            )
            @PathVariable("id") Long id,
            @ApiParam(
                    name = "Booking",
                    value = "Booking model"
            )
            @RequestBody @Valid BookingDTO bookingDTO);

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete Booking by \"id\"")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Booking deleted"),
            @ApiResponse(code = 404, message = "Booking not found")
    })
    ResponseEntity<HttpStatus> deleteBookingById(
            @ApiParam(
                    name = "id",
                    value = "Booking.id"
            )
            @PathVariable("id") Long id);
}