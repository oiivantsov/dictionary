package org.oleg.dictionary.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration class for handling CORS (Cross-Origin Resource Sharing) settings.
 * This class implements {@link WebMvcConfigurer} to allow custom CORS mappings.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Configure CORS settings by allowing cross-origin requests from specific origins and methods.
     *
     * @param registry the {@link CorsRegistry} that allows configuring CORS settings.
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Allow CORS requests for all endpoints.
                .allowedOrigins("http://localhost:3000", "https://dictionary-search.onrender.com") // Add both origins.
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Specify allowed HTTP methods.
                .allowedHeaders("*") // Allow all headers.
                .allowCredentials(true); // Allow sending cookies or credentials with cross-origin requests.
    }

}
