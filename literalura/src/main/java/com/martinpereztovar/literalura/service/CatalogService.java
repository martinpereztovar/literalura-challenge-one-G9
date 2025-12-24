package com.martinpereztovar.literalura.service;

import com.martinpereztovar.literalura.domain.Author;
import com.martinpereztovar.literalura.domain.Book;
import com.martinpereztovar.literalura.dto.GutendexAuthorDTO;
import com.martinpereztovar.literalura.dto.GutendexBookDTO;
import com.martinpereztovar.literalura.repository.AuthorRepository;
import com.martinpereztovar.literalura.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.LinkedHashSet;
import java.util.Set;
import org.springframework.data.domain.PageRequest;


@Service
public class CatalogService {

    private final BookSearchService bookSearchService;
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public CatalogService(BookSearchService bookSearchService,
                          BookRepository bookRepository,
                          AuthorRepository authorRepository) {
        this.bookSearchService = bookSearchService;
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    @Transactional
    public Book searchAndSaveFirstByTitle(String titleQuery) {
        List<GutendexBookDTO> results = bookSearchService.searchByTitle(titleQuery);
        if (results.isEmpty()) return null;

        GutendexBookDTO first = results.get(0);

        var existing = bookRepository.findByExternalId(first.id());
        if (existing.isPresent()) return existing.get();

        String language = resolveFirstLanguage(first.languages());
        Book book = new Book(first.id(), first.title(), language, first.download_count());

        if (first.authors() != null) {
            for (GutendexAuthorDTO a : first.authors()) {
                if (a == null || a.name() == null || a.name().isBlank()) continue;

                Author author = authorRepository
                        .findByNameIgnoreCase(a.name().trim())
                        .orElseGet(() -> authorRepository.save(
                                new Author(a.name().trim(), a.birth_year(), a.death_year())
                        ));

                book.addAuthor(author);
            }
        }

        return bookRepository.save(book);
    }

    @Transactional
    public Book searchAndSaveFirstByAuthor(String authorQuery) {
        List<GutendexBookDTO> results = bookSearchService.searchByTitle(authorQuery);

        if (results.isEmpty()) return null;

        String q = authorQuery.trim().toLowerCase();

        GutendexBookDTO firstMatch = results.stream()
                .filter(b -> b.authors() != null && b.authors().stream().anyMatch(a ->
                        a != null && a.name() != null && a.name().toLowerCase().contains(q)
                ))
                .findFirst()
                .orElse(null);

        if (firstMatch == null) return null;

        var existing = bookRepository.findByExternalId(firstMatch.id());
        if (existing.isPresent()) return existing.get();

        String language = resolveFirstLanguage(firstMatch.languages());
        Book book = new Book(firstMatch.id(), firstMatch.title(), language, firstMatch.download_count());

        if (firstMatch.authors() != null) {
            for (GutendexAuthorDTO a : firstMatch.authors()) {
                if (a == null || a.name() == null || a.name().isBlank()) continue;

                Author author = authorRepository
                        .findByNameIgnoreCase(a.name().trim())
                        .orElseGet(() -> authorRepository.save(
                                new Author(a.name().trim(), a.birth_year(), a.death_year())
                        ));

                book.addAuthor(author);
            }
        }

        return bookRepository.save(book);
    }


    public List<Book> listAllBooks() {
        return bookRepository.findAll();
    }

    public List<Book> listBooksByLanguage(String language) {
        return bookRepository.findByLanguageIgnoreCase(language);
    }

    private String resolveFirstLanguage(List<String> languages) {
        if (languages == null || languages.isEmpty()) return null;
        return languages.get(0);
    }

    public List<Author> listAllAuthors() {
        return authorRepository.findAll();
    }

    public List<Author> listAuthorsAliveInYear(int year) {
        Set<Author> alive = new LinkedHashSet<>();

        alive.addAll(authorRepository
                .findByBirthYearLessThanEqualAndDeathYearGreaterThanEqual(year, year));

        alive.addAll(authorRepository
                .findByBirthYearLessThanEqualAndDeathYearIsNull(year));

        return List.copyOf(alive);
    }

    public List<Book> top10MostDownloaded() {
        return bookRepository.findByOrderByDownloadCountDesc(PageRequest.of(0, 10));
    }

}
