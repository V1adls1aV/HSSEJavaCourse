package me.vladislav.homework02.app.dto.api.response;

import jakarta.validation.constraints.*;

public record CourseGetResponse(
    @NotNull
    @Min(1)
    Long id,

    @NotBlank
    @Size(min = 3, max = 50)
    String title,

    @NotBlank
    @Size(min = 3, max = 50)
    String author,

    String description,

    @Min(0) @Max(366)
    Integer duration) {}

