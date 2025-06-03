package com.redcare.client;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

import com.redcare.client.response.GitHubSearchResponse;
import com.redcare.error.GithubException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

/**
 * GitHubWebClient is a Spring component responsible for interacting with the GitHub API. It
 * provides methods to search for repositories based on language, creation date, and pagination
 * parameters.
 */
@Component
public class GitHubWebClient {

  private static final Logger log = LoggerFactory.getLogger(GitHubWebClient.class);

  private static final String BASE_URL = "https://api.github.com/search/repositories";
  private static final String QUERY_TEMPLATE = "?q=language:%s+created:>=%s&sort=stars&order=desc&page=%s&per_page=%s";

  private final RestTemplate restTemplate;

  /**
   * Constructor for GitHubWebClient.
   *
   * @param restTemplate the RestTemplate instance used for making HTTP requests
   */
  public GitHubWebClient(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  /**
   * Searches GitHub repositories based on the specified programming language, earliest creation
   * date, and pagination parameters.
   *
   * @param language             the programming language to filter repositories by
   * @param earliestCreationDate the earliest creation date for repositories
   * @param page                 the page number for pagination
   * @param perPage              the number of results per page
   * @return a GitHubSearchResponse containing the search results
   * @throws GithubException if an error occurs while calling the GitHub API
   */
  public GitHubSearchResponse searchRepositories(String language, Instant earliestCreationDate,
      Integer page, Integer perPage) {
    // Format the query string using the provided language, date, and pagination parameters
    String query = format(QUERY_TEMPLATE, language, formatDate(earliestCreationDate), page,
        perPage);
    String url = BASE_URL + query;
    try {
      log.info("Calling GitHub API: {}", url);

      ResponseEntity<GitHubSearchResponse> response = restTemplate.getForEntity(url,
          GitHubSearchResponse.class);
      return response.getBody();
    } catch (HttpStatusCodeException e) {
      switch (e.getStatusCode()) {
        case UNPROCESSABLE_ENTITY ->
            throw new GithubException(UNPROCESSABLE_ENTITY, "Invalid query: " + query);
        case SERVICE_UNAVAILABLE -> throw new GithubException(SERVICE_UNAVAILABLE,
            "GitHub is unreachable. Please try again later.");
        default -> {
          log.error("GitHub API error: {}, body: {}", e.getStatusCode(),
              e.getResponseBodyAsString());
          throw new GithubException(HttpStatus.valueOf(e.getStatusCode().value()),
              "Unexpected GitHub API error: " + e.getMessage());
        }
      }
    } catch (Exception e) {
      log.error("Failed to call GitHub API", e);
      throw new GithubException("Unexpected error when calling GitHub");
    }
  }

  /**
   * Formats an Instant into a date string in the format yyyy-MM-dd.
   *
   * @param date the Instant to format
   * @return a formatted date string
   */
  private String formatDate(Instant date) {
    return new SimpleDateFormat("yyyy-MM-dd").format(Date.from(date));
  }
}