package me.vladislav.homework01.app.dto.api.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record UserCreateRequest(@NotNull String username, @NotNull @Email String email) {
}
