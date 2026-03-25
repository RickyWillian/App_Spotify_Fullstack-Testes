package com.bar.jukebox_backend.services;

import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class QueueAccessService {

    // Uma classe interna para rastrear o estado de um IP
    private static class IpRequestState {
        private int count;
        private final LocalDateTime startTime;

        public IpRequestState() {
            this.count = 1;
            this.startTime = LocalDateTime.now();
        }

        public int getCount() {
            return count;
        }

        public void incrementCount() {
            this.count++;
        }

        public LocalDateTime getStartTime() {
            return startTime;
        }
    }

    // Mapa para armazenar o estado de cada IP
    private final Map<String, IpRequestState> requestStates = new ConcurrentHashMap<>();

    // Limites definidos na sua regra de negócio
    private static final int REQUEST_LIMIT = 3;
    private static final int COOLDOWN_MINUTES = 30;

    public boolean canAddSong(String ipAddress) {
        LocalDateTime now = LocalDateTime.now();
        IpRequestState state = requestStates.get(ipAddress);

        // Se o IP não tem estado ou o período de 30 minutos já passou, reseta o contador.
        if (state == null || state.getStartTime().plusMinutes(COOLDOWN_MINUTES).isBefore(now)) {
            requestStates.put(ipAddress, new IpRequestState());
            return true;
        }

        // Se o contador atingiu o limite, o usuário não pode adicionar mais músicas.
        if (state.getCount() >= REQUEST_LIMIT) {
            return false;
        }

        // Se o limite ainda não foi atingido, incrementa o contador e permite a ação.
        state.incrementCount();
        return true;
    }
}