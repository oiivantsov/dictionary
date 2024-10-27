
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
