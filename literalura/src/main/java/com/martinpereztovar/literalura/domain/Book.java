package com.martinpereztovar.literalura.domain;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "books", uniqueConstraints = {
        @UniqueConstraint(name = "uk_book_external_id", columnNames = "externalId")
})
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Gutendex/Gutenberg id
    @Column(nullable = false)
    private Integer externalId;

    @Column(nullable = false)
    private String title;

    @Column(length = 10)
    private String language;

    private Integer downloadCount;

    @ManyToMany
    @JoinTable(
            name = "book_authors",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private Set<Author> authors = new HashSet<>();

    protected Book() {}

    public Book(Integer externalId, String title, String language, Integer downloadCount) {
        this.externalId = externalId;
        this.title = title;
        this.language = language;
        this.downloadCount = downloadCount;
    }

    public Long getId() { return id; }
    public Integer getExternalId() { return externalId; }
    public String getTitle() { return title; }
    public String getLanguage() { return language; }
    public Integer getDownloadCount() { return downloadCount; }
    public Set<Author> getAuthors() { return authors; }

    public void addAuthor(Author author) {
        this.authors.add(author);
        author.getBooks().add(this);
    }

    @Override
    public String toString() {
        return String.format("[%d] %s | idioma: %s | downloads: %d",
                externalId,
                title,
                (language != null ? language : "N/A"),
                (downloadCount != null ? downloadCount : 0));
    }

}
