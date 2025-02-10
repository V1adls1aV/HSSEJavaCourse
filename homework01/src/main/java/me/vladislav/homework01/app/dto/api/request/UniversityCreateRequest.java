package me.vladislav.homework01.app.dto.api.request;

import jakarta.validation.constraints.NotNull;

public record UniversityCreateRequest(@NotNull String name, @NotNull String city, String description, @NotNull Integer rateKrutosty) {
}