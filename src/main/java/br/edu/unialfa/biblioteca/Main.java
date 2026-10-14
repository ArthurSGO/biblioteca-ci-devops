package br.edu.unialfa.biblioteca;

import java.util.List;

/** Exemplo executável independente de banco de dados ou servidor. */
public final class Main {
    private Main() {
    }

    public static void main(String[] args) {
        BibliotecaService biblioteca = new BibliotecaService(List.of(
            new Livro("L001", "Clean Code"),
            new Livro("L002", "Effective Java"),
            new Livro("L003", "The DevOps Handbook")
        ));

        System.out.println("=== Biblioteca CI - Trabalho 5 ===");
        System.out.println("Acervo inicial:");
        biblioteca.listarLivros().forEach(livro ->
            System.out.println(" - " + livro.codigo() + " | " + livro.titulo()));
        System.out.println("Disponíveis: " + biblioteca.totalDisponiveis());

        biblioteca.emprestar("L001");
        System.out.println("Empréstimo de L001 realizado.");
        System.out.println("Disponíveis após empréstimo: " + biblioteca.totalDisponiveis());

        biblioteca.devolver("L001");
        System.out.println("Devolução de L001 realizada.");
        System.out.println("Disponíveis após devolução: " + biblioteca.totalDisponiveis());
        System.out.println("Demonstração concluída com sucesso.");
    }
}
