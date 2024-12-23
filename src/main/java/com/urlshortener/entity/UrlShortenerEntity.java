package com.urlshortener.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class UrlShortenerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String shortId;

    @Column(nullable = false)
    private String longUrl;

    private LocalDateTime createdAt;

    private LocalDateTime expiresAt;

    public UrlShortenerEntity(String shortId, String longUrl, LocalDateTime createdAt, LocalDateTime expiresAt) {
        this.shortId = shortId;
        this.longUrl = longUrl;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
    }
}
