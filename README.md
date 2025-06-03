# 🧪 Redcare GitHub Repository Scorer

This project is a Spring Boot backend service designed to search GitHub repositories using the
GitHub REST API and score them based on popularity and activity metrics.

---

## Requirements

- Develop a scoring algorithm that assigns a popularity score to each repository.
- Factors contributing to the score:
    - Number of stars
    - Number of forks
    - Recency of updates
- the user should be able to configure the earliest created date and language of repositories.

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

Along with config, error and model packages that contains rest template configuration, main models
and our exception classes

## 🚀 How to Run the Project

### Option 1: Run Locally with Gradle

Open terminal on project then:

``` ./gradlew build```

``` ./gradlew bootRun```

### Option 2: Run with Docker

Open terminal on project then:

```docker build -t redcare-api . ```

```docker run -p 8080:8080 redcare-api ```

## 📡 API Usage

### GET `/api/scored-search`

**Query Parameters:**

| Name                   | Type        | Required | Description                          |
|------------------------|-------------|----------|--------------------------------------|
| `language`             | String      | ✅ Yes    | Programming language (e.g. `java`)   |
| `earliestCreationDate` | ISO Instant | ✅ Yes    | Repositories created after this date |
| `page`                 | Integer     | ✅ Yes    | number of page                       |
| `perPage`              | Integer     | ✅ Yes    | number of items per page             |

**Example:**

```GET```
GET http://localhost:8080/api/scored-search?language=java&earliestCreationDate=2020-03-01T00:00:00Z&page=2&perPage=10

**Response:**

```json
[
  {
    "name": "awesome-java-lib",
    "url": "https://github.com/awesome/awesome-java-lib",
    "stars": 1234,
    "forks": 432,
    "score": 1600
  }
]
```
