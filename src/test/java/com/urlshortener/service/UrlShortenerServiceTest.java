package com.urlshortener.service;

import com.urlshortener.dto.ShortenUrlRequestDto;
import com.urlshortener.entity.UrlShortenerEntity;
import com.urlshortener.repository.UrlShortenerRepository;
import com.urlshortener.utils.IdGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UrlShortenerServiceTest {

    @Mock
    private UrlShortenerRepository urlShortenerRepository;

    @Mock
    private IdGenerator idGenerator;

    @InjectMocks
    private UrlShortenerService urlShortenerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testShortenUrl_CustomShortIdExists_ShouldThrowException() {
        ShortenUrlRequestDto requestDto = new ShortenUrlRequestDto("https://example.com", "customId", 1800L);
        when(urlShortenerRepository.findByShortId("customId")).thenReturn(Optional.of(new UrlShortenerEntity()));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> urlShortenerService.shortenUrl(requestDto));
        assertEquals("ID already exists", exception.getMessage());
    }

    @Test
    void testShortenUrl_GeneratesUniqueShortId_Success() {
        ShortenUrlRequestDto requestDto = new ShortenUrlRequestDto("https://example.com", null, 1800L);
        when(idGenerator.generateUniqueId(6)).thenReturn("generatedId");
        when(urlShortenerRepository.findByShortId("generatedId")).thenReturn(Optional.empty());

        Optional<String> result = urlShortenerService.shortenUrl(requestDto);

        assertTrue(result.isPresent());
        assertEquals("generatedId", result.get());
        verify(urlShortenerRepository, times(1)).save(any(UrlShortenerEntity.class));
    }

    @Test
    void testGetLongUrl_ExpiredUrl_ShouldThrowException() {
        String shortId = "expiredId";
        UrlShortenerEntity entity = new UrlShortenerEntity(shortId, "https://example.com",
                LocalDateTime.now().minusDays(2), LocalDateTime.now().minusHours(1));
        when(urlShortenerRepository.findByShortId(shortId)).thenReturn(Optional.of(entity));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> urlShortenerService.getLongUrl(shortId));
        assertEquals("This URL has expired.", exception.getMessage());
        verify(urlShortenerRepository, times(1)).delete(entity);
    }

    @Test
    void testGetLongUrl_ValidUrl_Success() {
        String shortId = "validId";
        UrlShortenerEntity entity = new UrlShortenerEntity(shortId, "https://example.com",
                LocalDateTime.now(), LocalDateTime.now().plusDays(1));
        when(urlShortenerRepository.findByShortId(shortId)).thenReturn(Optional.of(entity));

        Optional<String> result = urlShortenerService.getLongUrl(shortId);

        assertTrue(result.isPresent());
        assertEquals("https://example.com", result.get());
    }

    @Test
    void testGetLongUrl_NotFound() throws Exception {
        when(urlShortenerRepository.findByShortId(any())).thenReturn(Optional.empty());

        Optional<String> result = urlShortenerService.getLongUrl(any());

        assertFalse(result.isPresent());
        verify(urlShortenerRepository, times(1)).findByShortId(any());
    }


    @Test
    void testDeleteById_IdExists_ShouldDeleteSuccessfully() {
        String shortId = "existingId";
        UrlShortenerEntity entity = new UrlShortenerEntity(shortId, "https://example.com",
                LocalDateTime.now(), null);
        when(urlShortenerRepository.findByShortId(shortId)).thenReturn(Optional.of(entity));

        boolean result = urlShortenerService.deleteById(shortId);

        assertTrue(result);
        verify(urlShortenerRepository, times(1)).delete(entity);
    }

    @Test
    void testDeleteById_IdNotExists_ShouldThrowException() {
        String shortId = "nonExistingId";
        when(urlShortenerRepository.findByShortId(shortId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> urlShortenerService.deleteById(shortId));
        assertEquals("Specified ID not found: nonExistingId", exception.getMessage());
    }
}
