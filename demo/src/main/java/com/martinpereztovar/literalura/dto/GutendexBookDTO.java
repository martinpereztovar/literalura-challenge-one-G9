package com.martinpereztovar.literalura.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GutendexBookDTO(
        int id,
        String title,
        List<GutendexAuthorDTO> authors,
        List<String> languages,
        int download_count
) {}
