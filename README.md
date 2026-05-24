# Calculadora de Nota

Projeto exemplo em Java + Maven para demonstrar build e teste automaticos no Jenkins,
integrado com o GitHub (atividade de Gerencia de Configuracao).

## Regra de negocio
- **ME** (Media dos Exercicios) = media aritmetica das 3 parciais.
- Se **ME >= 8**: aprovado direto, dispensado da Prova Final. **MF = ME**.
- Se **ME < 8**: faz a Prova Final (PF). **MF = (2*ME + PF) / 3**.
  - MF >= 5 -> Aprovado
  - MF < 5  -> Reprovado

| ME            | Faz PF? | MF              | Situacao  |
|---------------|---------|-----------------|-----------|
| >= 8          | Nao     | = ME            | Aprovado  |
| < 8           | Sim     | se MF >= 5      | Aprovado  |
| < 8           | Sim     | se MF < 5       | Reprovado |

## Metodos
- `calcularMediaExercicios(p1, p2, p3)` -> ME
- `precisaFazerProvaFinal(me)` -> true/false
- `calcularMediaFinal(me, pf)` -> MF
- `situacaoFinal(me, pf)` -> "Aprovado" / "Reprovado"

## Como rodar localmente
```
mvn clean test
```
Relatorio de cobertura (JaCoCo): `target/site/jacoco/index.html`

## Estrutura
```
calculadora-nota/
├── pom.xml
├── src/main/java/com/thiago/CalculadoraNota.java
└── src/test/java/com/thiago/CalculadoraNotaTest.java
```
