package org.oleg.dictionary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The main entry point for the Dictionary application.
 * <p>
 * This class configures and launches the Spring Boot application.
 * </p>
 */
@SpringBootApplication
public class DictionaryApplication {

	/**
	 * Main method that serves as the entry point for the Spring Boot application.
	 *
	 * @param args command-line arguments passed to the application
	 */
	public static void main(String[] args) {
		SpringApplication.run(DictionaryApplication.class, args);
	}

}
