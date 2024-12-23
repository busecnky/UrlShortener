package com.urlshortener.service;

import com.urlshortener.dto.ShortenUrlRequestDto;
import com.urlshortener.entity.UrlShortenerEntity;
import com.urlshortener.repository.UrlShortenerRepository;
import com.urlshortener.utils.IdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UrlShortenerService {
    private static final Logger logger = LoggerFactory.getLogger(UrlShortenerService.class);
    private final UrlShortenerRepository urlShortenerRepository;
    private final IdGenerator idGenerator;

    public UrlShortenerService(UrlShortenerRepository urlShortenerRepository, IdGenerator idGenerator) {
        this.urlShortenerRepository = urlShortenerRepository;
        this.idGenerator = idGenerator;
    }

    public Optional<String> shortenUrl(ShortenUrlRequestDto shortenUrlRequestDto) {
        if (shortenUrlRequestDto.getShortId() != null &&
                urlShortenerRepository.findByShortId(shortenUrlRequestDto.getShortId()).isPresent()) {
            throw new IllegalArgumentException("ID already exists");
        }

        logger.debug("Creating UrlShortenerEntity. LongURL: {}, TTL: {}",
                shortenUrlRequestDto.getLongUrl(), shortenUrlRequestDto.getTtlInSeconds());

        String shortId = (shortenUrlRequestDto.getShortId() != null) ?
                shortenUrlRequestDto.getShortId() : idGenerator.generateUniqueId(6);
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime expiresAt = (shortenUrlRequestDto.getTtlInSeconds() != null) ?
                createdAt.plusSeconds(shortenUrlRequestDto.getTtlInSeconds()) : null;

        UrlShortenerEntity urlShortenerEntity = new UrlShortenerEntity(shortId, shortenUrlRequestDto.getLongUrl(),
                createdAt, expiresAt);
        urlShortenerRepository.save(urlShortenerEntity);
        logger.info("UrlShortenerEntity is saved. ShortId: {}, ExpiresAt: {}", shortId, expiresAt);
        return Optional.ofNullable(shortId);
    }

    public Optional<String> getLongUrl(String shortId) {
        logger.debug("ShortId is being searching: {}", shortId);
        Optional<UrlShortenerEntity> optionalUrlShortener = urlShortenerRepository.findByShortId(shortId);
        if (optionalUrlShortener.isPresent()) {
            UrlShortenerEntity urlShortenerEntity = optionalUrlShortener.get();
            if (urlShortenerEntity.getExpiresAt() != null &&
                    urlShortenerEntity.getExpiresAt().isBefore(LocalDateTime.now())) {
                urlShortenerRepository.delete(urlShortenerEntity);
                throw new IllegalArgumentException("This URL has expired.");
            }
            return Optional.ofNullable(urlShortenerEntity.getLongUrl());
        }
        return Optional.empty();
    }

    public boolean deleteById(String id) {
        Optional<UrlShortenerEntity> urlShortenerEntityOptional = urlShortenerRepository.findByShortId(id);

        if (urlShortenerEntityOptional.isPresent()) {
            urlShortenerRepository.delete(urlShortenerEntityOptional.get());
            logger.info("Short ID is deleted successfully. ID: {}", id);
            return true;
        } else {
            logger.warn("Short ID could not be deleted. ID not found: {}", id);
            throw new IllegalArgumentException("Specified ID not found: " + id);
        }
    }

}
