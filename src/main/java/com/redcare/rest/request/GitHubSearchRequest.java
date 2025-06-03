package com.redcare.rest.request;

import com.redcare.rest.validation.NotInFuture;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;

public record GitHubSearchRequest(
    @NotBlank(message = "language cannot be blank") String language,
    @NotNull(message = "earliest date cannot be null") @NotInFuture Instant earliestCreationDate,
    @NotNull(message = "page cannot be null") @Min(value = 1,
        message = "page cannot be less than 1") @Max(value = 100,
        message = "max size cannot exceed 10") Integer page,
    @NotNull(message = "items per page cannot be null") @Min(value = 1,
        message = "items cannot be less than 1") @Max(value = 100,
        message =
            "max number of items per page cannot exceed 100") Integer perPage
) {

}