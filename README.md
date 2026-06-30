# Calculadora de Nota

Projeto exemplo em Java + Maven usado para demonstrar build e teste automáticos no Jenkins,
agora orquestrados em **containers Docker isolados** (atividade de Gerência de Configuração).

O Jenkins lê a definição da pipeline no `Jenkinsfile` versionado neste repositório
e dispara dois containers em sequência: um para compilar e empacotar o JAR
(`Dockerfile.build`) e outro para executar os testes (`Dockerfile.test`).

## Regra de negócio

- **ME** (Média dos Exercícios) = média aritmética das 3 parciais.
- Se **ME ≥ 8**: aprovado direto, dispensado da Prova Final. **MF = ME**.
- Se **ME < 8**: faz a Prova Final (PF). **MF = (2·ME + PF) / 3**.
  - MF ≥ 5 → Aprovado
  - MF < 5 → Reprovado

| ME    | Faz PF? | MF              | Situação  |
|-------|---------|-----------------|-----------|
| ≥ 8   | Não     | = ME            | Aprovado  |
| < 8   | Sim     | se MF ≥ 5       | Aprovado  |
| < 8   | Sim     | se MF < 5       | Reprovado |

## Métodos da classe `CalculadoraNota`

- `calcularMediaExercicios(p1, p2, p3)` — calcula a ME.
- `precisaFazerProvaFinal(me)` — `true` se o aluno precisa fazer a PF.
- `calcularMediaFinal(me, pf)` — devolve a ME quando o aluno é dispensado;
  caso contrário, aplica `(2·ME + PF) / 3`.
- `situacaoFinal(me, pf)` — devolve `"Aprovado"` ou `"Reprovado"`.

## Casos de teste

A classe `CalculadoraNotaTest` traz 5 casos focados em limites, decimais e invariantes:

| Caso de teste                                          | Foco                                  |
|--------------------------------------------------------|---------------------------------------|
| `mediaExerciciosCalculaCorretamenteComNotasDecimais`   | Notas decimais (7.5, 8.2, 9.1)        |
| `alunoComExatamenteOitoNaMediaEhDispensado`            | Limite ME = 8 (dispensa exata)        |
| `alunoComMediaFinalExatamenteCincoEhAprovado`          | Limite MF = 5 (aprovação exata)       |
| `alunoComNotasZeroEhReprovado`                         | Caso extremo (todas as notas zeradas) |
| `provaFinalEhIgnoradaQuandoAlunoEhDispensado`          | PF não influencia se ME ≥ 8           |

## Pipeline (Jenkinsfile)

A pipeline tem dois estágios, cada um rodando em seu container Docker:

1. **Build (container)** — a partir do `Dockerfile.build`, sobe um container com Maven + JDK 17
   e executa `mvn clean package -DskipTests`. O JAR vai para `target/`, compartilhado via volume.
2. **Test (container)** — a partir do `Dockerfile.test`, sobe outro container que executa
   `mvn test`. Os relatórios JUnit caem em `target/surefire-reports/` e são publicados
   pelo Jenkins (passo `post { always { junit ... } }`).

O `Jenkinsfile` também traz o gatilho `cron('H 2 * * *')` para execução agendada (nightly),
o que dispensa configurar agendamento na interface do Jenkins.

## Estrutura do projeto

```
calculodenota/
├── pom.xml
├── Dockerfile.build         <- imagem do estágio de build
├── Dockerfile.test          <- imagem do estágio de teste
├── Jenkinsfile              <- pipeline com 2 estágios em containers
├── README.md
└── src/
    ├── main/java/com/atividade/CalculadoraNota.java
    └── test/java/com/atividade/CalculadoraNotaTest.java
```

## Como rodar localmente

**Com Maven instalado** (modo desenvolvimento):

```
mvn clean test
```

**Com Docker** (mesma sequência que o Jenkins executa):

```
docker build -f Dockerfile.build -t calc-build .
docker run --rm -v "%cd%":/app calc-build

docker build -f Dockerfile.test -t calc-test .
docker run --rm -v "%cd%":/app calc-test
```

> No Windows PowerShell, troque `%cd%` por `${PWD}`. No Linux/macOS, use `$(pwd)`.

Os relatórios JUnit ficam em `target/surefire-reports/` após o container de teste rodar.

## Histórico da atividade

- **Parte 1** — Jenkins Freestyle chamando Maven diretamente na máquina (build + teste + cobertura JaCoCo).
- **Parte 2** — Jenkins Pipeline orquestrando dois containers Docker isolados (build em um, teste em outro).
