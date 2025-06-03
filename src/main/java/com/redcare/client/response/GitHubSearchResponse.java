package com.redcare.client.response;

import com.redcare.model.GitHubRepository;
import java.util.List;

public record GitHubSearchResponse(List<GitHubRepository> items) {

}

