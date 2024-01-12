package app.services.interfaces;

import app.dto.TimezoneDto;
import app.entities.Timezone;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface TimezoneService {
    List<TimezoneDto> getAllTimeZone();

    Timezone saveTimezone(TimezoneDto timezoneDto);

    Timezone updateTimezone(TimezoneDto timezoneDto);

    Page<TimezoneDto> getAllPagesTimezones(int page, int size);

    Optional<Timezone> getTimezoneById(Long id);

    void deleteTimezoneById(Long id);

}