package me.vladislav.homework.app.dto.api.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record BookPatchRequest(
    @NotNull
    @Min(1)
    Long id,

    @Size(min = 3, max = 50)
    String title,

    @Size(min = 3, max = 50)
    String author) {
}