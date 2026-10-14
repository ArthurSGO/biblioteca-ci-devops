package br.edu.unialfa.biblioteca;

/** Representa um livro identificado por código único. */
public record Livro(String codigo, String titulo) {
    public Livro {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("O código do livro é obrigatório.");
        }
        if (titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException("O título do livro é obrigatório.");
        }
        codigo = codigo.trim();
        titulo = titulo.trim();
    }
}
