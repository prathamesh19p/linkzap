
package com.example.shortener;

import jakarta.validation.constraints.NotBlank;

public record ShortenRequest(
        @NotBlank(message = "url is required")
        String url
) {}
