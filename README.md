# Projeto - Fase 1 — Banco de Dados

Aplicacao desenvolvida com **Spring Boot 3**, **Spring Data JPA**, **PostgreSQL** e **Lombok**, operando em modo Console / Terminal (CLI).

---

## Como Executar

### 1. Pre-requisitos
* **Java 17+** instalado.
* **PostgreSQL** em execucao na porta `5432`.

### 2. Clonar o Repositorio
```bash
git clone https://github.com/jhonatan299/projeto_fase_1_ban_2.git
cd projeto_fase_1_ban_2
```

### 3. Configurar o Banco de Dados
1. Crie a base de dados no PostgreSQL:
   ```bash
   createdb -U postgres apostas_esportivas
   ```
   *(ou execute `CREATE DATABASE apostas_esportivas;` no pgAdmin ou DBeaver)*

2. Restaure o backup com todas as tabelas e dados:
   ```bash
   psql -U postgres -d apostas_esportivas -f backup_apostas_esportivas.sql
   ```

3. *(Opcional)* Se a sua senha do PostgreSQL for diferente de `1234`, ajuste no arquivo:  
   `src/main/resources/application.properties`

### 4. Executar a Aplicacao
* **Via IDE (Recomendado):** Abra o projeto no **VS Code**, **IntelliJ** ou **Eclipse** e execute a classe:  
  `src/main/java/br/com/apostas/api/ProjetoApi1Application.java`
* **Via Terminal:**
  ```bash
  mvn spring-boot:run
  ```
