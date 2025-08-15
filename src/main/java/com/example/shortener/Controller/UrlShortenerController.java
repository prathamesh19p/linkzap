package com.example.shortener;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for URL shortening service.
 */
@RestController
@RequestMapping("/api/urls")
@RequiredArgsConstructor
public class UrlShortenerController {

    private final UrlShortenerService service;

    /**
     * Creates a short URL for the given original URL.
     */
    @PostMapping(value = "/shorten", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ShortenResponse> createShortUrl(@Valid @RequestBody ShortenRequest request) {
        ShortenResponse response = service.shorten(request.url());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Redirects to the original URL for the given short code.
     */
    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirectToOriginalUrl(@PathVariable String shortCode) {
        String originalUrl = service.resolveAndHit(shortCode);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.LOCATION, originalUrl);

        return ResponseEntity.status(HttpStatus.FOUND).headers(headers).build();
    }

    /**
     * Retrieves statistics for the given short code.
     */
    @GetMapping(value = "/stats/{shortCode}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StatsResponse> getStats(@PathVariable String shortCode) {
        StatsResponse stats = service.stats(shortCode);
        return ResponseEntity.ok(stats);
    }
}
