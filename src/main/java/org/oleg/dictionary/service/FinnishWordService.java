package org.oleg.dictionary.service;

import org.oleg.dictionary.model.FinnishWord;
import org.oleg.dictionary.repository.FinnishWordRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FinnishWordService {

    private final FinnishWordRepository repository;

    // Dependency Injection -- Constructor Injection
    public FinnishWordService(FinnishWordRepository repository) {
        this.repository = repository;
    }

    public List<FinnishWord> getAllWords() {
        return repository.findAll();
    }

    // optional is a container object which may or may not contain a non-null value
    public Optional<FinnishWord> getWordById(Long id) {
        return repository.findById(id);
    }

    public FinnishWord saveWord(FinnishWord word) {
        return repository.save(word);
    }

    public void deleteWord(Long id) {
        repository.deleteById(id);
    }

    public List<FinnishWord> searchByWord(String word) {
        return repository.findByWordContaining(word);
    }

    public List<FinnishWord> searchByTranslation(String translation) {
        return repository.findByTranslationContaining(translation);
    }

    public List<FinnishWord> searchByDaysSinceLastRepeat(Long daysSinceLastRepeat) {
        List<FinnishWord> words = repository.findAll();

        // Filter the words by daysSinceLastRepeat
        return words.stream()
                .filter(word -> word.getDaysSinceLastRepeat() != null && word.getDaysSinceLastRepeat() >= daysSinceLastRepeat)
                .toList();
    }

    public List<FinnishWord> searchByLevel(Integer level) {
        return repository.findByLevel(level);
    }
}
