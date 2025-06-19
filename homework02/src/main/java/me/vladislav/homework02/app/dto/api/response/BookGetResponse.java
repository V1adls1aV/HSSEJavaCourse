package me.vladislav.homework02.app.dto.api.response;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record BookGetResponse(
    @NotNull
    @Min(1)
    Long id,

    @NotBlank
    @Size(min = 3, max = 50)
    String title,

    @NotBlank
    @Size(min = 3, max = 50)
    String author) {
}
