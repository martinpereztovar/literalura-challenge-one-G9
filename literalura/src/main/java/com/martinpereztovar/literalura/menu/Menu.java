package com.martinpereztovar.literalura.menu;

import com.martinpereztovar.literalura.domain.Author;
import com.martinpereztovar.literalura.domain.Book;
import com.martinpereztovar.literalura.service.CatalogService;
import com.martinpereztovar.literalura.util.AnsiColors;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

import static com.martinpereztovar.literalura.util.AnsiColors.*;

@Component
public class Menu {

    private final CatalogService catalogService;
    private final Scanner scanner = new Scanner(System.in);

    public Menu(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    public void showMenu() {
        int option = -1;

        while (option != 0) {
            printMainMenu();

            System.out.print(BRIGHT_YELLOW + "➜ " + RESET + "Escolha uma opção: ");
            String input = scanner.nextLine();

            try {
                option = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println(error("Digite um número válido."));
                continue;
            }

            switch (option) {
                case 1 -> searchAndSaveBook();
                case 2 -> searchAndSaveBookByAuthor();
                case 3 -> listAllBooks();
                case 4 -> listBooksByLanguage();
                case 5 -> listAllAuthors();
                case 6 -> listAuthorsAliveInYear();
                case 7 -> listTop10MostDownloaded();
                case 0 -> {
                    System.out.println(success("Encerrando aplicação. Até logo!"));
                    return;
                }
                default -> {
                    System.out.println(error("Opção inválida. Tente novamente."));
                    pauseAndContinue();
                }
            }
        }
    }

    private void printMainMenu() {
        System.out.println("\n");
        printBox("LITERALURA - CATÁLOGO DE LIVROS");
        System.out.println();

        System.out.println(BRIGHT_CYAN + " 📖 BUSCAR E ADICIONAR" + RESET);
        System.out.println("   " + BRIGHT_WHITE + "1" + RESET + " → Buscar livro por título");
        System.out.println("   " + BRIGHT_WHITE + "2" + RESET + " → Buscar livro por autor");
        System.out.println();

        System.out.println(BRIGHT_GREEN + " 📚 CONSULTAR LIVROS" + RESET);
        System.out.println("   " + BRIGHT_WHITE + "3" + RESET + " → Listar todos os livros salvos");
        System.out.println("   " + BRIGHT_WHITE + "4" + RESET + " → Listar livros por idioma");
        System.out.println("   " + BRIGHT_WHITE + "7" + RESET + " → Top 10 livros mais baixados");
        System.out.println();

        System.out.println(BRIGHT_PURPLE + " ✍️  CONSULTAR AUTORES" + RESET);
        System.out.println("   " + BRIGHT_WHITE + "5" + RESET + " → Listar autores salvos");
        System.out.println("   " + BRIGHT_WHITE + "6" + RESET + " → Listar autores vivos em determinado ano");
        System.out.println();

        System.out.println(BRIGHT_RED + " 🚪 SAIR" + RESET);
        System.out.println("   " + BRIGHT_WHITE + "0" + RESET + " → Encerrar aplicação");
        System.out.println();
        printSeparator();
    }

    private void searchAndSaveBook() {
        System.out.println("\n" + title("═══ BUSCAR LIVRO POR TÍTULO ═══"));
        System.out.print(BRIGHT_YELLOW + "📝 " + RESET + "Digite o título: ");
        String title = scanner.nextLine().trim();

        if (title.isBlank()) {
            System.out.println(error("O título não pode ser vazio."));
            return;
        }

        System.out.println(info("Buscando..."));
        Book saved = catalogService.searchAndSaveFirstByTitle(title);

        if (saved == null) {
            System.out.println(warning("Nenhum resultado encontrado."));
            return;
        }

        System.out.println(success("Livro salvo no catálogo!"));
        printSeparator();
        printBookDetails(saved);
    }

    private void searchAndSaveBookByAuthor() {
        System.out.println("\n" + title("═══ BUSCAR LIVRO POR AUTOR ═══"));
        System.out.print(BRIGHT_YELLOW + "✍️  " + RESET + "Digite o nome do autor: ");
        String authorName = scanner.nextLine().trim();

        if (authorName.isBlank()) {
            System.out.println(error("O nome do autor não pode ser vazio."));
            return;
        }

        System.out.println(info("Buscando..."));
        Book saved = catalogService.searchAndSaveFirstByAuthor(authorName);

        if (saved == null) {
            System.out.println(warning("Nenhum resultado encontrado para esse autor."));
            return;
        }

        System.out.println(success("Livro salvo no catálogo!"));
        printSeparator();
        printBookDetails(saved);
    }

    private void listAllBooks() {
        List<Book> books = catalogService.listAllBooks();

        System.out.println("\n" + title("═══ CATÁLOGO DE LIVROS ═══"));

        if (books.isEmpty()) {
            System.out.println(warning("Nenhum livro salvo ainda."));
            return;
        }

        System.out.println(BRIGHT_GREEN + "Total de livros: " + books.size() + RESET);
        printSeparator();

        books.forEach(this::printBookDetails);
    }

    private void listBooksByLanguage() {
        System.out.println("\n" + title("═══ LIVROS POR IDIOMA ═══"));
        System.out.print(BRIGHT_YELLOW + "🌍 " + RESET + "Digite o idioma (ex.: en, es, pt): ");
        String language = scanner.nextLine().trim();

        if (language.isBlank()) {
            System.out.println(error("O idioma não pode ser vazio."));
            return;
        }

        List<Book> books = catalogService.listBooksByLanguage(language);

        if (books.isEmpty()) {
            System.out.println(warning("Nenhum livro encontrado para o idioma: " + language));
            return;
        }

        System.out.println(BRIGHT_GREEN + "Encontrados " + books.size() + " livro(s) em " + language.toUpperCase() + RESET);
        printSeparator();

        books.forEach(this::printBookDetails);
    }

    private void listAllAuthors() {
        List<Author> authors = catalogService.listAllAuthors();

        System.out.println("\n" + title("═══ AUTORES SALVOS ═══"));

        if (authors.isEmpty()) {
            System.out.println(warning("Nenhum autor salvo ainda."));
            return;
        }

        System.out.println(BRIGHT_GREEN + "Total de autores: " + authors.size() + RESET);
        printSeparator();

        authors.forEach(this::printAuthorDetails);
    }

    private void listAuthorsAliveInYear() {
        System.out.println("\n" + title("═══ AUTORES VIVOS EM DETERMINADO ANO ═══"));
        System.out.print(BRIGHT_YELLOW + "📅 " + RESET + "Digite o ano (ex.: 1900): ");
        String input = scanner.nextLine().trim();

        int year;
        try {
            year = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println(error("Digite um ano válido."));
            return;
        }

        List<Author> authors = catalogService.listAuthorsAliveInYear(year);

        if (authors.isEmpty()) {
            System.out.println(warning("Nenhum autor encontrado vivo em " + year + "."));
            return;
        }

        System.out.println(BRIGHT_GREEN + "Autores vivos em " + year + ": " + authors.size() + RESET);
        printSeparator();

        authors.forEach(this::printAuthorDetails);
    }

    private void listTop10MostDownloaded() {
        List<Book> books = catalogService.top10MostDownloaded();

        System.out.println("\n" + title("═══ TOP 10 LIVROS MAIS BAIXADOS ═══"));

        if (books.isEmpty()) {
            System.out.println(warning("Nenhum livro salvo ainda."));
            return;
        }

        printSeparator();

        for (int i = 0; i < books.size(); i++) {
            Book b = books.get(i);
            int downloads = b.getDownloadCount() != null ? b.getDownloadCount() : 0;

            String medal = switch (i) {
                case 0 -> BRIGHT_YELLOW + "🥇";
                case 1 -> BRIGHT_WHITE + "🥈";
                case 2 -> YELLOW + "🥉";
                default -> BRIGHT_BLUE + (i + 1) + "º";
            };

            System.out.println(medal + RESET + " " + BOLD + BRIGHT_WHITE + b.getTitle() + RESET);
            System.out.println("   " + BRIGHT_CYAN + "⬇ " + RESET + downloads + " downloads");
            System.out.println();
        }
    }



    private void printBookDetails(Book book) {
        System.out.println(BOLD + BRIGHT_CYAN + "📖 " + book.getTitle() + RESET);

        try {

            String bookInfo = book.toString();
            if (bookInfo != null && !bookInfo.isEmpty()) {

                String[] lines = bookInfo.split("\n");
                for (int i = 1; i < lines.length; i++) { // pula a primeira linha (título)
                    System.out.println("   " + BRIGHT_YELLOW + lines[i] + RESET);
                }
            }
        } catch (Exception e) {

            System.out.println("   " + DIM + "(detalhes do livro)" + RESET);
        }

        System.out.println();
    }

    private void printAuthorDetails(Author author) {
        System.out.println(BOLD + BRIGHT_PURPLE + "✍️  " + author.getName() + RESET);

        try {

            String authorInfo = author.toString();
            if (authorInfo != null && !authorInfo.isEmpty()) {
                String[] lines = authorInfo.split("\n");
                for (int i = 1; i < lines.length; i++) { // pula a primeira linha (nome)
                    System.out.println("   " + BRIGHT_GREEN + lines[i] + RESET);
                }
            }
        } catch (Exception e) {
            System.out.println("   " + DIM + "(detalhes do autor)" + RESET);
        }

        System.out.println();
    }

    private void pauseAndContinue() {
        System.out.println(DIM + "\nPressione ENTER para continuar..." + RESET);
        scanner.nextLine();
    }
}