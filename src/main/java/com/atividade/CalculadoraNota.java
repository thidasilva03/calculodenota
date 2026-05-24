package com.atividade;

/**
 * Calcula a situacao final de um aluno em uma disciplina.
 *
 * Regras:
 *  - ME = media aritmetica das 3 parciais (media dos exercicios)
 *  - Se ME >= 8: aprovado direto (dispensado da Prova Final) e MF = ME
 *  - Se ME <  8: faz a Prova Final (PF) e MF = (2*ME + PF) / 3
 *      - MF >= 5 -> Aprovado
 *      - MF <  5 -> Reprovado
 */
public class CalculadoraNota {

    private static final double MEDIA_DISPENSA_PF = 8.0;
    private static final double MEDIA_APROVACAO_FINAL = 5.0;

    /**
     * Calcula a Media dos Exercicios (ME) como media aritmetica simples
     * das tres parciais avaliativas.
     */
    public double calcularMediaExercicios(double p1, double p2, double p3) {
        return (p1 + p2 + p3) / 3;
    }

    /**
     * Indica se o aluno precisa fazer a Prova Final.
     * Precisa quando a ME for menor que 8.
     */
    public boolean precisaFazerProvaFinal(double me) {
        return me < MEDIA_DISPENSA_PF;
    }

    /**
     * Calcula a Media Final (MF).
     * Se o aluno foi dispensado (ME >= 8), a MF e a propria ME.
     * Caso contrario, MF = (2*ME + PF) / 3.
     */
    public double calcularMediaFinal(double me, double pf) {
        double mf;

        if(me >= MEDIA_DISPENSA_PF){
            mf = me;
        } else{
            mf = (2 * me + pf) / 3;
        }
        
        return mf;
    }

    /**
     * Retorna a Situacao Final (ST): "Aprovado" ou "Reprovado".
     * O valor de pf e ignorado quando o aluno e dispensado da PF.
     */
    public String situacaoFinal(double me, double pf) {
        String situacao = "Reprovado";

        if (me >= MEDIA_DISPENSA_PF) {
            situacao = "Aprovado";
        }

        double mf = calcularMediaFinal(me, pf);
        if (mf >= MEDIA_APROVACAO_FINAL) {
            situacao = "Aprovado";
        }
        return situacao;
    }
}
