package com.redcare.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.redcare.client.response.GitHubSearchResponse;
import com.redcare.error.GithubException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

class GitHubWebClientTest {

  private RestTemplate restTemplate;
  private GitHubWebClient gitHubWebClient;

  @BeforeEach
  void setUp() {
    restTemplate = mock(RestTemplate.class);
    gitHubWebClient = new GitHubWebClient(restTemplate);
  }

  @Test
  void searchRepositories_returnsResponseBody_whenStatusOk() {
    GitHubSearchResponse mockResponse = mock(GitHubSearchResponse.class);
    ResponseEntity<GitHubSearchResponse> responseEntity =
        new ResponseEntity<>(mockResponse, HttpStatus.OK);

    when(restTemplate.getForEntity(anyString(), eq(GitHubSearchResponse.class)))
        .thenReturn(responseEntity);

    GitHubSearchResponse result = gitHubWebClient.searchRepositories("java", Instant.now(), 1, 1);
    assertEquals(mockResponse, result);
  }

  @Test
  void searchRepositories_throwsGithubException_whenUnprocessableEntity() {
    HttpClientErrorException exception = HttpClientErrorException.UnprocessableEntity.create(
        HttpStatus.UNPROCESSABLE_ENTITY,
        "Unprocessable Entity",
        null,
        null,
        StandardCharsets.UTF_8
    );

    when(restTemplate.getForEntity(anyString(), eq(GitHubSearchResponse.class)))
        .thenThrow(exception);

    assertThrows(GithubException.class, () ->
        gitHubWebClient.searchRepositories("java", Instant.now(), 1, 1));
  }

  @Test
  void searchRepositories_throwsGithubException_whenServiceUnavailable() {
    HttpServerErrorException exception = HttpServerErrorException.ServiceUnavailable.create(
        HttpStatus.SERVICE_UNAVAILABLE,
        "Service Unavailable",
        null,
        null,
        StandardCharsets.UTF_8
    );

    when(restTemplate.getForEntity(anyString(), eq(GitHubSearchResponse.class)))
        .thenThrow(exception);

    assertThrows(GithubException.class, () ->
        gitHubWebClient.searchRepositories("java", Instant.now(), 1, 1));
  }

  @Test
  void searchRepositories_throwsGithubException_whenUnknownERROR() {
    HttpServerErrorException exception = HttpServerErrorException.ServiceUnavailable.create(
        HttpStatus.INTERNAL_SERVER_ERROR,
        "INTERNAL_SERVER_ERROR",
        null,
        null,
        StandardCharsets.UTF_8
    );

    when(restTemplate.getForEntity(anyString(), eq(GitHubSearchResponse.class)))
        .thenThrow(exception);

    assertThrows(GithubException.class, () ->
        gitHubWebClient.searchRepositories("java", Instant.now(), 1, 1));
  }

  @Test
  void searchRepositories_throwsException_whenUnknownERROR() {

    when(restTemplate.getForEntity(anyString(), eq(GitHubSearchResponse.class)))
        .thenThrow(new RuntimeException());

    assertThrows(GithubException.class, () ->
        gitHubWebClient.searchRepositories("java", Instant.now(), 1, 1));
  }
}