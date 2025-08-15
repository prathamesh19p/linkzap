
package com.example.shortener;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UrlShortenerService {

    private final UrlMappingRepository repository;

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    public boolean isValidUrl(String url) {
        try {
            URI uri = new URI(url);
            if (uri.getScheme() == null) return false;
            String s = uri.getScheme().toLowerCase();
            return (s.equals("http") || s.equals("https")) && uri.getHost() != null;
        } catch (Exception e) {
            return false;
        }
    }

    @Transactional
    public ShortenResponse shorten(String url) {
        if (!isValidUrl(url)) {
            throw new InvalidUrlException("Invalid URL: " + url);
        }
        // Persist to get ID
        UrlMapping mapping = UrlMapping.builder()
                .originalUrl(url)
                .shortCode("")
                .createdAt(LocalDateTime.now())
                .hitCount(0)
                .build();
        mapping = repository.save(mapping);

        String code = Base62.encode(mapping.getId());
        mapping.setShortCode(code);
        repository.save(mapping);

        String shortUrl = baseUrl.endsWith("/") ? baseUrl + code : baseUrl + "/" + code;
        return new ShortenResponse(code, shortUrl);
    }

    @Transactional
    public String resolveAndHit(String shortCode) {
        UrlMapping mapping = repository.findByShortCode(shortCode)
                .orElseThrow(() -> new NotFoundException("Short code not found: " + shortCode));
        mapping.setHitCount(mapping.getHitCount() + 1);
        repository.save(mapping);
        return mapping.getOriginalUrl();
    }

    @Transactional(readOnly = true)
    public StatsResponse stats(String shortCode) {
        UrlMapping mapping = repository.findByShortCode(shortCode)
                .orElseThrow(() -> new NotFoundException("Short code not found: " + shortCode));
        return new StatsResponse(mapping.getShortCode(), mapping.getOriginalUrl(), mapping.getCreatedAt(), mapping.getHitCount());
    }
}
