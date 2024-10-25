package org.oleg.dictionary.controller;

import org.oleg.dictionary.model.FinnishWord;
import org.oleg.dictionary.service.FinnishWordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * REST controller for managing Finnish words in the dictionary.
 * This controller provides endpoints for various operations such as
 * retrieving, filtering, adding, updating, and deleting words.
 */
@RestController
@RequestMapping("/api/words")
public class FinnishWordController {

    private final FinnishWordService service;

    /**
     * Constructor-based dependency injection for {@link FinnishWordService}.
     *
     * @param service the service to handle business logic for Finnish words
     */
    public FinnishWordController(FinnishWordService service) {
        this.service = service;
    }

    /**
     * Endpoint to retrieve all words in the dictionary.
     *
     * @return a list of all Finnish words
     */
    @GetMapping
    public List<FinnishWord> getAllWords() {
        return service.getAllWords();
    }

    /**
     * Endpoint to retrieve a word by its ID.
     *
     * @param id the ID of the word to retrieve
     * @return the word with the given ID, or a 404 Not Found response if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<FinnishWord> getWordById(@PathVariable Long id) {
        return service.getWordById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Endpoint to filter words based on several criteria.
     *
     * @param daysSinceLastRepeat number of days since the word was last repeated
     * @param level               the level of the word
     * @param popularity          the popularity of the word
     * @param frequency           the frequency of the word
     * @param source              the source of the word
     * @param category1           the first category of the word
     * @param category2           the second category of the word
     * @param repeatAgain         the status of the word to determine if it should be repeated
     * @return a list of words matching the filter criteria
     */
    @GetMapping("/filter")
    public ResponseEntity<List<FinnishWord>> filterWords(
            @RequestParam(required = false) Long daysSinceLastRepeat,
            @RequestParam(required = false) Integer level,
            @RequestParam(required = false) Integer popularity,
            @RequestParam(required = false) Integer frequency,
            @RequestParam(required = false) String source,
            @RequestParam(required = false) String category1,
            @RequestParam(required = false) String category2,
            @RequestParam(required = false) Integer repeatAgain) {

        List<FinnishWord> filteredWords = service.filterWords(
                daysSinceLastRepeat, level, popularity, frequency, source, category1, category2, repeatAgain);

        if (filteredWords.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(filteredWords);
    }

    /**
     * Endpoint to retrieve words that are ready for repetition based on their level.
     *
     * @param level the level of the words to retrieve
     * @return a list of words with the oldest repetition date for the given level
     */
    @GetMapping("/repeat")
    public ResponseEntity<List<FinnishWord>> getWordsForRepetition(
            @RequestParam("level") int level) {

        List<FinnishWord> words = service.getWordsByLevelWithOldestRepeat(level);
        if (words.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(words);
    }

    /**
     * Endpoint to add a new word to the dictionary.
     *
     * @param word the word to be added
     * @return the added word
     */
    @PostMapping
    public FinnishWord addWord(@RequestBody FinnishWord word) {
        return service.saveWord(word);
    }

    /**
     * Endpoint to upgrade the level of multiple words by 1.
     * It also updates the repetition date of the words to the current date.
     *
     * @param words the list of words to upgrade
     * @return a response with the updated words
     */
    @PostMapping("/upgrade")
    public ResponseEntity<?> upgradeWords(@RequestBody List<FinnishWord> words) {
        List<FinnishWord> updatedWords = new ArrayList<>();

        for (FinnishWord word : words) {
            word.setLevel(word.getLevel() + 1);
            word.setDateRepeated(LocalDate.now());
            updatedWords.add(service.saveWord(word));
        }

        return ResponseEntity.ok(updatedWords);
    }

    /**
     * Endpoint to update an existing word in the dictionary.
     *
     * @param id         the ID of the word to update
     * @param updateWord the updated word details
     * @return the updated word, or a 404 Not Found response if the word does not exist
     */
    @PutMapping("/{id}")
    public ResponseEntity<FinnishWord> updateword(@PathVariable Long id, @RequestBody FinnishWord updateWord) {
        return service.getWordById(id)
                .map(word -> {
                    updateWord.setId(word.getId());
                    return ResponseEntity.ok(service.saveWord(updateWord));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Endpoint to delete a word by its ID.
     *
     * @param id the ID of the word to delete
     * @return a 204 No Content response if the word was successfully deleted
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWord(@PathVariable Long id) {
        service.deleteWord(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Endpoint to search for words by their word or translation.
     *
     * @param word        the word to search for (optional)
     * @param translation the translation to search for (optional)
     * @return a list of words matching the search criteria
     */
    @GetMapping("/search")
    public ResponseEntity<List<FinnishWord>> searchWords(
            @RequestParam(required = false) String word,
            @RequestParam(required = false) String translation) {

        List<FinnishWord> results = new ArrayList<>();

        if (word != null) {
            results = service.searchByWord(word);
        } else if (translation != null) {
            results = service.searchByTranslation(translation);
        }

        if (results.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(results);
    }

    /**
     * Endpoint to get overall statistics about the words in the dictionary.
     *
     * @return a map containing statistics about total words, studied words, and word distributions
     */
    @GetMapping("/stats")
    public ResponseEntity<?> getWordsStatistics() {
        return ResponseEntity.ok(service.getWordsStatistics());
    }
}
