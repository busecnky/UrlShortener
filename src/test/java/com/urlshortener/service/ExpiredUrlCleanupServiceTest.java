package com.urlshortener.service;

import com.urlshortener.repository.UrlShortenerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

public class ExpiredUrlCleanupServiceTest {

    @Mock
    private UrlShortenerRepository urlShortenerRepository;

    @InjectMocks
    private ExpiredUrlCleanupService expiredUrlCleanupService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCleanupExpiredUrls_ShouldDeleteExpiredUrls() {
        LocalDateTime now = LocalDateTime.now();

        expiredUrlCleanupService.cleanupExpiredUrls();

        verify(urlShortenerRepository, times(1)).deleteAllByExpiresAtBefore(now);
    }
}
