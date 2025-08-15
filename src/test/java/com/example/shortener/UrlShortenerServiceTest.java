
package com.example.shortener;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UrlShortenerServiceTest {

    @Autowired
    UrlShortenerService service;

    @Test
    void shorten_and_stats_work() {
        ShortenResponse resp = service.shorten("https://example.com");
        assertNotNull(resp.shortCode());
        assertTrue(resp.shortUrl().endsWith("/" + resp.shortCode()));

        StatsResponse stats = service.stats(resp.shortCode());
        assertEquals("https://example.com", stats.originalUrl());
        assertEquals(0, stats.hitCount());

        // hit once
        String url = service.resolveAndHit(resp.shortCode());
        assertEquals("https://example.com", url);
        StatsResponse stats2 = service.stats(resp.shortCode());
        assertEquals(1, stats2.hitCount());
    }

    @Test
    void invalid_url_rejected() {
        assertThrows(InvalidUrlException.class, () -> service.shorten("notaurl"));
    }
}
