package com.urlshortener.controller;

import com.urlshortener.dto.ShortenUrlRequestDto;
import com.urlshortener.service.UrlShortenerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UrlShortenerControllerTest {

    @Mock
    private UrlShortenerService urlShortenerService;

    @InjectMocks
    private UrlShortenerController urlShortenerController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testShortenUrl() {
        ShortenUrlRequestDto requestDto = new ShortenUrlRequestDto("https://example.com", null, 1800L);
        when(urlShortenerService.shortenUrl(requestDto)).thenReturn(Optional.of("shortId"));

        ResponseEntity<Optional<String>> response = urlShortenerController.shortenUrl(requestDto);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("shortId", response.getBody().get());
        verify(urlShortenerService, times(1)).shortenUrl(requestDto);
    }

    @Test
    void testGetLongUrl() {
        String shortId = "shortId";
        when(urlShortenerService.getLongUrl(shortId)).thenReturn(Optional.of("https://example.com"));

        ResponseEntity<Optional<String>> response = urlShortenerController.getLongUrl(shortId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("https://example.com", response.getBody().get());
        verify(urlShortenerService, times(1)).getLongUrl(shortId);
    }

    @Test
    void testDeleteById() {
        String shortId = "shortId";
        when(urlShortenerService.deleteById(shortId)).thenReturn(true);

        ResponseEntity<Boolean> response = urlShortenerController.deleteById(shortId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(true, response.getBody());
        verify(urlShortenerService, times(1)).deleteById(shortId);
    }
}
