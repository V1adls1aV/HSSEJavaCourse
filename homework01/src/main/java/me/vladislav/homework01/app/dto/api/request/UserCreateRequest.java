package me.vladislav.homework01.app.dto.api.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserCreateRequest(
    @NotBlank
    @Size(min = 3, max = 50)
    String username,

    @NotBlank
    @Email
    String email) {
}
