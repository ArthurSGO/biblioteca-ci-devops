# Atividade prática — Modelagem do pipeline

**Disciplina:** Cloud Computing e DevOps Avançado  
**Aplicação:** Biblioteca CI (Java 17, Maven, JUnit 5)

## Cenário e proposta

A execução manual de builds dificulta a identificação antecipada de erros, favorece diferenças de configuração entre máquinas e fragmenta o registro dos resultados de teste. A proposta utiliza o GitHub Actions para automatizar a integração do código e produzir evidências verificáveis a cada mudança.

## Fluxo modelado

1. **Gatilho:** um push, pull request ou acionamento manual inicia a execução.
2. **Checkout:** o workflow recupera o commit correspondente ao evento.
3. **Preparação:** o ambiente recebe o JDK 17 e o cache de dependências Maven.
4. **Validação:** a existência de arquivos essenciais do projeto é verificada.
5. **Build:** o Maven compila o código Java.
6. **Testes:** os testes JUnit verificam as regras de empréstimo, devolução e validação de dados.
7. **Empacotamento:** a aplicação aprovada é empacotada em um JAR executável.
8. **Verificação de execução:** o JAR é executado para confirmar o funcionamento básico.
9. **Rastreabilidade:** são registrados SHA do commit, dados da execução e checksum SHA-256.
10. **Publicação:** JAR, metadados e relatórios ficam disponíveis como artefatos do GitHub Actions.

O [diagrama e a matriz de etapas](ARQUITETURA_PIPELINE.md) descrevem entradas, ações, saídas, condições de interrupção, evidências e responsabilidades.

## Riscos identificados e mitigações

| Risco | Mitigação adotada |
|---|---|
| Testes insuficientes | Testes unitários para operações válidas, duplicidade e estados inválidos |
| Falha ao obter dependências | Versões explícitas no `pom.xml` e cache de dependências Maven |
| Exposição de credenciais | Workflow sem segredos, permissão mínima e regras de exclusão no Git |
| Artefato sem identificação | Nome associado ao SHA do commit, metadados e checksum SHA-256 |

## Síntese para apresentação

O fluxo começa quando uma alteração é enviada ao GitHub. A automação identifica o commit, prepara o Java 17 e executa o Maven. Antes de gerar qualquer pacote, os testes JUnit verificam o comportamento da biblioteca. Caso exista uma falha, a execução termina sem publicar o JAR, e o log informa o motivo. Uma branch específica permite demonstrar esse bloqueio sem comprometer a versão principal.

Quando todas as verificações passam, o workflow executa a aplicação, calcula seu checksum e disponibiliza o JAR com informações que ligam o resultado ao commit. Essa combinação de automação, tratamento de falhas, evidências e rastreabilidade torna o processo de entrega repetível e auditável.
