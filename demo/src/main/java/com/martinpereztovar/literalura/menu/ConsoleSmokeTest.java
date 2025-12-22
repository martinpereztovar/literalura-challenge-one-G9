package com.martinpereztovar.literalura.menu;

import com.martinpereztovar.literalura.dto.GutendexBookDTO;
import com.martinpereztovar.literalura.service.BookSearchService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.stream.IntStream;

@Component
public class ConsoleSmokeTest implements CommandLineRunner {

    private final BookSearchService service;

    public ConsoleSmokeTest(BookSearchService service) {
        this.service = service;
    }

    @Override
    public void run(String... args) {
        var results = service.search("dickens"); // teste rápido

        System.out.println("\n=== Resultados (top 5) ===");
        IntStream.range(0, Math.min(5, results.size())).forEach(i -> {
            GutendexBookDTO b = results.get(i);
            String author = (b.authors() != null && !b.authors().isEmpty())
                    ? b.authors().get(0).name()
                    : "Autor desconhecido";
            System.out.printf("%d) [%d] %s — %s (downloads: %d)%n",
                    i + 1, b.id(), b.title(), author, b.download_count());
        });
        System.out.println("=========================\n");
    }
}
