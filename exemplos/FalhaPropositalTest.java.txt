package br.edu.unialfa.biblioteca;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FalhaPropositalTest {
    @Test
    void deveDetectarFalhaProposital() {
        assertEquals(2, 1, "Falha proposital para comprovar o bloqueio do CI");
    }
}
