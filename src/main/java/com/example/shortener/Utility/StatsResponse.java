
package com.example.shortener;

import java.time.LocalDateTime;

public record StatsResponse(String shortCode, String originalUrl, LocalDateTime createdAt, long hitCount) {}
