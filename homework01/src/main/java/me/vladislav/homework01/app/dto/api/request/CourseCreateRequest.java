package me.vladislav.homework01.app.dto.api.request;

public record CourseCreateRequest(String title, String author, String description, Integer duration) {
}