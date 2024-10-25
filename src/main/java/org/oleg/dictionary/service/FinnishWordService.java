package org.oleg.dictionary.service;

import org.oleg.dictionary.model.FinnishWord;
import org.oleg.dictionary.repository.FinnishWordRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service layer class for managing Finnish words.
 * It interacts with the {@link FinnishWordRepository} to perform CRUD operations and business logic.
 */
@Service
public class FinnishWordService {

    private final FinnishWordRepository repository;

    /**
     * Constructor that injects the FinnishWordRepository.
     *
     * @param repository the repository used for data access
     */
    public FinnishWordService(FinnishWordRepository repository) {
        this.repository = repository;
    }

    /**
     * Retrieves all Finnish words from the database.
     *
     * @return a list of all {@link FinnishWord} objects
     */
    public List<FinnishWord> getAllWords() {
        return repository.findAll();
    }

    /**
     * Retrieves a Finnish word by its ID.
     *
     * @param id the ID of the word to retrieve
     * @return an {@link Optional} containing the word if found, or empty if not found
     */
    public Optional<FinnishWord> getWordById(Long id) {
        return repository.findById(id);
    }

    /**
     * Saves a Finnish word to the database.
     *
     * @param word the {@link FinnishWord} object to save
     * @return the saved {@link FinnishWord} object
     */
    public FinnishWord saveWord(FinnishWord word) {
        return repository.save(word);
    }

    /**
     * Deletes a Finnish word by its ID.
     *
     * @param id the ID of the word to delete
     */
    public void deleteWord(Long id) {
        repository.deleteById(id);
    }

    /**
     * Searches for Finnish words by their word field.
     *
     * @param word the word to search for (partial or complete)
     * @return a list of {@link FinnishWord} objects that contain the given word
     */
    public List<FinnishWord> searchByWord(String word) {
        return repository.findByWordContaining(word);
    }

    /**
     * Searches for Finnish words by their translation field.
     *
     * @param translation the translation to search for (partial or complete)
     * @return a list of {@link FinnishWord} objects that contain the given translation
     */
    public List<FinnishWord> searchByTranslation(String translation) {
        return repository.findByTranslationContaining(translation);
    }

    /**
     * Filters Finnish words based on various criteria such as days since last repeat, level, popularity, frequency, and categories.
     *
     * @param daysSinceLastRepeat the number of days since the word was last repeated
     * @param level the level of the word
     * @param popularity the popularity of the word
     * @param frequency the frequency of the word
     * @param source the source of the word
     * @param category1 the primary category of the word
     * @param category2 the secondary category of the word
     * @param repeatAgain the repeatAgain flag for the word
     * @return a list of filtered {@link FinnishWord} objects
     */
    public List<FinnishWord> filterWords(
            Long daysSinceLastRepeat, Integer level, Integer popularity,
            Integer frequency, String source, String category1, String category2, Integer repeatAgain) {

        List<FinnishWord> allWords = repository.findAll();

        return allWords.stream()
                .filter(word -> daysSinceLastRepeat == null || (word.getDaysSinceLastRepeat() != null && word.getDaysSinceLastRepeat().equals(daysSinceLastRepeat)))
                .filter(word -> level == null || (word.getLevel() != null && word.getLevel().equals(level)))
                .filter(word -> popularity == null || (word.getPopularity() != null && word.getPopularity().equals(popularity)))
                .filter(word -> frequency == null || (word.getFrequency() != null && word.getFrequency() <= frequency))
                .filter(word -> source.isEmpty() || (!word.getSource().isEmpty() && word.getSource().equalsIgnoreCase(source)))
                .filter(word -> category1.isEmpty() || (!word.getCategory().isEmpty() && word.getCategory().equalsIgnoreCase(category1)))
                .filter(word -> category2.isEmpty() || (!word.getCategory2().isEmpty() && word.getCategory2().equalsIgnoreCase(category2)))
                .filter(word -> repeatAgain == null || (word.getRepeatAgain() != null && word.getRepeatAgain().equals(repeatAgain)))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves words at a given level and sorts them by the oldest repeat date.
     *
     * @param level the level of the words to retrieve
     * @return a list of {@link FinnishWord} objects at the specified level with the oldest repeat dates
     */
    public List<FinnishWord> getWordsByLevelWithOldestRepeat(int level) {
        List<FinnishWord> words = repository.findByLevel(level);

        long maxDaysSinceLastRepeat = words.stream()
                .filter(word -> word.getDaysSinceLastRepeat() != null)
                .mapToLong(FinnishWord::getDaysSinceLastRepeat)
                .max()
                .orElse(0);

        return words.stream()
                .filter(word -> word.getDaysSinceLastRepeat() != null && word.getDaysSinceLastRepeat() == maxDaysSinceLastRepeat)
                .sorted(Comparator.comparing(FinnishWord::getDateRepeated))
                .toList();
    }

    /**
     * Gathers statistical information about the words in the database, including total word count and distribution by level and days since last repeat.
     *
     * @return a map of statistical data, including total words, studied words, and distribution of words by level and repeat date
     */
    public Map<String, Object> getWordsStatistics() {
        // Create a map to store statistics
        Map<String, Object> statistics = new HashMap<>();

        // Get the total number of words in the repository (i.e., in the database)
        long totalWords = repository.count();
        // Add the total word count to the statistics map
        statistics.put("totalWords", totalWords);

        // Get the count of words that have a level greater than or equal to 1 (i.e., words that have been studied)
        long studiedWords = repository.findAll().stream()
                .filter(word -> word.getLevel() >= 1) // Only count words with a level of 1 or higher
                .count();
        // Add the count of studied words to the statistics map
        statistics.put("studiedWords", studiedWords);

        // Analogue of pivot table in Excel where vertical axis is days since last repeat and horizontal axis is level, and in each cell we have count of words
        // Create a map to store the distribution of words across levels and days since last repeat
        Map<Integer, Map<Long, Long>> distribution = new HashMap<>();
        // Create a set to store all unique days since last repeat (across all levels)
        Set<Long> allDays = new HashSet<>();

        // Loop through each level (from 1 to 12)
        for (int level = 1; level <= 12; level++) {
            // For each level, get the list of words at that level and calculate the distribution of days since last repeat
            Map<Long, Long> daysDistribution = repository.findByLevel(level).stream()
                    // Filter out words that do not have a valid "daysSinceLastRepeat" value
                    .filter(word -> word.getDaysSinceLastRepeat() != null)
                    // Group the words by their "daysSinceLastRepeat" value and count how many words correspond to that key
                    .collect(Collectors.groupingBy(
                            FinnishWord::getDaysSinceLastRepeat,  // Grouping by days since last repeat
                            Collectors.counting()                 // Counting the number of words for days-key
                    ));
            // Add the calculated distribution for the current level to the distribution map
            distribution.put(level, daysDistribution);
            // Add all unique days from this level to the set of all days
            allDays.addAll(daysDistribution.keySet());
        }

        // Add the distribution map (words grouped by level and days since last repeat) to the statistics map
        statistics.put("distribution", distribution);
        // Add the set of all unique days since last repeat to the statistics map
        statistics.put("allDays", allDays);

        // Return the complete statistics map
        return statistics;
    }
}
