# Projeto - Fase 1 — Sistema de Apostas Esportivas

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

---

## Menu Interativo do Terminal (CLI)

Ao iniciar, a aplicacao exibe o menu principal no console:

```
================================================================================
   SISTEMA DE APOSTAS ESPORTIVAS - CONSOLE CLI (SPRING BOOT 3 + DATA JPA)       
================================================================================

+----------------------------------------------------------------+
|        SISTEMA DE APOSTAS ESPORTIVAS - MENU PRINCIPAL          |
+----------------------------------------------------------------+
|  ENTIDADES (CRUD)                                              |
|  1 - Gerenciar Usuarios                                        |
|  2 - Gerenciar Esportes                                        |
|  3 - Gerenciar Competicoes                                     |
|  4 - Gerenciar Competidores                                    |
|  5 - Gerenciar Eventos Esportivos                              |
|  6 - Gerenciar Mercados                                        |
|  7 - Gerenciar Opcoes de Aposta                                |
|  8 - Gerenciar Apostas                                         |
+----------------------------------------------------------------+
|  PROCESSOS DE NEGOCIO & RELATORIOS                             |
|  9 - Registrar Participacao em Evento                          |
| 10 - Efetuar / Liquidar Aposta                                 |
| 11 - Relatorios Analiticos                                     |
+----------------------------------------------------------------+
|  0 - Sair do Sistema                                           |
+----------------------------------------------------------------+
```
