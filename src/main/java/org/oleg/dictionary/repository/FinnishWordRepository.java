package org.oleg.dictionary.repository;

import org.oleg.dictionary.model.FinnishWord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
// create proxy object for the repository, which will be used to perform CRUD operations
// spring data jpa parses the method name and automatically creates the query
// based on parsed data, Spring Data JPA will automatically generate the appropriate JPQL or SQL query
public interface FinnishWordRepository extends JpaRepository<FinnishWord, Long> {
    // Custom query to search words by the word field
    // SELECT * FROM finnish_dictionary WHERE word LIKE %word%;
    List<FinnishWord> findByWordContaining(String word);

    // Custom query to search words by the translation field
    List<FinnishWord> findByTranslationContaining(String translation);

    // Custom query to search words by the level field
    List<FinnishWord> findByLevel(Integer level);

}
