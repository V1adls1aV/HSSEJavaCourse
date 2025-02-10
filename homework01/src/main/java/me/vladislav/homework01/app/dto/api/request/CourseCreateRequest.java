package me.vladislav.homework01.app.dto.api.request;

import jakarta.validation.constraints.NotNull;

public record CourseCreateRequest(@NotNull String title, @NotNull String author, String description, Integer duration) {
}