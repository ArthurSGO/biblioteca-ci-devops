package br.edu.unialfa.biblioteca;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LivroTest {
    @Test
    void deveCriarLivroValido() {
        Livro livro = new Livro("L100", "Pipelines de CI");
        assertEquals("L100", livro.codigo());
        assertEquals("Pipelines de CI", livro.titulo());
    }

    @Test
    void naoDeveAceitarCodigoEmBranco() {
        assertThrows(IllegalArgumentException.class, () -> new Livro(" ", "Livro"));
    }

    @Test
    void naoDeveAceitarTituloNulo() {
        assertThrows(IllegalArgumentException.class, () -> new Livro("L100", null));
    }

    @Test
    void deveRemoverEspacosNasExtremidades() {
        Livro livro = new Livro(" L100 ", " Java ");
        assertEquals("L100", livro.codigo());
        assertEquals("Java", livro.titulo());
    }
}
