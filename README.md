# Plataforma de Apostas Esportivas — Spring Boot 3 CLI (Console)

Aplicação desenvolvida com **Spring Boot 3**, **Spring Data JPA**, **PostgreSQL** e **Lombok**, operando **100% em modo Console / Terminal (CLI)**, sem servidor web HTTP ou endpoints REST.

---

## 🚀 Como Executar

### Pré-requisitos
* **Java 17+** instalado.
* **PostgreSQL** em execução na porta `5432` com a base `apostas_esportivas`:
  * Para restaurar a base pronta, utilize o arquivo `backup_apostas_esportivas.sql`:
    ```bash
    psql -U postgres -d apostas_esportivas -f backup_apostas_esportivas.sql
    ```
    *(ou abra o arquivo `backup_apostas_esportivas.sql` e execute no Query Tool do pgAdmin / DBeaver)*
  * **Ajuste de Credenciais**: caso sua senha do PostgreSQL seja diferente de `1234`, basta ajustar a linha `spring.datasource.password=1234` no arquivo:
    `src/main/resources/application.properties`

### Como Executar:
Execute no terminal da pasta raiz:
```bash
mvn spring-boot:run
```

Ou abra o projeto diretamente em qualquer IDE (IntelliJ IDEA, VS Code ou Eclipse) e execute a classe:
`br.com.apostas.api.ProjetoApi1Application`

---

## ️ Menu Interativo do Terminal (CLI)

Ao iniciar, a aplicação exibe o menu principal no console:

```
==================================================
   SISTEMA DE APOSTAS ESPORTIVAS (SPRING BOOT CLI)
==================================================
 1. Gerenciar Usuários
 2. Gerenciar Esportes
 3. Gerenciar Competições
 4. Gerenciar Competidores
 5. Gerenciar Eventos Esportivos
 6. Gerenciar Mercados de Aposta
 7. Gerenciar Opções de Aposta (Cotações)
 8. Gerenciar Apostas & Liquidação
 9. Gerenciar Participações de Competidores (N:N)
10. Efetuar Nova Aposta
11. Relatórios e Estatísticas Analíticas (5 Relatórios)
 0. Sair
==================================================
```

---

## 🏛️ Arquitetura das Camadas

* **`br.com.apostas.api.model`**: 11 Entidades JPA com anotações de integridade relacional (`@Entity`, `@Table`, `@ManyToOne`, `@EmbeddedId`).
* **`br.com.apostas.api.repository`**: 11 Interfaces Spring Data JPA (`JpaRepository`) + consultas SQL nativas para relatórios.
* **`br.com.apostas.api.service`**: Regras de negócio, cálculo de cotação combinada ($V \times \prod \text{odds}$), congelamento de odds e controle transacional atômico (`@Transactional`).
* **`br.com.apostas.api.cli`**: 12 Menus interativos no terminal com leitura de dados, tratamento de erros e formatação em tabelas ASCII.
