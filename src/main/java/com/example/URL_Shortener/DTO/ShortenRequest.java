package com.example.URL_Shortener.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL; // Fixed import path

import java.time.Duration;
import java.time.LocalDateTime;

// Changed to record parameter syntax using (...)

public record ShortenRequest(
        @NotBlank(message = "The original URL cannot be empty") // Fixed typo 'k'
        @URL(message = "Please provide a valid URL format")
        String originalUrl,
        @Positive(message = "Duration must be greater than 0 minutes")
        Long duration
) {}