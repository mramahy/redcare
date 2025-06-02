# 🧪 Redcare GitHub Repository Scorer

This project is a Spring Boot backend service designed to search GitHub repositories using the GitHub REST API and score them based on popularity and activity metrics.

---

## What is this project about?

The application exposes a REST API that:
- Accepts a programming language and a minimum creation date
- Queries GitHub repositories matching the criteria
- Scores each repository based on stars, forks, and update recency
- Returns a ranked list of repositories with their computed scores

It's built with modern Spring Boot practices including:
- Validation and exception handling
- Layered architecture (Controller → Service → Client)
- Unit and integration testing
- GitHub API consumption via `RestTemplate`

---

## 📦 Prerequisites

To build and run the project, you need:

- Java 21+
- Gradle (or use the included `gradlew`)
- (Optional) Docker

## Project Structure

the project is divided into 3 main packages:
- rest: Contains the REST controller, global exception handler, validators and model attribute.
- service: Contains the service layer that handles scoring logic.
- client: Contains the GitHub API client and response for repositories fetched from GitHub.

Along with config, error and model packages that contains rest template configuration,  main models and our exception classes

## 🚀 How to Run the Project

### Option 1: Run Locally with Gradle

Open terminal on project then:

``` ./gradlew build```

``` ./gradlew bootRun```

### Option 2: Run with Docker

Open terminal on project then:

```docker build -t redcare-api . ```

```docker run -p 8080:8080 redcare-api ```
