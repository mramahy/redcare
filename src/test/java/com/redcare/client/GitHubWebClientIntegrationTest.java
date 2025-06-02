package com.redcare.client;

import com.redcare.error.GithubException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GitHubWebClientIntegrationTest {

    @Autowired
    private GitHubWebClient client;

    @Test
    void returnsRepositoriesForValidQuery() {
        var result = client.searchRepositories("java", Instant.ofEpochMilli(1577836800000L), 1, 10);

        assertNotNull(result);
        assertFalse(result.items().isEmpty());
    }

    @Test
    void returnsEmptyListForNonsenseQuery() {
        var result = client.searchRepositories("java",
                Instant.now().plus(1, ChronoUnit.DAYS), 1, 10);

        assertNotNull(result);
        assertTrue(result.items().isEmpty());
    }

    @Test
    void returnsErrorNonsenseQuery() {
        assertThrows(GithubException.class, ()-> client.searchRepositories("asd435asda$#%ads",
                Instant.now().plus(1, ChronoUnit.DAYS), 1, 1));
    }
}
