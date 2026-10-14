# Evidências de execução — Pipeline CI da Biblioteca

**Disciplina:** Cloud Computing e DevOps Avançado  
**Trabalho:** T5 — Pipeline de integração contínua mínimo e reproduzível  
**Repositório:** [ArthurSGO/biblioteca-ci-devops](https://github.com/ArthurSGO/biblioteca-ci-devops)  
**Ferramentas:** Java 17, Maven, JUnit 5 e GitHub Actions

## 1. Visão geral

O pipeline definido em [`.github/workflows/ci.yml`](../.github/workflows/ci.yml) automatiza a obtenção do código, a configuração do ambiente Java, a compilação, os testes unitários, o empacotamento e a publicação de artefatos. Cada execução está vinculada a uma branch e a um commit identificável.

Foram realizadas duas execuções em **14 de outubro de 2026**: uma execução aprovada na branch `main` e uma execução com falha intencional na branch `evidencia-falha`. A comparação demonstra que o processo publica o JAR quando os testes passam e interrompe a entrega quando um teste falha.

| Verificação | `main` | `evidencia-falha` |
|---|---|---|
| Resultado do workflow | **Sucesso** | **Falha intencional** |
| Compilação | Aprovada | Aprovada |
| Testes JUnit | **15 aprovados**, nenhuma falha | **16 executados**, uma falha intencional |
| Empacotamento do JAR | Executado | Bloqueado |
| Publicação do JAR | Realizada | Não realizada |
| Relatórios de testes | Publicados | Publicados |

## 2. Execução aprovada — branch `main`

- **Execução:** [T5 - CI da Biblioteca #1](https://github.com/ArthurSGO/biblioteca-ci-devops/actions/runs/38618406899)
- **Commit:** [`f38590df674ddd5c948bb25a66dfe66a69daf34f`](https://github.com/ArthurSGO/biblioteca-ci-devops/commit/f38590df674ddd5c948bb25a66dfe66a69daf34f)
- **Resultado:** `success`
- **Testes:** 15 executados, 0 falhas, 0 erros e 0 ignorados.

O GitHub Actions concluiu todas as etapas obrigatórias: checkout, preparação do JDK 17, validação dos arquivos, compilação, testes JUnit, geração e execução do JAR, identificação do commit, cálculo do checksum SHA-256 e publicação dos artefatos.

**Artefatos publicados:**

| Artefato | Conteúdo e finalidade |
|---|---|
| [JAR e dados de rastreabilidade](https://github.com/ArthurSGO/biblioteca-ci-devops/actions/runs/38618406899/artifacts/11920034187) | `biblioteca-ci-1.0.0.jar`, `build-info.txt` e `checksum-sha256.txt` |
| [Relatórios JUnit](https://github.com/ArthurSGO/biblioteca-ci-devops/actions/runs/38618406899/artifacts/11919939225) | Relatórios Maven Surefire dos 15 testes aprovados |

O artefato do aplicativo foi identificado no Actions como `biblioteca-jar-f38590df674ddd5c948bb25a66dfe66a69daf34f`, vinculando o resultado ao código-fonte utilizado na compilação.

## 3. Execução com falha detectada — branch `evidencia-falha`

- **Execução:** [T5 - CI da Biblioteca #2](https://github.com/ArthurSGO/biblioteca-ci-devops/actions/runs/38618577583)
- **Commit:** [`b9d14e5675ff76549c7834cdebd7ca38c9d6b127`](https://github.com/ArthurSGO/biblioteca-ci-devops/commit/b9d14e5675ff76549c7834cdebd7ca38c9d6b127)
- **Resultado:** `failure` — falha provocada exclusivamente para validar o bloqueio do pipeline.
- **Testes:** 16 executados, 15 aprovados, 1 falha, 0 erros e 0 ignorados.

A branch contém o teste `FalhaPropositalTest.deveDetectarFalhaProposital()`, cuja asserção compara intencionalmente os valores `2` e `1`. O Maven identificou a divergência e registrou a falha no JUnit.

O comportamento observado confirma o tratamento correto do erro:

1. A compilação foi concluída normalmente.
2. A etapa **5. Executar os testes JUnit** falhou, com a identificação do teste reprovado no log.
3. As etapas de empacotamento, execução do JAR, geração de metadados e publicação do aplicativo foram ignoradas.
4. A etapa de publicação dos relatórios JUnit foi executada mesmo após a falha, preservando a evidência para análise.
5. A branch `main` permaneceu com sua execução aprovada, sem receber o teste propositalmente incorreto.

**Artefato de diagnóstico:** [Relatórios JUnit da execução com falha](https://github.com/ArthurSGO/biblioteca-ci-devops/actions/runs/38618577583/artifacts/11919864419).

A ausência de um artefato JAR nessa execução confirma que o pipeline **não publica a aplicação quando um teste obrigatório falha**.

## 4. Reprodução e investigação

O projeto utiliza dependências e plugins declarados no `pom.xml`. Com **JDK 17 ou superior** e **Maven 3.9 ou superior**, a verificação da versão principal pode ser reproduzida a partir do repositório:

```bash
git clone https://github.com/ArthurSGO/biblioteca-ci-devops.git
cd biblioteca-ci-devops
mvn clean test
mvn clean package
java -jar target/biblioteca-ci-1.0.0.jar
```

Para reproduzir a detecção da falha intencional, utilize a branch correspondente:

```bash
git switch evidencia-falha
mvn clean test
```

Nesse segundo cenário, o comando de testes deve terminar com erro devido à asserção proposital. O resultado pode ser investigado pelo terminal, pelos logs do GitHub Actions ou pelos arquivos em `target/surefire-reports/`.

O diagnóstico deve começar pela primeira etapa reprovada e pela mensagem do teste ou do compilador. Uma nova execução após a correção permite verificar se a alteração voltou a atender aos critérios de qualidade.

## 5. Segurança e rastreabilidade

- O workflow define a permissão mínima `contents: read` para o token automático do GitHub.
- A aplicação não depende de credenciais, serviços externos ou variáveis secretas.
- O nome do artefato aprovado inclui o SHA do commit que originou a execução.
- O arquivo `build-info.txt` registra repositório, commit, referência Git e endereço da execução.
- O arquivo `checksum-sha256.txt` permite conferir a integridade do JAR gerado.
- A etapa de publicação dos relatórios utiliza `always()`, permitindo investigar inclusive execuções reprovadas.

**Disponibilidade dos artefatos:** os arquivos foram configurados com retenção de 30 dias e têm expiração indicada pelo GitHub para **13 de novembro de 2026**. O histórico e os logs das execuções podem ser consultados pelos links acima; os downloads dos artefatos dependem do respectivo prazo de retenção.

## 6. Conclusão

As execuções registradas comprovam o atendimento ao escopo do Trabalho 5: pipeline executável a partir do repositório, build e testes automatizados, bloqueio em caso de falha, artefato identificável, evidências de execução e rastreabilidade até o commit. A atividade demonstra integração contínua; **deploy automático em produção não faz parte deste escopo**.
