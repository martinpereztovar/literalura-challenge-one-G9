package com.martinpereztovar.literalura.client;

import com.martinpereztovar.literalura.dto.GutendexResponseDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
public class GutendexClient {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String BASE_URL = "https://gutendex.com/books";

    public GutendexResponseDTO searchBooks(String query) {
        String url = UriComponentsBuilder
                .fromUri(URI.create(BASE_URL))
                .queryParam("search", query)
                .toUriString();

        return restTemplate.getForObject(url, GutendexResponseDTO.class);
    }
}
