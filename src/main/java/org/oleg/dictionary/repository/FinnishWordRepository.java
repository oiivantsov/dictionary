package org.oleg.dictionary.repository;

import org.oleg.dictionary.model.FinnishWord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for performing CRUD operations on the {@link FinnishWord} entity.
 * Extends the {@link JpaRepository} interface, which provides methods for basic CRUD operations.
 * Spring Data JPA automatically generates the appropriate JPQL or SQL queries based on the method names.
 */
@Repository
public interface FinnishWordRepository extends JpaRepository<FinnishWord, Long> {

    /**
     * Custom query to search words by their word field.
     * The generated query will search for Finnish words that contain the given string.
     *
     * @param word the word or substring to search for
     * @return a list of {@link FinnishWord} objects containing the given word
     */
    List<FinnishWord> findByWordContaining(String word);

    /**
     * Custom query to search words by their translation field.
     * The generated query will search for Finnish words that contain the given translation string.
     *
     * @param translation the translation or substring to search for
     * @return a list of {@link FinnishWord} objects with translations containing the given string
     */
    List<FinnishWord> findByTranslationContaining(String translation);

    /**
     * Custom query to search words by their level field.
     * The generated query will search for Finnish words that match the given level.
     *
     * @param level the level of the word to search for
     * @return a list of {@link FinnishWord} objects with the specified level
     */
    List<FinnishWord> findByLevel(Integer level);
}
