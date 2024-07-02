**URL Shortener API:**

**Overview:**

The URL Shortener API is a Java-based Spring Boot application that provides a service for shortening long URLs. Users can create short URLs with an expiration date, update existing short URLs, and redirect to the original long URLs. The application also maintains a local CSV file to store URL data, ensuring persistence across application restarts.

This project demonstrates practical use of Spring Boot, PostgreSQL, and OpenCSV, offering a comprehensive example of building a URL shortening service.



**Features**

**Shorten URLs:** Convert long URLs into short, user-friendly links.

**Set Expiration Dates:** Specify an expiration date for short URLs.

**Update URLs:** Update the original URL for existing short URLs.

**Redirect Short URLs:** Redirect users from short URLs to their original long URLs.

**CSV File Management:** Read from and write to a CSV file for URL data storage and management.

**Database Integration:** Store URL data in a PostgreSQL database.

**Exception Handling:** Handles cases for expired URLs and non-existent short URLs with appropriate error responses.




**Technologies Used**

**Java 17:** Programming language used for the application.

**Spring Boot:** Framework for building the RESTful API.

**PostgreSQL:** Relational database for storing URL data.

**OpenCSV:** Library for reading from and writing to CSV files.

**Maven:** Build tool for managing dependencies and building the project

