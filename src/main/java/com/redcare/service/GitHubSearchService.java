package com.redcare.service;

import com.redcare.client.GitHubWebClient;
import com.redcare.model.GitHubRepository;
import com.redcare.model.ScoredRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;

@Service
public class GitHubSearchService {

    private final GitHubWebClient gitHubWebClient;

    public GitHubSearchService(GitHubWebClient gitHubWebClient) {
        this.gitHubWebClient = gitHubWebClient;
    }

    public List<ScoredRepository>  searchAndScoreRepositories(String q, Instant earliestCreationDate) {
        var gitHubRepositories =
                gitHubWebClient.searchRepositories(q, earliestCreationDate).items();
        return gitHubRepositories.stream().map(
                githubRepository -> new ScoredRepository(
                        githubRepository.name(),
                        githubRepository.htmlUrl(),
                        githubRepository.stargazersCount(),
                        githubRepository.forksCount(),
                        calculateScore(githubRepository)
                )
        ).sorted(Comparator.comparingLong(ScoredRepository::score).reversed())
                .toList();
    }

    private Long calculateScore(GitHubRepository gitHubRepository) {
        return gitHubRepository.stargazersCount()
                + gitHubRepository.forksCount()
                - daysWithNoUpdates(gitHubRepository.updatedDate());
    }

    private long daysWithNoUpdates(Instant instant) {
        return Duration.between(instant, Instant.now()).toDays();
    }
}
