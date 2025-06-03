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

/**
 * GitHubSearchController is a REST controller that handles requests for searching and scoring GitHub repositories.
 * It provides an endpoint to retrieve scored repositories based on search criteria.
 */
@RestController
public class GitHubSearchController {

    private final GitHubSearchService searchService;

    /**
     * Constructor for GitHubSearchController.
     *
     * @param searchService the service responsible for searching and scoring GitHub repositories
     */
    public GitHubSearchController(GitHubSearchService searchService) {
        this.searchService = searchService;
    }

    /**
     * Handles GET requests to the /api/scored-search endpoint.
     * Searches and scores GitHub repositories based on the provided request parameters.
     *
     * @param request the search request containing language, creation date, pagination, and other criteria
     * @return a ResponseEntity containing a list of scored repositories
     */
    @GetMapping("/api/scored-search")
    public ResponseEntity<List<ScoredRepository>> getScoredRepos(@Valid @ModelAttribute GitHubSearchRequest request) {

        var repositoryResults = searchService.searchAndScoreRepositories(
                request.language(),
                request.earliestCreationDate(),
                request.page(),
                request.perPage());

        return ResponseEntity.ok(repositoryResults);
    }
}