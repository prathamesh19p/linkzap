
# URL Shortener (Java 17, Spring Boot 3)

A minimal, production-quality URL shortener API.

## Run

> Requires JDK 17 and Gradle. If you have the Gradle wrapper, use `./gradlew`. Otherwise run `gradle`.

```bash
# build
gradle clean build

# run
gradle bootRun
```

App starts on `http://localhost:8080`.

## Endpoints

### Shorten
```bash
curl -i -X POST http://localhost:8080/shorten   -H "Content-Type: application/json"   -d '{"url":"https://example.com/my/very/long/path?utm_source=test"}'
```
Response:
```json
{"shortCode":"abc123","shortUrl":"http://localhost:8080/abc123"}
```

### Redirect
```bash
curl -i http://localhost:8080/{shortCode}
```

### Stats
```bash
curl -s http://localhost:8080/stats/{shortCode} | jq
```

## Notes
- Base62 encoding of the numeric DB id ensures short, unique codes.
- H2 in-memory DB; enable the console at `/h2-console`.
- `app.base-url` controls the returned `shortUrl`.
```yaml
app:
  base-url: http://localhost:8080
```
