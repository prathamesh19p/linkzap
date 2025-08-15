
package com.example.shortener;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class UrlShortenerController {

    private final UrlShortenerService service;

    @PostMapping(path = "/shorten", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ShortenResponse> shorten(@Valid @RequestBody ShortenRequest request) {
        ShortenResponse resp = service.shorten(request.url());
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    @GetMapping("/{shortCode}")
    public void redirect(@PathVariable String shortCode, HttpServletResponse response) throws IOException {
        String original = service.resolveAndHit(shortCode);
        response.setStatus(HttpServletResponse.SC_FOUND);
        response.setHeader("Location", original);
    }

    @GetMapping(path = "/stats/{shortCode}", produces = MediaType.APPLICATION_JSON_VALUE)
    public StatsResponse stats(@PathVariable String shortCode) {
        return service.stats(shortCode);
    }
}
