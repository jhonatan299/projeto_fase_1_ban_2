# Projeto - Fase 1 — Banco de Dados

Aplicacao desenvolvida com **Spring Boot 3**, **Spring Data JPA**, **PostgreSQL** e **Lombok**, operando **100% em modo Console / Terminal (CLI)**, sem servidor web HTTP ou endpoints REST.

---

## Como Executar

### Pre-requisitos
* **Java 17+** instalado.
* **PostgreSQL** em execucao na porta `5432` com a base `apostas_esportivas`:
  * Para restaurar a base pronta, utilize o arquivo `backup_apostas_esportivas.sql`:
    ```bash
    psql -U postgres -d apostas_esportivas -f backup_apostas_esportivas.sql
    ```
    *(ou abra o arquivo `backup_apostas_esportivas.sql` e execute no Query Tool do pgAdmin / DBeaver)*
  * **Ajuste de Credenciais**: caso sua senha do PostgreSQL seja diferente de `1234`, basta ajustar a linha `spring.datasource.password=1234` no arquivo:
    `src/main/resources/application.properties`

### Execucao
Execute no terminal da pasta raiz:
```bash
mvn spring-boot:run
```

Ou abra o projeto diretamente em qualquer IDE (IntelliJ IDEA, VS Code ou Eclipse) e execute a classe:
`br.com.apostas.api.ProjetoApi1Application`
