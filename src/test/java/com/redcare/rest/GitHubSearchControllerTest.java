package com.redcare.rest;

import com.redcare.error.GithubException;
import com.redcare.model.ScoredRepository;
import com.redcare.rest.request.GitHubSearchRequest;
import com.redcare.service.GitHubSearchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.List;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GitHubSearchController.class)
class GitHubSearchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GitHubSearchService searchService;


    @BeforeEach
    void resetMocks() {
        reset(searchService);
    }

    @Test
    void returnsScoredRepositoriesForValidRequest() throws Exception {
        var request = new GitHubSearchRequest("java", Instant.now().minusSeconds(3600), 1, 10);
        var mockResults = List.of(
                new ScoredRepository("repo1", "http://repo1", 100L, 10L, 95L),
                new ScoredRepository("repo2", "http://repo2", 90L, 5L, 90L)
        );
        when(searchService.searchAndScoreRepositories(request.language(), request.earliestCreationDate(), request.page(), request.perPage()))
                .thenReturn(mockResults);

        mockMvc.perform(get("/api/scored-search")
                        .param("language", "java")
                        .param("earliestCreationDate", request.earliestCreationDate().toString())
                        .param("page", request.page().toString())
                        .param("perPage", request.perPage().toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("repo1"))
                .andExpect(jsonPath("$[0].score").value(95));
    }

    @Test
    void returnsEmptyListForNoMatchingRepositories() throws Exception {
        var request = new GitHubSearchRequest("nonexistent-language", Instant.now().minusSeconds(3600), 1, 1);
        when(searchService.searchAndScoreRepositories(request.language(), request.earliestCreationDate(), request.page(), request.perPage()))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/scored-search")
                        .param("language", "nonexistent-language")
                        .param("earliestCreationDate", request.earliestCreationDate().toString())
                        .param("page", request.page().toString())
                        .param("perPage", request.perPage().toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void returnsBadRequestForInvalidRequest() throws Exception {
        mockMvc.perform(get("/api/scored-search")
                        .param("language", "")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void returnsErrorMessageWhenGithubExceptionThrown() throws Exception {
        var request = new GitHubSearchRequest("java", Instant.now().minusSeconds(3600), 1, 1);
        when(searchService.searchAndScoreRepositories(request.language(), request.earliestCreationDate(), request.page(), request.perPage()))
                .thenThrow(new GithubException("GitHubException"));

        mockMvc.perform(get("/api/scored-search")
                        .param("language", "java")
                        .param("earliestCreationDate", request.earliestCreationDate().toString())
                        .param("page", request.page().toString())
                        .param("perPage", request.perPage().toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}