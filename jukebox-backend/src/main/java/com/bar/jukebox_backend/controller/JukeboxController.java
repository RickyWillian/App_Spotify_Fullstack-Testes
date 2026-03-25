package com.bar.jukebox_backend.controller;

import com.bar.jukebox_backend.model.Track;
import com.bar.jukebox_backend.services.QueueAccessService;
import com.bar.jukebox_backend.services.SpotifyService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
@Validated
public class JukeboxController {

    @Autowired
    private SpotifyService spotifyService;
    
    @Autowired
    private QueueAccessService queueAccessService;

    @GetMapping("/search")
    public List<Track> searchTracks(
            @RequestParam("query") 
            @NotBlank(message = "O termo de busca não pode ser vazio.")
            @Size(min = 2, max = 50, message = "O termo de busca deve ter entre 2 e 50 caracteres.") 
            String query) throws IOException, InterruptedException {
        return spotifyService.searchTracks(query);
    }

    @PostMapping("/add-to-queue")
    public ResponseEntity<String> addToQueue(
            @RequestParam("uri") 
            @NotBlank(message = "A URI da música não pode ser vazia.") 
            String uri, HttpServletRequest request) throws IOException, InterruptedException {
       
        String ipAddress = request.getRemoteAddr();

        // 1. Verificação de rate limiting
        if (!queueAccessService.canAddSong(ipAddress)) {
            return new ResponseEntity<>("Você atingiu o limite de músicas adicionadas. Tente novamente mais tarde.", HttpStatus.TOO_MANY_REQUESTS);
        }

        // 2. Validação do formato da URI do Spotify
        if (!uri.startsWith("spotify:track:")) {
            return new ResponseEntity<>("A URI fornecida não é uma URI de faixa do Spotify válida.", HttpStatus.BAD_REQUEST);
        }

        // 3. Adicionar música à fila
        String result = spotifyService.addToQueue(uri);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}