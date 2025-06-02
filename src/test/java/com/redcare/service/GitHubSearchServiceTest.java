package com.redcare.service;

import com.redcare.client.GitHubWebClient;
import com.redcare.client.response.GitHubSearchResponse;
import com.redcare.model.GitHubRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;

import static java.time.Instant.now;
import static java.time.temporal.ChronoUnit.DAYS;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class GitHubSearchServiceTest {

    @Mock
    private GitHubWebClient gitHubWebClient;

    @InjectMocks
    private GitHubSearchService gitHubSearchService;

    @Test
    void returnsScoredRepositoriesSortedByScore() {
        var now = now();
        var repositories = List.of(
                new GitHubRepository("repo1", "http://repo1", 100L, 10L, now.minusSeconds(3600), now.minusSeconds(1800)),
                new GitHubRepository("repo2", "http://repo2", 90L, 5L, now.minusSeconds(7200), now.minusSeconds(3600))
        );
        when(gitHubWebClient.searchRepositories("java", now.minusSeconds(3600), 1, 1))
                .thenReturn(new GitHubSearchResponse(repositories));

        var result = gitHubSearchService.searchAndScoreRepositories("java", now.minusSeconds(3600), 1, 1);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).name()).isEqualTo("repo1");
        assertThat(result.get(1).name()).isEqualTo("repo2");
    }

    @Test
    void returnsEmptyListWhenNoRepositoriesFound() {
        Instant now = now();
        when(gitHubWebClient.searchRepositories("nonexistent", now, 1, 1))
                .thenReturn(new GitHubSearchResponse(List.of()));

        var result = gitHubSearchService.searchAndScoreRepositories("nonexistent",
                now, 1, 1);

        assertThat(result).isEmpty();
    }

    @Test
    void calculatesScoreCorrectlyForRepository() {
        Instant now = now();
        var repository = new GitHubRepository("repo1", "http://repo1", 100L, 10L,
                now.minus(1, DAYS), now.minus(1, DAYS));
        when(gitHubWebClient.searchRepositories("java", now, 1, 1))
                .thenReturn(new GitHubSearchResponse(List.of(repository)));

        var result = gitHubSearchService.searchAndScoreRepositories("java", now, 1, 1);

        assertThat(result.getFirst().score()).isEqualTo(100 + 10 - 1);
    }

    @Test
    void handlesRepositoriesWithNoUpdatesGracefully() {
        Instant now = now();
        var repository = new GitHubRepository("repo1", "http://repo1", 100L, 10L, null, now());
        when(gitHubWebClient.searchRepositories("java", now, 1, 1))
                .thenReturn(new GitHubSearchResponse(List.of(repository)));

        var result = gitHubSearchService.searchAndScoreRepositories("java", now, 1, 1);

        assertThat(result.getFirst().score()).isEqualTo(100 + 10);
    }
}