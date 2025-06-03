package com.redcare.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.redcare.error.GithubException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GitHubWebClientIntegrationTest {

  @Autowired
  private GitHubWebClient client;

  @Test
  void returnsRepositoriesForValidQuery() {
    var result = client.searchRepositories("java", Instant.ofEpochMilli(1577836800000L), 1, 10);

    assertNotNull(result);
    assertFalse(result.items().isEmpty());
    assertEquals(10, result.items().size());
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
    assertThrows(GithubException.class, () -> client.searchRepositories("asd435asda$#%ads",
        Instant.now().plus(1, ChronoUnit.DAYS), 1, 1));
  }
}
