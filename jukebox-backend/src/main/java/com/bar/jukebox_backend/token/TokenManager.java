package com.bar.jukebox_backend.token;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.logstash.logback.argument.StructuredArguments;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.util.Base64;

@Component
public class TokenManager {

    private static final Logger logger = LoggerFactory.getLogger(TokenManager.class);

    @Value("${spotify.refresh-token}")
    private String refreshToken;

    @Value("${spotify.client-id}")
    private String clientId;

    @Value("${spotify.client-secret}")
    private String clientSecret;
    
    @Autowired
    private HttpClient httpClient;
    
    private String accessToken;
    private Instant expirationTime;
    
    private static final int EXPIRATION_BUFFER_SECONDS = 60;

    @Scheduled(fixedRate = 3300000)
    public synchronized void updateAccessToken() throws IOException, InterruptedException {
        logger.info("Iniciando a atualização do token de acesso do Spotify.");

        String credentials = clientId + ":" + clientSecret;
        String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://accounts.spotify.com/api/token"))
                .header("Authorization", "Basic " + encodedCredentials)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(
                        "grant_type=refresh_token" +
                        "&refresh_token=" + refreshToken
                ))
                .build();
        
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        // Log estruturado com informações da resposta
        logger.info("Resposta da API de token do Spotify",
                StructuredArguments.kv("status_code", response.statusCode()),
                StructuredArguments.kv("response_body", response.body()));

        if (response.statusCode() != 200) {
            logger.error("Falha ao obter o token de acesso. Status: {}", response.statusCode());
            throw new IOException("Failed to get access token from Spotify API.");
        }

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonResponse = mapper.readTree(response.body());

        this.accessToken = jsonResponse.get("access_token").asText();
        long expiresIn = jsonResponse.get("expires_in").asLong();
        this.expirationTime = Instant.now().plusSeconds(expiresIn - EXPIRATION_BUFFER_SECONDS);

        
        logger.info("Novo token de acesso obtido com sucesso",
                StructuredArguments.kv("token_expiration_time", this.expirationTime.toString()));
    }
    
    public synchronized String getAccessToken() throws IOException, InterruptedException {
        if (accessToken == null || Instant.now().isAfter(expirationTime)) {
            logger.info("O token de acesso está nulo ou expirado. Iniciando a atualização.");
            updateAccessToken();
        }
        return accessToken;
    }
}