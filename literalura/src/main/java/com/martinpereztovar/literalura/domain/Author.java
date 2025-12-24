package com.martinpereztovar.literalura.domain;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "authors", uniqueConstraints = {
        @UniqueConstraint(name = "uk_author_name", columnNames = "name")
})
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private Integer birthYear;
    private Integer deathYear;

    @ManyToMany(mappedBy = "authors")
    private Set<Book> books = new HashSet<>();

    protected Author() {}

    public Author(String name, Integer birthYear, Integer deathYear) {
        this.name = name;
        this.birthYear = birthYear;
        this.deathYear = deathYear;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public Integer getBirthYear() { return birthYear; }
    public Integer getDeathYear() { return deathYear; }
    public Set<Book> getBooks() { return books; }

    @Override
    public String toString() {
        String birth = birthYear != null ? birthYear.toString() : "?";
        String death = deathYear != null ? deathYear.toString() : "?";
        return name + " (" + birth + " - " + death + ")";
    }
}
