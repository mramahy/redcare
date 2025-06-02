package com.redcare.rest;

import com.redcare.model.ScoredRepository;
import com.redcare.rest.request.GitHubSearchRequest;
import com.redcare.service.GitHubSearchService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GitHubSearchController {

    private final GitHubSearchService searchService;

    public GitHubSearchController(GitHubSearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/api/scored-search")
    public ResponseEntity<List<ScoredRepository>> getScoredRepos(@Valid @ModelAttribute GitHubSearchRequest request) {

        var repositoryResults =
                searchService.searchAndScoreRepositories(request.language(),
                        request.earliestCreationDate());

        return ResponseEntity.ok(repositoryResults);
    }
}
