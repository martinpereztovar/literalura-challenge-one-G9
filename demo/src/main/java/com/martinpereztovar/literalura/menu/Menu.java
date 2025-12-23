package com.martinpereztovar.literalura.menu;

import com.martinpereztovar.literalura.dto.GutendexAuthorDTO;
import com.martinpereztovar.literalura.dto.GutendexBookDTO;
import com.martinpereztovar.literalura.service.BookSearchService;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class Menu {

    private final BookSearchService bookSearchService;
    private final Scanner scanner = new Scanner(System.in);

    private final List<GutendexBookDTO> searchedBooks = new ArrayList<>();

    public Menu(BookSearchService bookSearchService) {
        this.bookSearchService = bookSearchService;
    }

    public void showMenu() {
        int option = -1;

        while (option != 0) {
            System.out.println("""
                    \n=== LITERALURA ===
                    1 - Buscar livro por título (salvar o primeiro resultado)
                    2 - Listar livros já buscados (nesta execução)
                    3 - Buscar livros por autor (salvar o primeiro resultado)
                    4 - Listar autores dos livros buscados
                    5 - Listar autores vivos em um determinado ano
                    0 - Sair
                    """);

            System.out.print("Escolha uma opção: ");
            String input = scanner.nextLine();

            try {
                option = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Erro: digite um número válido.");
                continue;
            }

            switch (option) {
                case 1 -> searchBookByTitle();
                case 2 -> listSearchedBooks();
                case 3 -> searchBookByAuthor();
                case 4 -> listAuthors();
                case 5 -> listLivingAuthorsByYear();
                case 0 -> System.out.println("Encerrando aplicação...");
                default -> System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    private void searchBookByTitle() {
        System.out.print("Digite o título para buscar: ");
        String title = scanner.nextLine().trim();

        if (title.isBlank()) {
            System.out.println("Erro: o título não pode ser vazio.");
            return;
        }

        List<GutendexBookDTO> results = bookSearchService.searchByTitle(title);
        saveFirstResult(results);
    }

    private void searchBookByAuthor() {
        System.out.print("Digite o nome do autor para buscar: ");
        String author = scanner.nextLine().trim();

        if (author.isBlank()) {
            System.out.println("Erro: o nome do autor não pode ser vazio.");
            return;
        }

        List<GutendexBookDTO> results = bookSearchService.searchByAuthor(author);
        saveFirstResult(results);
    }

    private void saveFirstResult(List<GutendexBookDTO> results) {
        if (results.isEmpty()) {
            System.out.println("Nenhum resultado encontrado.");
            return;
        }

        GutendexBookDTO first = results.get(0);

        boolean alreadySaved = searchedBooks.stream().anyMatch(b -> b.id() == first.id());
        if (!alreadySaved) searchedBooks.add(first);

        System.out.println("\nPrimeiro resultado:");
        System.out.println(formatBook(first));
        if (alreadySaved) {
            System.out.println("(Obs.: esse livro já estava no catálogo desta execução.)");
        }
    }

    private void listSearchedBooks() {
        if (searchedBooks.isEmpty()) {
            System.out.println("Nenhum livro buscado ainda.");
            return;
        }

        System.out.println("\n=== Livros buscados nesta execução ===");
        for (int i = 0; i < searchedBooks.size(); i++) {
            System.out.printf("%d) %s%n", i + 1, formatBook(searchedBooks.get(i)));
        }
    }

    private void listAuthors() {
        List<GutendexAuthorDTO> authors = extractDistinctFirstAuthors();

        if (authors.isEmpty()) {
            System.out.println("Nenhum autor disponível. Busque livros primeiro.");
            return;
        }

        System.out.println("\n=== Autores (dos livros buscados) ===");
        for (int i = 0; i < authors.size(); i++) {
            GutendexAuthorDTO a = authors.get(i);
            System.out.printf("%d) %s%n", i + 1, formatAuthor(a));
        }
    }

    private void listLivingAuthorsByYear() {
        System.out.print("Digite o ano (ex.: 1900): ");
        String input = scanner.nextLine().trim();

        int year;
        try {
            year = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Erro: digite um ano válido (número).");
            return;
        }

        List<GutendexAuthorDTO> authors = extractDistinctFirstAuthors();
        List<GutendexAuthorDTO> living = authors.stream()
                .filter(a -> isAuthorAliveInYear(a, year))
                .toList();

        if (authors.isEmpty()) {
            System.out.println("Nenhum autor disponível. Busque livros primeiro.");
            return;
        }

        if (living.isEmpty()) {
            System.out.println("Nenhum autor (dos livros buscados) estava vivo nesse ano.");
            return;
        }

        System.out.println("\n=== Autores vivos em " + year + " ===");
        for (int i = 0; i < living.size(); i++) {
            System.out.printf("%d) %s%n", i + 1, formatAuthor(living.get(i)));
        }
    }


    private List<GutendexAuthorDTO> extractDistinctFirstAuthors() {
        Map<String, GutendexAuthorDTO> byName = new LinkedHashMap<>();

        for (GutendexBookDTO book : searchedBooks) {
            GutendexAuthorDTO firstAuthor = getFirstAuthor(book);
            if (firstAuthor == null || firstAuthor.name() == null || firstAuthor.name().isBlank()) continue;

            String key = firstAuthor.name().trim().toLowerCase();
            byName.putIfAbsent(key, firstAuthor);
        }

        return new ArrayList<>(byName.values());
    }

    private GutendexAuthorDTO getFirstAuthor(GutendexBookDTO book) {
        if (book.authors() == null || book.authors().isEmpty()) return null;
        return book.authors().get(0); // Trello: only the first author
    }

    private boolean isAuthorAliveInYear(GutendexAuthorDTO author, int year) {
        Integer birth = author.birth_year();
        Integer death = author.death_year();

        if (birth == null) return false;

        boolean born = year >= birth;
        boolean notDeadYet = (death == null) || (year <= death);

        return born && notDeadYet;
    }

    private String formatAuthor(GutendexAuthorDTO a) {
        String birth = a.birth_year() != null ? a.birth_year().toString() : "?";
        String death = a.death_year() != null ? a.death_year().toString() : "?";
        return String.format("%s (%s - %s)", a.name(), birth, death);
    }

    private String formatBook(GutendexBookDTO book) {
        GutendexAuthorDTO firstAuthor = getFirstAuthor(book);

        String authorName = (firstAuthor != null && firstAuthor.name() != null && !firstAuthor.name().isBlank())
                ? firstAuthor.name()
                : "Autor desconhecido";

        String language = (book.languages() != null && !book.languages().isEmpty())
                ? book.languages().get(0)
                : "N/A";

        return String.format("[%d] %s — %s | idioma: %s | downloads: %d",
                book.id(), book.title(), authorName, language, book.download_count());
    }
}
