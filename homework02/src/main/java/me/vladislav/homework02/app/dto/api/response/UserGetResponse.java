package me.vladislav.homework02.app.dto.api.response;

import jakarta.validation.constraints.*;

public record UserGetResponse(
    @NotNull
    @Min(1)
    Long id,

    @NotBlank
    @Size(min = 3, max = 50)
    String username,

    @NotBlank
    @Email
    String email) {
}
