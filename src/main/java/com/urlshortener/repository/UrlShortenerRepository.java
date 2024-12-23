package com.urlshortener.repository;

import com.urlshortener.entity.UrlShortenerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UrlShortenerRepository extends JpaRepository<UrlShortenerEntity, String> {
    Optional<UrlShortenerEntity> findByShortId(String shortId);
    void deleteAllByExpiresAtBefore(LocalDateTime now);

}
