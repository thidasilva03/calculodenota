package com.atividade;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Casos de teste para a CalculadoraNota.
 * Cobrem as tres linhas da tabela de regras:
 *  - ME >= 8                 -> Aprovado (dispensado da PF)
 *  - ME <  8 e MF >= 5       -> Aprovado
 *  - ME <  8 e MF <  5       -> Reprovado
 */
class CalculadoraNotaTest {

    private final CalculadoraNota calc = new CalculadoraNota();
    private static final double DELTA = 0.0001;

    @Test
    void deveCalcularMediaDosExercicios() {
        // (7 + 8 + 9) / 3 = 8
        assertEquals(8.0, calc.calcularMediaExercicios(7, 8, 9), DELTA);
    }

    @Test
    void alunoComMediaAltaNaoFazProvaFinal() {
        assertFalse(calc.precisaFazerProvaFinal(8.0));   // ME = 8 -> dispensado
        assertTrue(calc.precisaFazerProvaFinal(7.9));    // ME < 8 -> faz PF
    }

    @Test
    void alunoDispensadoTemMediaFinalIgualAMedia() {
        // ME = 9 (>= 8) -> MF = ME, situacao Aprovado, PF ignorada
        assertEquals(9.0, calc.calcularMediaFinal(9.0, 0.0), DELTA);
        assertEquals("Aprovado", calc.situacaoFinal(9.0, 0.0));
    }

    @Test
    void alunoComProvaFinalSuficienteEhAprovado() {
        // ME = 6, PF = 6 -> MF = (2*6 + 6)/3 = 6 (>= 5) -> Aprovado
        assertEquals(6.0, calc.calcularMediaFinal(6.0, 6.0), DELTA);
        assertEquals("Aprovado", calc.situacaoFinal(6.0, 6.0));
    }

    @Test
    void alunoComProvaFinalInsuficienteEhReprovado() {
        // ME = 4, PF = 3 -> MF = (2*4 + 3)/3 = 3.6667 (< 5) -> Reprovado
        assertEquals("Reprovado", calc.situacaoFinal(4.0, 3.0));
    }
}
