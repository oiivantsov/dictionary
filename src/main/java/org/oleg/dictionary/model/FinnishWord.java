package org.oleg.dictionary.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "finnish_dictionary")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FinnishWord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date_added")
    private LocalDate dateAdded;

    @Column(name = "date_repeated")
    private LocalDate dateRepeated;

    @Transient // This field will not be stored in the database
    private Long daysSinceLastRepeat;

    @Column
    private Integer level;

    @Column(nullable = false)
    private String word;

    @Column(columnDefinition = "TEXT")
    private String translation;

    @Column
    private String category;

    @Column
    private String category2;

    @Column
    private String source;

    @Column
    private Integer popularity;

    @Column
    private Integer repeatAgain;

    @Column(columnDefinition = "TEXT")
    private String comment;

    @Column(columnDefinition = "TEXT")
    private String example;

    @Column(columnDefinition = "TEXT")
    private String synonyms;

    @Column(name = "word_formation", columnDefinition = "TEXT")
    private String wordFormation;

    @Column
    private Integer frequency;

    // Getter for calculated field
    public Long getDaysSinceLastRepeat() {
        if (this.dateRepeated != null) {
            return ChronoUnit.DAYS.between(this.dateRepeated, LocalDate.now());
        } else {
            return null;
        }
    }
}
