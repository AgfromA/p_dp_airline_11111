package app.enums.seats.aircraftseats;

import app.enums.seats.interfaces.AircraftSeats;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

// FIXME превратить всю эту бадягу с енамом в нормальную сущность, обозвать AircraftModel
@Getter
@RequiredArgsConstructor
public enum AirbusA319Seats implements AircraftSeats {

    A1("A1", false, false),
    B1("B1", false, false),
    C1("C1", false, false),
    D1("D1", false, false),
    E1("E1", false, false),
    F1("F1", false, false),

    A2("A2", false, false),
    B2("B2", false, false),
    C2("C2", false, false),
    D2("D2", false, false),
    E2("E2", false, false),
    F2("F2", false, false),

    A3("A3", false, false),
    B3("B3", false, false),
    C3("C3", false, false),
    D3("D3", false, false),
    E3("E3", false, false),
    F3("F3", false, false),

    A4("A4", false, false),
    B4("B4", false, false),
    C4("C4", false, false),
    D4("D4", false, false),
    E4("E4", false, false),
    F4("F4", false, false),

    A5("A5", false, false),
    B5("B5", false, false),
    C5("C5", false, false),
    D5("D5", false, false),
    E5("E5", false, false),
    F5("F5", false, false),

    A6("A6", false, false),
    B6("B6", false, false),
    C6("C6", false, false),
    D6("D6", false, false),
    E6("E6", false, false),
    F6("F6", false, false),

    A7("A7", false, false),
    B7("B7", false, false),
    C7("C7", false, false),
    D7("D7", false, false),
    E7("E7", false, false),
    F7("F7", false, false),

    A8("A8", false, false),
    B8("B8", false, false),
    C8("C8", false, false),
    D8("D8", false, false),
    E8("E8", false, false),
    F8("F8", false, false),

    A9("A9", false, false),
    B9("B9", false, false),
    C9("C9", false, false),
    D9("D9", false, false),
    E9("E9", false, false),
    F9("F9", false, false),

    A10("A10", false, true),
    B10("B10", false, true),
    C10("C10", false, true),
    D10("D10", false, true),
    E10("E10", false, true),
    F10("F10", false, true),

    A11("A11", true, false),
    B11("B11", true, false),
    C11("C11", true, false),
    D11("D11", true, false),
    E11("E11", true, false),
    F11("F11", true, false),

    A12("A12", false, false),
    B12("B12", false, false),
    C12("C12", false, false),
    D12("D12", false, false),
    E12("E12", false, false),
    F12("F12", false, false),

    A13("A13", false, false),
    B13("B13", false, false),
    C13("C13", false, false),
    D13("D13", false, false),
    E13("E13", false, false),
    F13("F13", false, false),

    A14("A14", false, false),
    B14("B14", false, false),
    C14("C14", false, false),
    D14("D14", false, false),
    E14("E14", false, false),
    F14("F14", false, false),

    A15("A15", false, false),
    B15("B15", false, false),
    C15("C15", false, false),
    D15("D15", false, false),
    E15("E15", false, false),
    F15("F15", false, false),

    A16("A16", false, false),
    B16("B16", false, false),
    C16("C16", false, false),
    D16("D16", false, false),
    E16("E16", false, false),
    F16("F16", false, false),

    A17("A17", false, false),
    B17("B17", false, false),
    C17("C17", false, false),
    D17("D17", false, false),
    E17("E17", false, false),
    F17("F17", false, false),

    A18("A18", false, false),
    B18("B18", false, false),
    C18("C18", false, false),
    D18("D18", false, false),
    E18("E18", false, false),
    F18("F18", false, false),

    A19("A19", false, false),
    B19("B19", false, false),
    C19("C19", false, false),
    D19("D19", false, false),
    E19("E19", false, false),
    F19("F19", false, false),

    A20("A20", false, false),
    B20("B20", false, false),
    C20("C20", false, false),
    D20("D20", false, false),
    E20("E20", false, false),
    F20("F20", false, false),

    A21("A21", false, false),
    B21("B21", false, false),
    C21("C21", false, false),
    D21("D21", false, false),
    E21("E21", false, false),
    F21("F21", false, false),

    A22("A22", false, false),
    B22("B22", false, false),
    C22("C22", false, false),
    D22("D22", false, false),
    E22("E22", false, false),
    F22("F22", false, false),

    A23("A23", false, false),
    B23("B23", false, false),
    C23("C23", false, false),
    D23("D23", false, false),
    E23("E23", false, false),
    F23("F23", false, false),

    A24("A24", false, true),
    B24("B24", false, true),
    C24("C24", false, true),
    D24("D24", false, true),
    E24("E24", false, true),
    F24("F24", false, true);

    private final String number;
    private final boolean isNearEmergencyExit;
    private final boolean isLockedBack;

    /*public String getNumber() {
        return number;
    }

    public boolean isNearEmergencyExit() {
        return isNearEmergencyExit;
    }

    public boolean isLockedBack() {
        return isLockedBack;
    }*/
}
