package com.redcare.model;

public record ScoredRepository(
        String name,
        String url,
        Long stars,
        Long forks,
        Long score
) {}
