# URL Shortener API 🌐

## Overview 📖

The URL Shortener API is a Java-based Spring Boot application integrated with a React front end, allowing users to shorten long URLs, set expiration dates, update existing URLs, and redirect to original URLs. It uses a MySQL database for persistence and stores URL mappings, with functionality to handle URL expiration. The entire application is containerized using Docker for simplified deployment and scalability, ensuring consistent environments for both the Spring Boot API and MySQL database, making it efficient and practical for real-world use.


![image](https://github.com/user-attachments/assets/aea3efed-6e65-48d1-a915-e8885496c17d)

![image](https://github.com/user-attachments/assets/92588812-b7d6-4b23-82d2-d8fcaafb58a0)

![image](https://github.com/user-attachments/assets/ed1027c0-d7e9-40d7-835b-af4aa244b654)

![image](https://github.com/user-attachments/assets/9917030c-b3ef-4897-8f2a-18d2ba8d7feb)

This is how data base looks like
![image](https://github.com/user-attachments/assets/225fedf5-8111-4870-a348-737d55366d2d)

Create MYSQL docker image and conatiner

![image](https://github.com/user-attachments/assets/04bad821-106e-4d0a-9596-470b24b8d309)

Build the docker image for spring boot application
![image](https://github.com/user-attachments/assets/a7831628-c167-4324-b777-c3a88665c3cd)

Create Network for connecting MySql to spring boot

![image](https://github.com/user-attachments/assets/f6852843-7783-4eb6-8dea-5de38c7a3828)

Run the Command to run the docker image

![image](https://github.com/user-attachments/assets/9afa1097-bb83-4d81-8796-62039f33d0f5)

For frontend : Add docker file and run these commands
docker build -t urlshortenerfrontend:dev .
docker run -p 3000:3000 urlshortenerfrontend:dev



## Features ✨

- **Shorten URLs:** 🔗 Convert long URLs into short, user-friendly links.
- **Set Expiration Dates:** ⏳ Specify an expiration date for short URLs.
- **Update URLs:** 🔄 Update the original URL for existing short URLs.
- **Redirect Short URLs:** 🔄 Redirect users from short URLs to their original long URLs.
- **Database Integration:** 🗄️ Store URL data in a SQL database.
- **Exception Handling:** 🚫 Handles cases for expired URLs and non-existent short URLs with appropriate error responses.

## Technologies Used 💻

- **Java 17:** ☕ Programming language used for the application.
- **Spring Boot:** 🚀 Framework for building the RESTful API.
- **SQL:** 🗄️ Relational database for storing URL data.
- **React:** ⚛️ Front-end library for building user interfaces.
- **Maven:** 📦 Build tool for managing dependencies and building the project.
- **Docker:**🛳️ Used for containerizing.

## Getting Started 🚀

### Prerequisites 📋

- Java 17 ☕
- Maven 📦
- SQL database (MySQL, MariaDB, etc.) 🗄️
