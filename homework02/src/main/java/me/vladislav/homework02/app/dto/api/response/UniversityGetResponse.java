package me.vladislav.homework02.app.dto.api.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UniversityGetResponse(
    @NotNull
    Long id,

    @NotBlank
    @Size(min = 3, max = 50)
    String name,

    @NotBlank
    @Size(min = 3, max = 50)
    String city,

    String description,

    Integer rateKrutosty) {}
