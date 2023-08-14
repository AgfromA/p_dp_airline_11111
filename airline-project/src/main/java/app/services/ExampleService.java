package app.services;


import app.entities.Example;
import app.repositories.ExampleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExampleService {

    private final ExampleRepository exampleRepository;

    public List<Example> findAll() {
        return exampleRepository.findAll();
    }

    public Page<Example> getPage(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return exampleRepository.findAll(pageRequest);
    }

    public Optional<Example> findById(Long id) {
        return exampleRepository.findById(id);
    }

    public Example save(Example example) {
        return exampleRepository.save(example);
    }

    public Optional<Example> update(Long id, Example example) {
        Optional<Example> optionalSavedExample = findById(id);
        Example savedExample;
        if (optionalSavedExample.isEmpty()) {
            return optionalSavedExample;
        } else {
            savedExample = optionalSavedExample.get();
        }
        if (example.getExampleText() != null) {
            savedExample.setExampleText(example.getExampleText());
        }
        return Optional.of(exampleRepository.save(savedExample));
    }

    public Optional<Example> delete(Long id) {
        Optional<Example> optionalSavedExample = findById(id);
        if (optionalSavedExample.isEmpty()) {
            return optionalSavedExample;
        } else {
            exampleRepository.deleteById(id);
            return optionalSavedExample;
        }
    }
}