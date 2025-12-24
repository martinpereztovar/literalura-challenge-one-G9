package com.martinpereztovar.literalura.repository;

import com.martinpereztovar.literalura.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;

public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByExternalId(Integer externalId);
    List<Book> findByLanguageIgnoreCase(String language);
    List<Book> findByOrderByDownloadCountDesc(Pageable pageable);
}


