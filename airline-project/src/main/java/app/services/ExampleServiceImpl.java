package app.services;


import app.dto.ExampleDto;
import app.mappers.ExampleMapper;
import app.repositories.ExampleRepository;
import app.services.interfaces.ExampleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExampleServiceImpl implements ExampleService {

    private final ExampleRepository exampleRepository;
    private final ExampleMapper exampleMapper;

    public List<ExampleDto> findAll() {
        return exampleMapper.convertToExamleDTOList(exampleRepository.findAll());
    }

    public Page<ExampleDto> getPage(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return exampleRepository.findAll(pageRequest).map(exampleMapper::toDto);
    }

    public Optional<ExampleDto> findById(Long id) {
        return exampleRepository.findById(id).map(exampleMapper::toDto);
    }

    public ExampleDto save(ExampleDto exampleDto) {
        var example = exampleMapper.toEntity(exampleDto);
        return exampleMapper.toDto(exampleRepository.save(example));
    }

    public Optional<ExampleDto> update(Long id, ExampleDto exampleDto) {
        var example = exampleMapper.toEntity(exampleDto);
        Optional<ExampleDto> optionalSavedExample = findById(id);
        ExampleDto savedExample;
        if (optionalSavedExample.isEmpty()) {
            return optionalSavedExample;
        } else {
            savedExample = optionalSavedExample.get();
        }
        if (example.getExampleText() != null) {
            savedExample.setExampleText(example.getExampleText());
        }
        return Optional.of(exampleMapper.toDto(exampleRepository.save(exampleMapper.toEntity(savedExample))));
    }

    public Optional<ExampleDto> delete(Long id) {
        Optional<ExampleDto> optionalSavedExample = findById(id);
        if (optionalSavedExample.isEmpty()) {
            return optionalSavedExample;
        } else {
            exampleRepository.deleteById(id);
            return optionalSavedExample;
        }
    }
}