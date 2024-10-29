
# Finnish Dictionary RESTful API

Welcome to the Finnish Dictionary RESTful API, a Spring Boot-based service that allows users to store, retrieve, and manage Finnish words along with their translations, categories, and other related metadata. The project includes a comprehensive CRUD (Create, Read, Update, Delete) interface and provides a range of filtering and search functionalities.

## Project Overview

- **Framework**: Spring Boot 3.3.4
- **Database**: PostgreSQL
- **Build Tool**: Maven
- **Java Version**: 17
- **API Deployment**: Hosted on Render ([example of GET-request](https://dictionary-a919.onrender.com/api/words/1))

## Project History

The project started as a simple **Excel spreadsheet** where I manually recorded Finnish words, translations, and additional notes while learning the language over the past **two years**. Over time, the Excel format became cumbersome to use, especially as the number of entries grew beyond **10,000 words**. To make the data more accessible and useful for study, I decided to develop a more comprehensive solution.

This project is now a part of my personal dictionary ecosystem, which includes:
- **This RESTful API** built with **Spring Boot**, providing structured access to the word data and enabling various query operations.
- A **React-based frontend** that allows for a user-friendly interface to search, filter, and manage words.
- A **Python parser** that automatically extracts and updates word data from **open Finnish dictionary sources**, making it easier to add new words.

### Note
The React app and the Python parser are not yet publicly available, but links will be provided here in future updates.

By transforming the original Excel-based dictionary into a structured API and web interface, this project aims to enhance my language learning process and make vocabulary management more efficient.


## Features

- Add, update, delete, and retrieve Finnish words with translations.
- Search words by text or translation.
- Filter words by attributes such as level, popularity, frequency, and categories.
- Retrieve words that are ready for repetition based on their last repeat date.
- Statistics endpoint that provides insights into the distribution of words by level and days since the last repeat.
- CORS configuration allows frontend integration (e.g., with React apps).

## API Endpoints

### Base URL
The API is hosted on Render, example GET-request: https://dictionary-a919.onrender.com/api/words/1

Please note that **the first loading may take about 30-50 seconds** due to the **free-tier hosting on Render**. This delay occurs because the server might need to wake up from an idle state. Subsequent requests should be faster.


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
The project is preconfigured to use a **test PostgreSQL database** that includes a rich dataset of **over 13,000 Finnish words**. These entries have been meticulously collected over **two years** during my journey of learning Finnish. This setup is ideal for testing the API and exploring the different capabilities of the application without needing to configure a custom database.

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


## Database Schema

The application's database consists of a single table called `finnish_dictionary` that stores Finnish words along with various metadata. Below is the structure of the table and a description of each field:

### `finnish_dictionary` Table

| Column                | Type         | Description                                                                                           |
|-----------------------|--------------|-------------------------------------------------------------------------------------------------------|
| `id`                  | BIGINT       | The **primary key** for the table. This value is automatically generated and uniquely identifies each word entry. |
| `date_added`          | DATE         | The date when the word was **added** to the dictionary. This helps track how long a word has been in the system. |
| `date_repeated`       | DATE         | The date when the word was **last repeated**. This is used to calculate how many days have passed since the last repetition. |
| `daysSinceLastRepeat` | TRANSIENT    | A **calculated field** (not stored in the database) that shows the number of **days since the word was last repeated**. |
| `level`               | INTEGER      | Represents how well the word is **learned** by the user, following an **interval repetition** method. Words with higher levels are reviewed less frequently. |
| `word`                | TEXT         | The **Finnish word** being added to the dictionary. This field is required. |
| `translation`         | TEXT         | The **translation** of the Finnish word, typically in another language such as English or Russian. |
| `category`            | TEXT         | The **primary category** of the word, which can help group words by themes (e.g., "nature", "verbs"). |
| `category2`           | TEXT         | The **secondary category** for more specific classification of the word. |
| `source`              | TEXT         | The **source** from where the word was learned or collected, such as a textbook, website, or conversation. |
| `popularity`          | INTEGER      | Indicates the **popularity** or frequency of use of the word. Higher values indicate more commonly used words. |
| `repeatAgain`         | INTEGER      | A flag indicating whether the word should be **repeated** again soon (1 for true, 0 for false). |
| `comment`             | TEXT         | **Additional comments** or notes about the word, such as context or nuances in usage. |
| `example`             | TEXT         | **Example sentences** or phrases that demonstrate the usage of the word in context. |
| `synonyms`            | TEXT         | A list of **synonyms** for the Finnish word, helping to expand vocabulary. |
| `word_formation`      | TEXT         | Information about **word formation** or related word forms (e.g., noun forms, verb conjugations). |
| `frequency`           | INTEGER      | A **rating from the 10,000 most popular Finnish words**, where a lower number indicates a more common word. This helps prioritize frequently used words for study. |

This schema is designed to provide a balance between detailed information and simplicity, allowing users to track their progress while maintaining a large vocabulary database.

## Main Sources of Data

During the collection of data, I utilized various reputable sources to ensure accuracy and completeness, including:

- [Finnish Wiktionary](https://fi.wiktionary.org/wiki/Wikisanakirja:Etusivu)
- [English Wiktionary](https://en.wiktionary.org/wiki/Wiktionary:Main_Page)
- [Glosbe Finnish-English Dictionary](https://fi.glosbe.com/fi/en)
- Books such as:
  - *Suomalais-Venäläinen Suursanakirja* by I. Vahros and A. Scherbakoff (2007)
  - *Tarkista Tästä* by Hannele Jönsson-Korhola & Leila White, focusing on Finnish word usage and grammar.

These sources provided a solid foundation for building a robust vocabulary database.

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
