package com.spotify.jukebox;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Random;

public class SpotifyAuthUrlGenerator {

	private static final String CLIENT_ID = System.getenv("SPOTIFY_CLIENT_ID");
    private static final String REDIRECT_URI = "https://galactoid-bessie-jumpily.ngrok-free.dev/callback";
    private static final String SCOPE = "user-modify-playback-state";

    public static void main(String[] args) {
        try {
            // Gerar um valor aleatório para o parâmetro 'state'
            String state = generateRandomString(16);

            // Montar a URL de autorização
            String authUrl = "https://accounts.spotify.com/authorize?"
                    + "client_id=" + URLEncoder.encode(CLIENT_ID, "UTF-8")
                    + "&response_type=code"
                    + "&redirect_uri=" + URLEncoder.encode(REDIRECT_URI, "UTF-8")
                    + "&scope=" + URLEncoder.encode(SCOPE, "UTF-8")
                    + "&state=" + URLEncoder.encode(state, "UTF-8");

            System.out.println("Cole esta URL no seu navegador e pressione Enter:\n");
            System.out.println(authUrl);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private static String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(characters.charAt(random.nextInt(characters.length())));
        }
        return sb.toString();
    }
}