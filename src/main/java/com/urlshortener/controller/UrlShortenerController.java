package com.urlshortener.controller;

import com.urlshortener.dto.ShortenUrlRequestDto;
import com.urlshortener.service.UrlShortenerService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping
public class UrlShortenerController {
    private static final Logger logger = LoggerFactory.getLogger(UrlShortenerController.class);
    private final UrlShortenerService urlShortenerService;

    public UrlShortenerController(UrlShortenerService urlShortenerService) {
        this.urlShortenerService = urlShortenerService;
    }

    @PostMapping
    public ResponseEntity<Optional<String>> shortenUrl(@Valid @RequestBody ShortenUrlRequestDto shortenUrlRequestDto) {
        logger.info("A request to create a new short URL was received. LongURL: {}, TTL: {}",
                shortenUrlRequestDto.getLongUrl(), shortenUrlRequestDto.getTtlInSeconds());
        return ResponseEntity.ok(urlShortenerService.shortenUrl(shortenUrlRequestDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<String>> getLongUrl(@PathVariable String id) {
        logger.info("Redirect request received with ShortId: {}", id);
        return ResponseEntity.ok(urlShortenerService.getLongUrl(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteById(@PathVariable String id) {
        logger.info("Delete request received with ShortId: {}", id);
        return ResponseEntity.ok(urlShortenerService.deleteById(id));
    }
}
