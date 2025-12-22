package com.martinpereztovar.literalura.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GutendexResponseDTO(
        int count,
        String next,
        String previous,
        List<GutendexBookDTO> results
) {}

