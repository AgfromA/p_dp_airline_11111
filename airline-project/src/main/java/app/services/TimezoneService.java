package app.services;

import app.dto.TimezoneDto;
import app.entities.Timezone;
import app.repositories.TimezoneRepository;
import app.mappers.TimezoneMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TimezoneService {

    private final TimezoneRepository timezoneRepository;
    private final TimezoneMapper timezoneMapper;

    public List<TimezoneDto> getAllTimeZone() {
        return timezoneMapper.toDtoList(timezoneRepository.findAll());
    }

    @Transactional
    public Timezone saveTimezone(TimezoneDto timezoneDto) {
        timezoneDto.setId(null);
        var timezone = timezoneMapper.toEntity(timezoneDto);
        return timezoneRepository.save(timezone);
    }

    @Transactional
    public Timezone updateTimezone(TimezoneDto timezoneDto) {
        var timezone = timezoneMapper.toEntity(timezoneDto);
        return timezoneRepository.save(timezone);
    }

    public Page<TimezoneDto> getAllPagesTimezones(int page, int size) {
        return timezoneRepository.findAll(PageRequest.of(page, size))
                .map(timezoneMapper::toDto);
    }

    public Optional<Timezone> getTimezoneById(Long id) {
        return timezoneRepository.findById(id);
    }

    @Transactional
    public void deleteTimezoneById(Long id) {
        timezoneRepository.deleteById(id);
    }
}