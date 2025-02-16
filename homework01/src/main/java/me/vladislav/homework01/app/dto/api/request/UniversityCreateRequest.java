package me.vladislav.homework01.app.dto.api.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UniversityCreateRequest(
    @NotBlank
    @Size(min = 3, max = 50)
    String name,

    @NotBlank
    @Size(min = 3, max = 50)
    String city,

    String description,

    Integer rateKrutosty) {
}