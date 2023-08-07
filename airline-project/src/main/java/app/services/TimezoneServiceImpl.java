package app.services;

import app.entities.Timezone;
import app.repositories.TimezoneRepository;
import app.services.interfaces.TimezoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TimezoneServiceImpl implements TimezoneService {

    private final TimezoneRepository timezoneRepository;

    @Transactional
    @Override
    public Timezone saveTimezone(Timezone timezone) {
        return timezoneRepository.save(timezone);
    }

    @Override
    @Transactional
    public Timezone updateTimezone(Timezone timezone) {
        return timezoneRepository.save(timezone);
    }

    @Override
    public Page<Timezone> getAllPagesTimezones(int page, int size) {
        return timezoneRepository.findAll(PageRequest.of(page, size));
    }

    @Override
    public Optional<Timezone> getTimezoneById(Long id) {
        return timezoneRepository.findById(id);
    }

    @Transactional
    @Override
    public void deleteTimezoneById(Long id) {
        timezoneRepository.deleteById(id);
    }
}