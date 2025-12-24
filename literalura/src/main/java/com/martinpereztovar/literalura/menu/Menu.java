package com.martinpereztovar.literalura.menu;

import com.martinpereztovar.literalura.domain.Author;
import com.martinpereztovar.literalura.domain.Book;
import com.martinpereztovar.literalura.service.CatalogService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

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
            System.out.println("""
        \n=== LITERALURA ===
        1 - Buscar livro por título (salvar o primeiro resultado)
        2 - Buscar livro por autor (salvar o primeiro resultado)
        3 - Listar todos os livros salvos
        4 - Listar livros por idioma
        5 - Listar autores salvos
        6 - Listar autores vivos em determinado ano
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
                case 1 -> searchAndSaveBook();
                case 2 -> searchAndSaveBookByAuthor();
                case 3 -> listAllBooks();
                case 4 -> listBooksByLanguage();
                case 5 -> listAllAuthors();
                case 6 -> listAuthorsAliveInYear();
                case 0 -> {
                    System.out.println("Encerrando aplicação...");
                    return; // encerra de verdade o menu
                }
                default -> System.out.println("Opção inválida. Tente novamente.");
            }

        }
    }

    private void searchAndSaveBook() {
        System.out.print("Digite o título para buscar: ");
        String title = scanner.nextLine().trim();

        if (title.isBlank()) {
            System.out.println("Erro: o título não pode ser vazio.");
            return;
        }

        Book saved = catalogService.searchAndSaveFirstByTitle(title);
        if (saved == null) {
            System.out.println("Nenhum resultado encontrado.");
            return;
        }

        System.out.println("\nLivro salvo no catálogo:");
        System.out.println(saved);
    }

    private void searchAndSaveBookByAuthor() {
        System.out.print("Digite o nome do autor para buscar: ");
        String authorName = scanner.nextLine().trim();

        if (authorName.isBlank()) {
            System.out.println("Erro: o nome do autor não pode ser vazio.");
            return;
        }

        Book saved = catalogService.searchAndSaveFirstByAuthor(authorName);
        if (saved == null) {
            System.out.println("Nenhum resultado encontrado para esse autor.");
            return;
        }

        System.out.println("\nLivro salvo no catálogo:");
        System.out.println(saved);
    }


    private void listAllBooks() {
        List<Book> books = catalogService.listAllBooks();
        if (books.isEmpty()) {
            System.out.println("Nenhum livro salvo ainda.");
            return;
        }

        System.out.println("\n=== Catálogo de livros ===");
        books.forEach(System.out::println);
    }

    private void listBooksByLanguage() {
        System.out.print("Digite o idioma (ex.: en, es, pt): ");
        String language = scanner.nextLine().trim();

        if (language.isBlank()) {
            System.out.println("Erro: idioma não pode ser vazio.");
            return;
        }

        List<Book> books = catalogService.listBooksByLanguage(language);
        if (books.isEmpty()) {
            System.out.println("Nenhum livro encontrado para esse idioma.");
            return;
        }

        System.out.println("\n=== Livros por idioma ===");
        books.forEach(System.out::println);
    }

    private void listAllAuthors() {
        List<Author> authors = catalogService.listAllAuthors();

        if (authors.isEmpty()) {
            System.out.println("Nenhum autor salvo ainda.");
            return;
        }

        System.out.println("\n=== Autores salvos ===");
        authors.forEach(System.out::println);
    }

    private void listAuthorsAliveInYear() {
        System.out.print("Digite o ano (ex.: 1900): ");
        String input = scanner.nextLine().trim();

        int year;
        try {
            year = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Erro: digite um ano válido.");
            return;
        }

        List<Author> authors = catalogService.listAuthorsAliveInYear(year);
        if (authors.isEmpty()) {
            System.out.println("Nenhum autor encontrado vivo nesse ano.");
            return;
        }

        System.out.println("\n=== Autores vivos em " + year + " ===");
        authors.forEach(System.out::println);
    }
}
