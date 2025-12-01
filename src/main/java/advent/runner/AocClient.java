package advent.runner;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Optional;

public class AocClient {

    private static final String BASE_URL = "https://adventofcode.com";

    private final HttpClient client;
    private final String sessionToken;
    private final int year;

    public AocClient(int year) {
        this.year = year;
        this.client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        this.sessionToken = loadSessionToken()
                .orElseThrow(() -> new IllegalStateException("No AoC session token found. Set AOC_SESSION env var."));
    }

    private Optional<String> loadSessionToken() {
        String token = System.getenv("AOC_SESSION");
        if (token != null && !token.isBlank()) return Optional.of(token.trim());
        return Optional.empty();
    }

    public boolean fetchInput(int day, String fileName) {
        String url = String.format("%s/%d/day/%d/input", BASE_URL, year, day);
        ConsoleLog.info("Requesting input from " + url);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(10))
                .header("Cookie", "session=" + sessionToken)
                .header("User-Agent", "AdventRunner (Java HTTPClient)")
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                String body = response.body().trim();
                if (body.isEmpty()) {
                    ConsoleLog.warn("Received empty response (puzzle may not be unlocked yet).");
                    return false;
                }
                writeToResources(day, year, body, fileName);
                ConsoleLog.info("Fetched input for day " + day + " successfully.");
                return true;
            } else {
                ConsoleLog.error("Failed to fetch input. HTTP " + response.statusCode());
                ConsoleLog.error("Response body: " + response.body());
                return false;
            }
        } catch (Exception e) {
            ConsoleLog.error("Error fetching input: " + e.getMessage());
            return false;
        }
    }

    public boolean submitAnswer(int day, int part, String answer) {
        String url = String.format("%s/%d/day/%d/answer", BASE_URL, year, day);
        ConsoleLog.info(String.format("Submitting answer for day %d part %d ...", day, part));

        String form = String.format("level=%d&answer=%s", part, answer.trim());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(15))
                .header("Cookie", "session=" + sessionToken)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("User-Agent", "AdventRunner (Java HTTPClient)")
                .POST(HttpRequest.BodyPublishers.ofString(form))
                .build();

        try {
            HttpResponse<String> resp = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (resp.statusCode() != 200) {
                ConsoleLog.error("Submission failed: HTTP " + resp.statusCode());
                return false;
            }
            String body = resp.body();
            if (body.contains("That's the right answer")) {
                ConsoleLog.success("Submission accepted. Correct answer!");
                return true;
            }
            if (body.contains("That's not the right answer")) {
                ConsoleLog.warn("Submission rejected: wrong answer.");
                return false;
            }
            if (body.contains("You gave an answer too recently")) {
                ConsoleLog.warn("Submission rate‑limited; wait before retrying.");
                return false;
            }
            if (body.contains("You don't seem to be solving the right level")) {
                ConsoleLog.warn("Already solved or wrong level selected.");
                return false;
            }
            ConsoleLog.warn("Unrecognized response; check site manually.");
            return false;
        } catch (Exception e) {
            ConsoleLog.error("Submission error: " + e.getMessage());
            return false;
        }
    }

    private void writeToResources(int day, int year, String content, String fileName) {
        try {
            String dayFolder = String.format("src/main/resources/%d/day%02d", year, day);
            Files.createDirectories(Path.of(dayFolder));
            Path output = Path.of(dayFolder, fileName.toLowerCase() + ".txt");
            Files.writeString(output, content + System.lineSeparator());
            ConsoleLog.info("Saved input to " + output);
        } catch (IOException e) {
            ConsoleLog.error("Failed to write input file: " + e.getMessage());
        }
    }
}