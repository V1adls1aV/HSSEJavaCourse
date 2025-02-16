package me.vladislav.homework01.app.dto.api.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record BookGetResponse(
    @NotNull
    Long id,

    @NotBlank
    @Size(min = 3, max = 50)
    String title,

    @NotBlank
    @Size(min = 3, max = 50)
    String author) {
}
