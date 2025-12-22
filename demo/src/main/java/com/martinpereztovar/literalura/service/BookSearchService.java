package com.martinpereztovar.literalura.service;

import com.martinpereztovar.literalura.client.GutendexClient;
import com.martinpereztovar.literalura.dto.GutendexBookDTO;
import com.martinpereztovar.literalura.dto.GutendexResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookSearchService {

    private final GutendexClient client;

    public BookSearchService(GutendexClient client) {
        this.client = client;
    }

    public List<GutendexBookDTO> search(String query) {
        GutendexResponseDTO response = client.searchBooks(query);
        if (response == null || response.results() == null) return List.of();
        return response.results();
    }
}
