package com.redcare.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;

public record GitHubRepository(
    String name,
    @JsonProperty("html_url") String htmlUrl,
    @JsonProperty("stargazers_count") Long stargazersCount,
    @JsonProperty("forks_count") Long forksCount,
    @JsonProperty("created_at") Instant creationDate,
    @JsonProperty("updated_at") Instant updatedDate
) {

}

