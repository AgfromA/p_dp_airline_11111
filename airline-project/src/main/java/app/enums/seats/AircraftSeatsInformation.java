package app.enums.seats;

import app.enums.seats.aircraftseats.*;
import app.enums.seats.interfaces.AircraftSeats;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

// FIXME превратить всю эту бадягу с енамом в нормальную сущность, обозвать AircraftModel
@Getter
@RequiredArgsConstructor
public enum AircraftSeatsInformation {

    AIRBUS_A320("Airbus A320", 158, 8, AirbusA320Seats.values()),
    AIRBUS_A320_NEO("Airbus A320 Neo", 164, 8, AirbusA320NeoSeats.values()),
    AIRBUS_A319("Airbus A319", 144, 0, AirbusA319Seats.values()),
    BOEING_737_800("Boeing 737-800", 168, 12, Boeing737800Seats.values()),
    EMBRAER_ERJ170("Embraer ERJ170", 76, 0, EmbraerERJ170Seats.values());

    private final String aircraftModel;
    private final int totalNumberOfSeats;
    private final int numberOfBusinessClassSeats;
    private final AircraftSeats[] aircraftSeats;
}
