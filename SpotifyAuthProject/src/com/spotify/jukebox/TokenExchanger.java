package com.spotify.jukebox;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;
import java.util.Scanner;

public class TokenExchanger {

	private static final String CLIENT_ID = System.getenv("SPOTIFY_CLIENT_ID");
	private static final String CLIENT_SECRET = System.getenv("SPOTIFY_CLIENT_SECRET");
    private static final String REDIRECT_URI = "https://galactoid-bessie-jumpily.ngrok-free.dev/callback";

    public static void main(String[] args) throws IOException, InterruptedException {
        
        System.out.println("Cole o código de autorização do console do Eclipse:");
        Scanner scanner = new Scanner(System.in);
        String authorizationCode = scanner.nextLine();
        
        String credentials = CLIENT_ID + ":" + CLIENT_SECRET;
        String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());

        HttpRequest request = HttpRequest.newBuilder()
        	    .uri(URI.create("https://accounts.spotify.com/api/token"))
        	    .header("Authorization", "Basic " + encodedCredentials)
        	    .header("Content-Type", "application/x-www-form-urlencoded")
        	    .POST(HttpRequest.BodyPublishers.ofString(
        	            "grant_type=authorization_code" +
        	            "&code=" + authorizationCode +
        	            "&redirect_uri=" + REDIRECT_URI
        	    ))
        	    .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("\nResposta da API (Tokens):");
        System.out.println(response.body());
        scanner.close();
    }

	
}

