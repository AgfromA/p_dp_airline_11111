package app.services.interfaces;

import app.dto.TimezoneDTO;
import app.entities.Timezone;
import org.springframework.data.domain.Page;

import java.util.Optional;


public interface TimezoneService {

    Timezone saveTimezone(TimezoneDTO timezoneDTO);

    Timezone updateTimezone(TimezoneDTO timezoneDTO);

    Page<Timezone> getAllPagesTimezones(int page, int size);

    Optional<Timezone> getTimezoneById(Long id);

    void deleteTimezoneById(Long id);

}