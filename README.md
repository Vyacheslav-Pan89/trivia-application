# Trivia Application

Simple multi-module Spring Boot 3 application for a trivia game.

Modules:
- `api` – REST API (players, trivia questions).
- `core` – DTOs, services, business logic.
- `persistence` – JPA entities and repositories.

## Run

From the project root:

bash mvn clean install mvn -pl api spring-boot:run

Default URL: `http://localhost:8080`
