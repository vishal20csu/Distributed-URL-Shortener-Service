package com.example.URL_Shortener.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "short_urls", indexes = {
        @Index(name = "idx_short_urls_code", columnList = "shortUrl"),
        @Index(name = "idx_short_urls_expiry", columnList = "expiresAt")
})
public class ShortUrl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "long_url", columnDefinition = "TEXT", nullable = false)
    private String longUrl;

    @Column(name = "short_url", length = 7, unique = true)
    private String shortUrl;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // NOTE: Changed duration to an integer (representing days or minutes)
    // because LocalDateTime represents a specific calendar date/time, not a timeframe length.
    @Column(name = "duration_days", nullable = false)
    private int durationDays;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;
}
