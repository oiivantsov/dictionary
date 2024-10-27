
# Finnish Dictionary RESTful API

Welcome to the Finnish Dictionary RESTful API, a Spring Boot-based service that allows users to store, retrieve, and manage Finnish words along with their translations, categories, and other related metadata. The project includes a comprehensive CRUD (Create, Read, Update, Delete) interface and provides a range of filtering and search functionalities.

## Project Overview

- **Framework**: Spring Boot 3.3.4
- **Database**: PostgreSQL
- **Build Tool**: Maven
- **Java Version**: 17
- **API Deployment**: Hosted on [Render](https://dictionary-a919.onrender.com/api/words)

## Features

- Add, update, delete, and retrieve Finnish words with translations.
- Search words by text or translation.
- Filter words by attributes such as level, popularity, frequency, and categories.
- Retrieve words that are ready for repetition based on their last repeat date.
- Statistics endpoint that provides insights into the distribution of words by level and days since the last repeat.
- CORS configuration allows frontend integration (e.g., with React apps).

## API Endpoints

### Base URL
https://dictionary-a919.onrender.com/api/words


### Word Management

- **Get all words**: `GET /api/words`
- **Get word by ID**: `GET /api/words/{id}`
- **Search words**: `GET /api/words/search?word={word}&translation={translation}`
- **Filter words**: `GET /api/words/filter`
    - Parameters: `daysSinceLastRepeat`, `level`, `popularity`, `frequency`, `source`, `category1`, `category2`, `repeatAgain`
- **Get words for repetition**: `GET /api/words/repeat?level={level}`
- **Add a new word**: `POST /api/words` (JSON body with word details)
- **Update a word**: `PUT /api/words/{id}` (JSON body with updated word details)
- **Delete a word**: `DELETE /api/words/{id}`
- **Upgrade word levels**: `POST /api/words/upgrade` (JSON array of words to be upgraded)

### Statistics

- **Get dictionary statistics**: `GET /api/words/stats`

## Technologies Used
- **Spring Boot**: Simplifies backend development.
- **Spring Data JPA**: Manages database access.
- **PostgreSQL**: Stores Finnish words and their metadata.
- **Lombok**: Reduces boilerplate code.
- **Docker**: Containerizes the application for easier deployment.
- **Render**: Hosts the application in the cloud.


## Database Setup

### Using the Test Database
The project is preconfigured to use a **test PostgreSQL database** that includes a set of preloaded Finnish words. This setup is ideal for testing the API without needing to configure a custom database. The test database allows users to explore the API's capabilities, such as searching, filtering, and managing words.

### Using Your Own Database
If you want to use your **own database** instead of the provided test database, follow these steps:

1. **Set Up a PostgreSQL Database**:
- Create a new PostgreSQL database using your preferred method (e.g., through a cloud provider like AWS RDS or locally on your machine).
- Make sure to note the database credentials, including:
  - Database name
  - Username
  - Password
  - Host (URL or IP address)
  - Port (typically `5432`)

2. **Update Environment Variables**:
- Edit the environment variables in the `application.properties` or set them in your deployment environment (e.g., Render, Docker, etc.). The variables to configure are:
  ```properties
  spring.datasource.url=jdbc:postgresql://${DB_HOST}:${DB_PORT}/${DB_NAME}
  spring.datasource.username=${DB_USER}
  spring.datasource.password=${DB_PASSWORD}
  ```
- Replace `${DB_HOST}`, `${DB_PORT}`, `${DB_NAME}`, `${DB_USER}`, and `${DB_PASSWORD}` with the values for your database.

3. **Database Schema**:
- The application will automatically create the required tables using **Spring Data JPA** when it starts, as long as `spring.jpa.hibernate.ddl-auto=update` is set in the `application.properties`. This will ensure that the `finnish_dictionary` table is created if it does not already exist.
- Alternatively, you can manually create the schema using a SQL script if you prefer a more controlled setup.

4. **Populate the Database**:
- If you wish to start with your own set of words, you can manually add entries into the `finnish_dictionary` table using an SQL client or import data from a CSV or Excel file.

5. **Restart the Application**:
- After updating the configuration, restart the application to connect to the new database. The API will now use your custom PostgreSQL database for all its operations.

By following these steps, you can easily transition from the provided test database to a custom setup that better fits your needs. This flexibility allows you to adapt the application to your specific use case or integrate it into an existing environment.

## Contributing
Contributions are welcome! Please fork the repository and submit a pull request for any improvements or features.

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Contact
**Oleg Ivantsov**  
Feel free to reach out via email or through [LinkedIn](https://www.linkedin.com/in/oleg-ivantsov/)

## Acknowledgments
- Thanks to the Spring community for the excellent documentation and support.
- Special thanks to Render for providing free-tier hosting services.
