package com.urlshortener.service;

import com.urlshortener.repository.UrlShortenerRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ExpiredUrlCleanupService {
    private static final Logger logger = LoggerFactory.getLogger(ExpiredUrlCleanupService.class);
    private final UrlShortenerRepository urlShortenerRepository;

    public ExpiredUrlCleanupService(UrlShortenerRepository urlShortenerRepository) {
        this.urlShortenerRepository = urlShortenerRepository;
    }

    @Scheduled(fixedRate = 1800000)
    @Transactional
    public void cleanupExpiredUrls() {
        LocalDateTime now = LocalDateTime.now();
        urlShortenerRepository.deleteAllByExpiresAtBefore(now);
        logger.info("Expired URLs cleaned: {}", now);
    }
}
