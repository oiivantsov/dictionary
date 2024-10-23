package org.oleg.dictionary.controller;

import org.oleg.dictionary.model.FinnishWord;
import org.oleg.dictionary.service.FinnishWordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/words")
public class FinnishWordController {

    private final FinnishWordService service;

    // Loose coupling, which is achieved by injecting the dependency through the constructor
    public FinnishWordController(FinnishWordService service) {
        this.service = service;
    }

    @GetMapping
    public List<FinnishWord> getAllWords() {
        return service.getAllWords();
    }

    @GetMapping("/{id}")
    public ResponseEntity<FinnishWord> getWordById(@PathVariable Long id) {
        return service.getWordById(id)
                // If the word is found, return it with status code 200 OK
                .map(ResponseEntity::ok)
                // If the word is not found, return status code 404 Not Found
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public FinnishWord addWord(@RequestBody FinnishWord word) {
        return service.saveWord(word);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FinnishWord> updateword(@PathVariable Long id, @RequestBody FinnishWord updateWord) {
        return service.getWordById(id)
                .map(word -> {
                    updateWord.setId(word.getId());
                    return ResponseEntity.ok(service.saveWord(updateWord));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    // instead of void we can use wildcard <?> to return any type
    public ResponseEntity<Void> deleteWord(@PathVariable Long id) {
        service.deleteWord(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<FinnishWord>> searchWords(
            @RequestParam(required = false) String word,
            @RequestParam(required = false) String translation) {

        List<FinnishWord> results = new ArrayList<>();

        // If the word is provided, search by word
        if (word != null) {
            results = service.searchByWord(word);
        }
        // If translation is provided, search by translation
        else if (translation != null) {
            results = service.searchByTranslation(translation);
        }

        // If neither parameter is provided, return an empty list
        if (results.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(results);
    }

}
