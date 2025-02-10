package me.vladislav.homework01.app.dto.api.request;

public record UniversityCreateRequest(String name, String city, String description, Integer rateKrutosty) {
}