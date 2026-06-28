package com.atividade;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Casos de teste para a CalculadoraNota.
 * Exploram limites (8.0 na ME, 5.0 na MF), notas decimais,
 * caso extremo (tudo zero) e a garantia de que a PF e ignorada
 * quando o aluno e dispensado.
 */
class CalculadoraNotaTest {

    private final CalculadoraNota calc = new CalculadoraNota();
    private static final double DELTA = 0.0001;

    @Test
    void mediaExerciciosCalculaCorretamenteComNotasDecimais() {
        // (7.5 + 8.2 + 9.1) / 3 = 8.2666...
        assertEquals(8.2667, calc.calcularMediaExercicios(7.5, 8.2, 9.1), DELTA);
    }

    @Test
    void alunoComExatamenteOitoNaMediaEhDispensado() {
        // ME = 8.0 (limite): dispensado da PF, MF = ME, Aprovado.
        assertFalse(calc.precisaFazerProvaFinal(8.0));
        assertEquals(8.0, calc.calcularMediaFinal(8.0, 0.0), DELTA);
        assertEquals("Aprovado", calc.situacaoFinal(8.0, 0.0));
    }

    @Test
    void alunoComMediaFinalExatamenteCincoEhAprovado() {
        // ME = 4.5, PF = 6.0 -> MF = (2*4.5 + 6.0)/3 = 5.0 (limite de aprovacao).
        assertEquals(5.0, calc.calcularMediaFinal(4.5, 6.0), DELTA);
        assertEquals("Aprovado", calc.situacaoFinal(4.5, 6.0));
    }

    @Test
    void alunoComNotasZeroEhReprovado() {
        // Caso extremo: todas as notas zeradas.
        assertEquals(0.0, calc.calcularMediaExercicios(0, 0, 0), DELTA);
        assertTrue(calc.precisaFazerProvaFinal(0.0));
        assertEquals(0.0, calc.calcularMediaFinal(0.0, 0.0), DELTA);
        assertEquals("Reprovado", calc.situacaoFinal(0.0, 0.0));
    }

    @Test
    void provaFinalEhIgnoradaQuandoAlunoEhDispensado() {
        // Aluno dispensado (ME >= 8): qualquer valor de PF deve ser ignorado.
        assertEquals(9.0, calc.calcularMediaFinal(9.0, 1.0), DELTA);
        assertEquals(9.0, calc.calcularMediaFinal(9.0, 10.0), DELTA);
        assertEquals("Aprovado", calc.situacaoFinal(9.5, 0.0));
    }
}
