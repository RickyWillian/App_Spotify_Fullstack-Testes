package com.bar.jukebox_backend.services;

import com.bar.jukebox_backend.model.Track;
import com.bar.jukebox_backend.token.TokenManager;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.logstash.logback.argument.StructuredArguments;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class SpotifyService {
    
    private static final Logger logger = LoggerFactory.getLogger(SpotifyService.class);
    
    @Autowired
    private TokenManager tokenManager;
    
    @Autowired
    private HttpClient httpClient;

    // Constantes para a lógica de retry
    private static final int MAX_RETRIES = 3;
    private static final long RETRY_DELAY_MS = 1000; // 1 segundo

    public List<Track> searchTracks(String query) throws IOException, InterruptedException {
        int attempt = 0;
        while (attempt < MAX_RETRIES) {
            try {
                String accessToken = tokenManager.getAccessToken();
                String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8.toString());
                String searchUrl = "https://api.spotify.com/v1/search?"
                        + "q=" + encodedQuery
                        + "&type=track"
                        + "&limit=10";

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(searchUrl))
                        .header("Authorization", "Bearer " + accessToken)
                        .GET()
                        .build();

                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() >= 400) {
                    throw new IOException("Erro na API do Spotify: Status " + response.statusCode() + " - " + response.body());
                }

                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(response.body());
                JsonNode items = root.path("tracks").path("items");

                logger.info("Busca de música bem-sucedida",
                        StructuredArguments.kv("query", query),
                        StructuredArguments.kv("track_count", items.size()));
                
                return mapper.convertValue(items, new TypeReference<List<Track>>() {});

            } catch (IOException | InterruptedException e) {
                attempt++;
                logger.warn("Tentativa #{} falhou para a busca de música. Motivo: {}", attempt, e.getMessage());
                if (attempt >= MAX_RETRIES) {
                    logger.error("Todas as {} tentativas falharam. Abortando a busca.", MAX_RETRIES);
                    throw e; // Relança a exceção após todas as tentativas falharem
                }
                // Adiciona um atraso antes da próxima tentativa
                Thread.sleep(RETRY_DELAY_MS);
            }
        }
        return null; // Este trecho nunca será alcançado
    }
    
    public String addToQueue(String uri) throws IOException, InterruptedException {
        String accessToken = tokenManager.getAccessToken();
        String queueUrl = "https://api.spotify.com/v1/me/player/queue"
                + "?uri=" + URLEncoder.encode(uri, StandardCharsets.UTF_8.toString());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(queueUrl))
                .header("Authorization", "Bearer " + accessToken)
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return "Música adicionada à fila! Status: " + response.statusCode();
    }
}