# Dicionario de Dados — Sistema de Apostas Esportivas

## 1. Tabela: `esporte`
Representa as modalidades esportivas cadastradas no sistema (ex: Futebol, Basquete, Formula 1, Rugby).

| Coluna | Tipo de Dado | Restricoes | Descricao |
| :--- | :--- | :--- | :--- |
| `codesporte` | SERIAL | PK, NOT NULL | Identificador unico do esporte (auto-incremento). |
| `nome` | VARCHAR(100) | NOT NULL, UNIQUE | Nome da modalidade esportiva. |
| `max_competidores_evento` | INTEGER | NOT NULL, DEFAULT 2, CHECK (>= 2) | Limite maximo de competidores permitidos em um unico evento daquele esporte (ex: 2 para Futebol/MMA, 20 para F1, 15 para Rugby). |

---

## 2. Tabela: `competicao`
Representa torneios, campeonatos ou ligas de um determinado esporte.

| Coluna | Tipo de Dado | Restricoes | Descricao |
| :--- | :--- | :--- | :--- |
| `codcompeticao` | SERIAL | PK, NOT NULL | Identificador unico da competicao. |
| `nome` | VARCHAR(150) | NOT NULL | Nome da competicao (ex: Brasileirao Serie A). |
| `pais` | VARCHAR(60) | NULL | Pais ou regiao de realizacao da competicao. |
| `data_inicio` | DATE | NOT NULL | Data de abertura do campeonato. |
| `data_fim` | DATE | NULL | Data de encerramento do campeonato. |
| `codesporte` | INTEGER | FK (esporte), NOT NULL | Esporte ao qual a competicao pertence. |

---

## 3. Tabela: `competidor`
Representa times, clubes, selecoes ou atletas individuais.

| Coluna | Tipo de Dado | Restricoes | Descricao |
| :--- | :--- | :--- | :--- |
| `codcompetidor` | SERIAL | PK, NOT NULL | Identificador unico do competidor. |
| `nome` | VARCHAR(150) | NOT NULL | Nome do time ou atleta. |
| `tipo` | VARCHAR(20) | NOT NULL | Tipo do competidor: 'TIME' ou 'INDIVIDUAL'. |

---

## 4. Tabela: `evento_esportivo`
Representa uma partida, jogo, corrida ou confronto especifico.

| Coluna | Tipo de Dado | Restricoes | Descricao |
| :--- | :--- | :--- | :--- |
| `codevento` | SERIAL | PK, NOT NULL | Identificador unico do evento esportivo. |
| `descricao` | VARCHAR(200) | NULL | Descricao/rotulo da partida (ex: Flamengo x Palmeiras). |
| `data_hora` | TIMESTAMP | NOT NULL | Data e hora agendada para o evento. |
| `status` | VARCHAR(20) | NOT NULL | Status: 'AGENDADO', 'AO_VIVO', 'FINALIZADO', 'CANCELADO'. |
| `codcompeticao` | INTEGER | FK (competicao), NOT NULL | Competicao a que o evento pertence. |

---

## 5. Tabela: `participacao` (N:M)
Tabela associativa que vincula competidores a um evento esportivo especifico.

| Coluna | Tipo de Dado | Restricoes | Descricao |
| :--- | :--- | :--- | :--- |
| `codevento` | INTEGER | PK, FK (evento_esportivo), NOT NULL | Evento esportivo da participacao. |
| `codcompetidor` | INTEGER | PK, FK (competidor), NOT NULL | Competidor participante do evento. |

*Regra de Negocio*: A quantidade de registros na tabela `participacao` para um mesmo `codevento` nao pode exceder o valor de `esporte.max_competidores_evento` do esporte correspondente.

---

## 6. Tabela: `mercado`
Representa um tipo de aposta disponivel dentro de um evento.

| Coluna | Tipo de Dado | Restricoes | Descricao |
| :--- | :--- | :--- | :--- |
| `codmercado` | SERIAL | PK, NOT NULL | Identificador unico do mercado. |
| `tipo` | VARCHAR(100) | NOT NULL | Tipo do mercado (ex: 'Vencedor do Jogo (1X2)', 'Total de Gols'). |
| `status` | VARCHAR(20) | NOT NULL | Status operacional: 'ABERTO', 'SUSPENSO', 'FECHADO'. |
| `codevento` | INTEGER | FK (evento_esportivo), NOT NULL | Evento esportivo ao qual o mercado pertence. |

---

## 7. Tabela: `opcao_aposta`
Representa cada selecao/palpite possivel dentro de um mercado com sua respectiva cotação (odd).

| Coluna | Tipo de Dado | Restricoes | Descricao |
| :--- | :--- | :--- | :--- |
| `codopcao` | SERIAL | PK, NOT NULL | Identificador unico da opcao. |
| `resultado` | VARCHAR(100) | NOT NULL | Descricao do palpite (ex: 'Flamengo Vence', 'Empate'). |
| `odd` | NUMERIC(6,2) | NOT NULL, CHECK (> 1.00) | Multiplicador/Cotacao de retorno do palpite. |
| `descricao` | VARCHAR(200) | NULL | Detalhes adicionais do palpite. |
| `codmercado` | INTEGER | FK (mercado), NOT NULL | Mercado ao qual esta opcao pertence. |

---

## 8. Tabela: `usuario`
Representa os clientes apostadores cadastrados no sistema.

| Coluna | Tipo de Dado | Restricoes | Descricao |
| :--- | :--- | :--- | :--- |
| `codusuario` | SERIAL | PK, NOT NULL | Identificador unico do usuario. |
| `nome` | VARCHAR(150) | NOT NULL | Nome completo do apostador. |
| `cpf` | CHAR(11) | NOT NULL, UNIQUE | Cadastro de Pessoa Fisica (11 digitos numericos). |
| `email` | VARCHAR(150) | NOT NULL, UNIQUE | Endereco de e-mail institucional/pessoal. |
| `senha` | VARCHAR(255) | NOT NULL | Senha ou hash de autenticacao. |
| `data_cadastro` | TIMESTAMP | NOT NULL, DEFAULT NOW() | Data e hora de criacao da conta. |
| `status` | VARCHAR(20) | NOT NULL, DEFAULT 'ATIVO' | Estado da conta: 'ATIVO', 'BLOQUEADO', 'INATIVO'. |

---

## 9. Tabela: `aposta`
Representa o bilhete de aposta mestre gerado pelo usuario.

| Coluna | Tipo de Dado | Restricoes | Descricao |
| :--- | :--- | :--- | :--- |
| `codaposta` | SERIAL | PK, NOT NULL | Identificador unico do bilhete. |
| `data_hora` | TIMESTAMP | NOT NULL, DEFAULT NOW() | Momento da efetivacao da aposta. |
| `status` | VARCHAR(20) | NOT NULL, DEFAULT 'PENDENTE' | Status do bilhete: 'PENDENTE', 'GANHA', 'PERDIDA', 'CANCELADA'. |
| `valor` | NUMERIC(10,2) | NOT NULL, CHECK (> 0) | Valor apostado em dinheiro (R$). |
| `valor_retorno` | NUMERIC(10,2) | NULL | Valor final pago ao apostador apos liquidacao. |
| `codusuario` | INTEGER | FK (usuario), NOT NULL | Usuario apostador titular do bilhete. |

---

## 10. Tabela: `item_aposta`
Representa cada selecao/palpite incluido dentro de um bilhete de aposta.

| Coluna | Tipo de Dado | Restricoes | Descricao |
| :--- | :--- | :--- | :--- |
| `coditem` | SERIAL | PK, NOT NULL | Identificador unico do item. |
| `oddcadastrada` | NUMERIC(6,2) | NOT NULL | Odd congelada no instante exato da confirmacao do bilhete. |
| `resultado` | VARCHAR(20) | NOT NULL, DEFAULT 'PENDENTE' | Status do palpite: 'PENDENTE', 'GANHA', 'PERDIDA', 'CANCELADA'. |
| `codaposta` | INTEGER | FK (aposta), NOT NULL | Bilhete pai ao qual o item pertence. |
| `codopcao` | INTEGER | FK (opcao_aposta), NOT NULL | Opcao de aposta selecionada. |
