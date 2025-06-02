package com.redcare.rest.request;

import com.redcare.rest.validation.NotInFuture;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record GitHubSearchRequest(
        @NotBlank String language,
        @NotNull @NotInFuture Instant earliestCreationDate,
        @Min(1) Integer page,
        @Min(1) Integer perPage
) {}