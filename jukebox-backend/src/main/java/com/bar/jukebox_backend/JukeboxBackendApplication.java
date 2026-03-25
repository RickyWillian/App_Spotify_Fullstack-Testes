package com.bar.jukebox_backend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.net.http.HttpClient;
import java.time.Duration;

@SpringBootApplication
@EnableScheduling
public class JukeboxBackendApplication {

	private static final Logger logger = LoggerFactory.getLogger(JukeboxBackendApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(JukeboxBackendApplication.class, args);
	}

	@Bean
	public HttpClient httpClient() {
		return HttpClient.newBuilder()
				.connectTimeout(Duration.ofSeconds(10))
				.build();
	}

	@Bean
	public CommandLineRunner run() {
		return args -> {
			logger.info("Application starting...");
			logger.info("-------------------------------------------------");
			logger.info("Obtendo novo token de acesso...");
			logger.info("-------------------------------------------------");
		};
	}
}