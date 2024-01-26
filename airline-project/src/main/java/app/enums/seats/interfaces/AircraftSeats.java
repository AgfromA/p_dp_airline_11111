package app.enums.seats.interfaces;

// FIXME превратить всю эту бадягу с енамом в нормальную сущность, обозвать AircraftModel
public interface AircraftSeats {

    String getNumber();

    boolean isNearEmergencyExit();

    boolean isLockedBack();
}
