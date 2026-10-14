# Evidências e verificação do pipeline

**Disciplina:** Cloud Computing e DevOps Avançado  
**Trabalho:** T5 — Pipeline CI mínimo e reproduzível

As evidências de execução são produzidas pelo próprio [workflow de CI](../.github/workflows/ci.yml) e armazenadas no **GitHub Actions**, com identificação da branch, do commit e do resultado de cada execução. Os relatórios e os artefatos podem ser conferidos diretamente no histórico de execuções do repositório.

## Execução bem-sucedida

**Branch:** `main`.

Uma execução aprovada demonstra:

- Checkout do commit e configuração do ambiente Java 17.
- Compilação e testes automatizados sem erros.
- Geração e execução do arquivo `biblioteca-ci-1.0.0.jar`.
- Publicação do artefato `biblioteca-jar-<SHA>`.
- Disponibilização de `build-info.txt`, `checksum-sha256.txt` e relatórios Surefire.

O resultado pode ser verificado no status do job, nos logs das etapas e nos artefatos associados à execução.

## Execução com falha detectada

**Branch:** `evidencia-falha`.

O teste `FalhaPropositalTest.deveDetectarFalhaProposital()` utiliza uma asserção intencionalmente incorreta (`assertEquals(2, 1)`) para demonstrar o comportamento do pipeline diante de um teste reprovado.

A verificação esperada é:

- Falha na etapa **Executar os testes JUnit**.
- Identificação do teste reprovado no log e no relatório Surefire.
- Interrupção das etapas de empacotamento e publicação do JAR.
- Preservação dos relatórios de testes quando gerados.
- Nenhuma alteração necessária na branch principal `main`.

A execução com falha é intencional e representa a validação do critério de bloqueio do pipeline, não um defeito na versão principal.

## Investigação e reprodução

O workflow também pode ser executado localmente com os seguintes comandos:

```bash
mvn clean test
mvn clean package
```

Quando algum teste falha, o Maven identifica a classe e o método. Os relatórios detalhados são armazenados em `target/surefire-reports/`. No GitHub Actions, os mesmos resultados aparecem nos logs e podem ser recuperados como artefatos da execução.

Para investigar uma falha, a equipe analisa a primeira etapa reprovada, consulta a mensagem da asserção ou do compilador, corrige a causa e executa novamente o pipeline. O JAR só é disponibilizado quando os controles obrigatórios são aprovados.

## Proteção de informações sensíveis

A aplicação não utiliza senhas, tokens ou serviços externos. O workflow declara `contents: read`, restringindo as permissões do token automático do GitHub. Os logs e os artefatos contêm apenas resultados de build, testes e identificação da execução.

**Observação:** as informações acima descrevem as evidências geradas pela configuração do projeto. Os resultados reais de sucesso e falha só poderão ser confirmados no histórico do GitHub Actions após a publicação das duas branches.
