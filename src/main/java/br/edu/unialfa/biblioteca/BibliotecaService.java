package br.edu.unialfa.biblioteca;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/** Regras de empréstimo e devolução; os dados são armazenados em memória. */
public class BibliotecaService {
    private final Map<String, Livro> acervo = new LinkedHashMap<>();
    private final Set<String> emprestados = new HashSet<>();

    public BibliotecaService(Collection<Livro> livros) {
        Objects.requireNonNull(livros, "A lista de livros não pode ser nula.");
        for (Livro livro : livros) {
            Objects.requireNonNull(livro, "Nenhum livro pode ser nulo.");
            if (acervo.putIfAbsent(livro.codigo(), livro) != null) {
                throw new IllegalArgumentException("Código de livro duplicado: " + livro.codigo());
            }
        }
    }

    public List<Livro> listarLivros() {
        return List.copyOf(new ArrayList<>(acervo.values()));
    }

    public int totalDeLivros() {
        return acervo.size();
    }

    public int totalDisponiveis() {
        return acervo.size() - emprestados.size();
    }

    public boolean estaDisponivel(String codigo) {
        validarLivroExistente(codigo);
        return !emprestados.contains(codigo);
    }

    public void emprestar(String codigo) {
        validarLivroExistente(codigo);
        if (!emprestados.add(codigo)) {
            throw new IllegalStateException("O livro já está emprestado: " + codigo);
        }
    }

    public void devolver(String codigo) {
        validarLivroExistente(codigo);
        if (!emprestados.remove(codigo)) {
            throw new IllegalStateException("O livro não está emprestado: " + codigo);
        }
    }

    private void validarLivroExistente(String codigo) {
        if (codigo == null || !acervo.containsKey(codigo)) {
            throw new IllegalArgumentException("Livro não encontrado: " + codigo);
        }
    }
}
