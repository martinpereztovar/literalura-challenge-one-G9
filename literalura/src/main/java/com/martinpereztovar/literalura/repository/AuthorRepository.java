package com.martinpereztovar.literalura.repository;

import com.martinpereztovar.literalura.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    Optional<Author> findByNameIgnoreCase(String name);

    // Vivos no ano: nasceu <= ano e morreu >= ano
    List<Author> findByBirthYearLessThanEqualAndDeathYearGreaterThanEqual(
            Integer birthYear,
            Integer deathYear
    );

    // Vivos no ano: nasceu <= ano e deathYear é null (ainda vivo)
    List<Author> findByBirthYearLessThanEqualAndDeathYearIsNull(Integer birthYear);
}
