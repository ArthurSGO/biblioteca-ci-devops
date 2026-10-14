package br.edu.unialfa.biblioteca;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BibliotecaServiceTest {
    private BibliotecaService biblioteca;

    @BeforeEach
    void prepararAcervo() {
        biblioteca = new BibliotecaService(List.of(
            new Livro("L001", "Clean Code"),
            new Livro("L002", "Effective Java")
        ));
    }

    @Test
    void deveIniciarComTodosOsLivrosDisponiveis() {
        assertEquals(2, biblioteca.totalDeLivros());
        assertEquals(2, biblioteca.totalDisponiveis());
        assertTrue(biblioteca.estaDisponivel("L001"));
    }

    @Test
    void deveEmprestarLivro() {
        biblioteca.emprestar("L001");
        assertFalse(biblioteca.estaDisponivel("L001"));
        assertEquals(1, biblioteca.totalDisponiveis());
    }

    @Test
    void deveDevolverLivro() {
        biblioteca.emprestar("L001");
        biblioteca.devolver("L001");
        assertTrue(biblioteca.estaDisponivel("L001"));
        assertEquals(2, biblioteca.totalDisponiveis());
    }

    @Test
    void naoDeveEmprestarLivroDuasVezes() {
        biblioteca.emprestar("L001");
        IllegalStateException erro = assertThrows(IllegalStateException.class,
            () -> biblioteca.emprestar("L001"));
        assertTrue(erro.getMessage().contains("já está emprestado"));
    }

    @Test
    void naoDeveDevolverLivroQueNaoFoiEmprestado() {
        assertThrows(IllegalStateException.class, () -> biblioteca.devolver("L002"));
    }

    @Test
    void naoDeveEmprestarLivroInexistente() {
        assertThrows(IllegalArgumentException.class, () -> biblioteca.emprestar("XYZ"));
    }

    @Test
    void naoDeveDevolverLivroInexistente() {
        assertThrows(IllegalArgumentException.class, () -> biblioteca.devolver("XYZ"));
    }

    @Test
    void naoDeveVerificarLivroInexistente() {
        assertThrows(IllegalArgumentException.class, () -> biblioteca.estaDisponivel(null));
    }

    @Test
    void naoDeveAceitarCodigosDuplicados() {
        assertThrows(IllegalArgumentException.class, () -> new BibliotecaService(List.of(
            new Livro("L001", "Primeiro"), new Livro("L001", "Segundo")
        )));
    }

    @Test
    void naoDevePermitirAlterarListaRetornada() {
        List<Livro> livros = biblioteca.listarLivros();
        assertThrows(UnsupportedOperationException.class,
            () -> livros.add(new Livro("L003", "Novo")));
    }

    @Test
    void devePreservarAOrdemOriginalDosLivros() {
        assertEquals(List.of("L001", "L002"), biblioteca.listarLivros()
            .stream().map(Livro::codigo).toList());
    }
}
