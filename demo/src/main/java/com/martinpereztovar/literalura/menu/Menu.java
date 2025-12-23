package com.martinpereztovar.literalura.menu;

import com.martinpereztovar.literalura.dto.GutendexBookDTO;
import com.martinpereztovar.literalura.service.BookSearchService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

@Component
public class Menu {

    private final BookSearchService bookSearchService;
    private final Scanner scanner = new Scanner(System.in);

    public Menu(BookSearchService bookSearchService) {
        this.bookSearchService = bookSearchService;
    }

    public void showMenu() {
        int option = -1;

        while (option != 0) {
            System.out.println("""
                    \n=== LITERALURA ===
                    1 - Buscar livros por título ou autor
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
                case 1 -> searchBooks();
                case 0 -> System.out.println("Encerrando aplicação...");
                default -> System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    private void searchBooks() {
        System.out.print("Digite um termo para busca: ");
        String query = scanner.nextLine().trim();

        if (query.isBlank()) {
            System.out.println("Erro: o termo de busca não pode ser vazio.");
            return;
        }

        List<GutendexBookDTO> results = bookSearchService.search(query);

        if (results.isEmpty()) {
            System.out.println("Nenhum livro encontrado (base do Gutenberg contém apenas obras em domínio público).");
            return;
        }

        System.out.println("\n=== Resultados (top 10) ===");
        for (int i = 0; i < Math.min(10, results.size()); i++) {
            GutendexBookDTO book = results.get(i);

            String author = (book.authors() != null && !book.authors().isEmpty())
                    ? book.authors().get(0).name()
                    : "Autor desconhecido";

            String language = (book.languages() != null && !book.languages().isEmpty())
                    ? book.languages().get(0)
                    : "N/A";

            System.out.printf("%d) [%d] %s — %s | idioma: %s | downloads: %d%n",
                    i + 1, book.id(), book.title(), author, language, book.download_count());
        }
        System.out.println("===========================");
    }
}
