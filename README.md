# Biblioteca CI — Trabalho 5

**Disciplina:** Cloud Computing e DevOps Avançado  
**Tema:** Pipeline de integração contínua mínimo e reproduzível

![Java 17](https://img.shields.io/badge/Java-17-informational)
![Maven](https://img.shields.io/badge/Maven-build-informational)
![JUnit 5](https://img.shields.io/badge/JUnit-5-informational)
![GitHub Actions](https://img.shields.io/badge/GitHub_Actions-CI-informational)

## Sobre o projeto

O projeto implementa um fluxo de **integração contínua (CI)** para uma aplicação Java de biblioteca. Cada alteração enviada às branches monitoradas passa por compilação, testes automatizados e geração de um artefato rastreável.

A aplicação representa um acervo em memória, com consulta à disponibilidade, empréstimo e devolução de livros. As regras de negócio impedem, por exemplo, o empréstimo duplicado e a devolução de um livro que não está emprestado.

O objetivo é demonstrar um processo automatizado, verificável e reproduzível. Não há dependência de banco de dados, infraestrutura de nuvem ou credenciais externas.

## Tecnologias

| Tecnologia | Aplicação |
|---|---|
| Java 17 | Implementação da aplicação |
| Apache Maven | Compilação, testes e empacotamento |
| JUnit Jupiter 5 | Testes unitários automatizados |
| GitHub Actions | Execução do pipeline de CI |
| Git/GitHub | Versionamento, gatilhos e rastreabilidade |

## Organização do repositório

```text
.github/
  workflows/
    ci.yml
src/
  main/java/br/edu/unialfa/biblioteca/
    BibliotecaService.java
    Livro.java
    Main.java
  test/java/br/edu/unialfa/biblioteca/
    BibliotecaServiceTest.java
    LivroTest.java
docs/
  ARQUITETURA_PIPELINE.md
  ATIVIDADE_PRATICA.md
  EVIDENCIAS.md
exemplos/
  FalhaPropositalTest.java.txt
pom.xml
```

O teste demonstrativo em `exemplos/` possui extensão `.txt` e, por isso, **não participa da execução regular dos testes**. Ele é utilizado exclusivamente para verificar o bloqueio de uma alteração inválida na branch `evidencia-falha`.

## Execução local

**Requisitos:** JDK 17 ou superior e Apache Maven 3.9 ou superior.

```bash
mvn clean test
mvn clean package
java -jar target/biblioteca-ci-1.0.0.jar
```

Na demonstração, o acervo começa com três livros disponíveis, passa a ter dois após o empréstimo de `L001` e retorna a três após sua devolução.

## Pipeline de integração contínua

O workflow [`.github/workflows/ci.yml`](.github/workflows/ci.yml) é acionado nas seguintes situações:

- `push` na branch `main` ou `evidencia-falha`;
- abertura ou atualização de um pull request para `main`;
- execução manual autorizada pelo GitHub Actions.

O processo verifica a estrutura do projeto, prepara o JDK 17, compila o código, executa os testes JUnit, empacota e executa o JAR e registra informações de rastreabilidade.

O empacotamento e a publicação do JAR dependem da aprovação das etapas anteriores. Se a compilação ou algum teste falhar, o pipeline registra a falha e não publica o artefato executável. Relatórios de testes são preservados quando disponíveis, inclusive nas execuções com erro.

### Artefatos e relatórios

| Saída | Identificação | Finalidade |
|---|---|---|
| JAR executável | `biblioteca-ci-1.0.0.jar` | Aplicação compilada |
| Publicação do JAR | `biblioteca-jar-<SHA>` | Associação do artefato ao commit |
| Relatórios JUnit | `relatorios-junit-<RUN_ID>` | Resultado dos testes automatizados |
| Metadados | `build-info.txt` | Repositório, commit, referência e execução |
| Integridade | `checksum-sha256.txt` | Hash SHA-256 do JAR |

Os artefatos são gerados no GitHub Actions e vinculados às execuções correspondentes. O projeto não realiza deploy automático em produção, pois essa etapa não integra o escopo mínimo do T5.

## Segurança

O workflow utiliza permissão mínima de leitura (`contents: read`) e não depende de tokens pessoais ou senhas. A configuração permanece versionada; informações sensíveis não devem fazer parte do código-fonte. O `.gitignore` exclui arquivos locais comuns que possam conter credenciais.

## Documentação técnica

- [Arquitetura do pipeline, etapas, critérios de falha e riscos](docs/ARQUITETURA_PIPELINE.md)
- [Modelagem da atividade prática da Aula 09](docs/ATIVIDADE_PRATICA.md)
- [Evidências geradas e procedimentos de verificação](docs/EVIDENCIAS.md)
