package me.vladislav.homework01.app.dto.api.request;

import jakarta.validation.constraints.NotNull;

public record BookCreateRequest(@NotNull String title, @NotNull String author) {
}
