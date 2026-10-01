--
-- PostgreSQL database dump
--

\restrict lrrWUnmL2ydiSdeNfmAJ208f4sa2uHZsgxztlMzn81qdXpxSTBhaUaeEKM78FpW

-- Dumped from database version 18.6
-- Dumped by pg_dump version 18.6

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

ALTER TABLE IF EXISTS ONLY public.participacao DROP CONSTRAINT IF EXISTS participacao_codevento_fkey;
ALTER TABLE IF EXISTS ONLY public.participacao DROP CONSTRAINT IF EXISTS participacao_codcompetidor_fkey;
ALTER TABLE IF EXISTS ONLY public.opcao_aposta DROP CONSTRAINT IF EXISTS opcao_aposta_codmercado_fkey;
ALTER TABLE IF EXISTS ONLY public.mercado DROP CONSTRAINT IF EXISTS mercado_codevento_fkey;
ALTER TABLE IF EXISTS ONLY public.item_aposta DROP CONSTRAINT IF EXISTS item_aposta_codopcao_fkey;
ALTER TABLE IF EXISTS ONLY public.item_aposta DROP CONSTRAINT IF EXISTS item_aposta_codaposta_fkey;
ALTER TABLE IF EXISTS ONLY public.evento_esportivo DROP CONSTRAINT IF EXISTS evento_esportivo_codcompeticao_fkey;
ALTER TABLE IF EXISTS ONLY public.competicao DROP CONSTRAINT IF EXISTS competicao_codesporte_fkey;
ALTER TABLE IF EXISTS ONLY public.aposta DROP CONSTRAINT IF EXISTS aposta_codusuario_fkey;
DROP INDEX IF EXISTS public.idx_opcao_mercado;
DROP INDEX IF EXISTS public.idx_mercado_evento;
DROP INDEX IF EXISTS public.idx_item_aposta_opcao;
DROP INDEX IF EXISTS public.idx_item_aposta_aposta;
DROP INDEX IF EXISTS public.idx_evento_competicao;
DROP INDEX IF EXISTS public.idx_competicao_esporte;
DROP INDEX IF EXISTS public.idx_aposta_usuario;
ALTER TABLE IF EXISTS ONLY public.usuario DROP CONSTRAINT IF EXISTS usuario_pkey;
ALTER TABLE IF EXISTS ONLY public.usuario DROP CONSTRAINT IF EXISTS usuario_email_key;
ALTER TABLE IF EXISTS ONLY public.participacao DROP CONSTRAINT IF EXISTS participacao_pkey;
ALTER TABLE IF EXISTS ONLY public.opcao_aposta DROP CONSTRAINT IF EXISTS opcao_aposta_pkey;
ALTER TABLE IF EXISTS ONLY public.mercado DROP CONSTRAINT IF EXISTS mercado_pkey;
ALTER TABLE IF EXISTS ONLY public.item_aposta DROP CONSTRAINT IF EXISTS item_aposta_pkey;
ALTER TABLE IF EXISTS ONLY public.evento_esportivo DROP CONSTRAINT IF EXISTS evento_esportivo_pkey;
ALTER TABLE IF EXISTS ONLY public.esporte DROP CONSTRAINT IF EXISTS esporte_pkey;
ALTER TABLE IF EXISTS ONLY public.esporte DROP CONSTRAINT IF EXISTS esporte_nome_key;
ALTER TABLE IF EXISTS ONLY public.competidor DROP CONSTRAINT IF EXISTS competidor_pkey;
ALTER TABLE IF EXISTS ONLY public.competicao DROP CONSTRAINT IF EXISTS competicao_pkey;
ALTER TABLE IF EXISTS ONLY public.aposta DROP CONSTRAINT IF EXISTS aposta_pkey;
ALTER TABLE IF EXISTS public.usuario ALTER COLUMN codusuario DROP DEFAULT;
ALTER TABLE IF EXISTS public.opcao_aposta ALTER COLUMN codopcao DROP DEFAULT;
ALTER TABLE IF EXISTS public.mercado ALTER COLUMN codmercado DROP DEFAULT;
ALTER TABLE IF EXISTS public.item_aposta ALTER COLUMN coditem DROP DEFAULT;
ALTER TABLE IF EXISTS public.evento_esportivo ALTER COLUMN codevento DROP DEFAULT;
ALTER TABLE IF EXISTS public.esporte ALTER COLUMN codesporte DROP DEFAULT;
ALTER TABLE IF EXISTS public.competidor ALTER COLUMN codcompetidor DROP DEFAULT;
ALTER TABLE IF EXISTS public.competicao ALTER COLUMN codcompeticao DROP DEFAULT;
ALTER TABLE IF EXISTS public.aposta ALTER COLUMN codaposta DROP DEFAULT;
DROP SEQUENCE IF EXISTS public.usuario_codusuario_seq;
DROP TABLE IF EXISTS public.usuario;
DROP TABLE IF EXISTS public.participacao;
DROP SEQUENCE IF EXISTS public.opcao_aposta_codopcao_seq;
DROP TABLE IF EXISTS public.opcao_aposta;
DROP SEQUENCE IF EXISTS public.mercado_codmercado_seq;
DROP TABLE IF EXISTS public.mercado;
DROP SEQUENCE IF EXISTS public.item_aposta_coditem_seq;
DROP TABLE IF EXISTS public.item_aposta;
DROP SEQUENCE IF EXISTS public.evento_esportivo_codevento_seq;
DROP TABLE IF EXISTS public.evento_esportivo;
DROP SEQUENCE IF EXISTS public.esporte_codesporte_seq;
DROP TABLE IF EXISTS public.esporte;
DROP SEQUENCE IF EXISTS public.competidor_codcompetidor_seq;
DROP TABLE IF EXISTS public.competidor;
DROP SEQUENCE IF EXISTS public.competicao_codcompeticao_seq;
DROP TABLE IF EXISTS public.competicao;
DROP SEQUENCE IF EXISTS public.aposta_codaposta_seq;
DROP TABLE IF EXISTS public.aposta;
SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: aposta; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.aposta (
    codaposta integer NOT NULL,
    status character varying(20) DEFAULT 'PENDENTE'::character varying NOT NULL,
    data_hora timestamp without time zone DEFAULT now() NOT NULL,
    valor numeric(10,2) NOT NULL,
    valor_retorno numeric(10,2),
    codusuario integer NOT NULL,
    CONSTRAINT chk_aposta_status CHECK (((status)::text = ANY ((ARRAY['PENDENTE'::character varying, 'GANHA'::character varying, 'PERDIDA'::character varying, 'CANCELADA'::character varying])::text[])))
);


--
-- Name: aposta_codaposta_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.aposta_codaposta_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: aposta_codaposta_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.aposta_codaposta_seq OWNED BY public.aposta.codaposta;


--
-- Name: competicao; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.competicao (
    codcompeticao integer NOT NULL,
    nome character varying(150) NOT NULL,
    pais character varying(60),
    data_inicio date NOT NULL,
    data_fim date,
    codesporte integer NOT NULL
);


--
-- Name: competicao_codcompeticao_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.competicao_codcompeticao_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: competicao_codcompeticao_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.competicao_codcompeticao_seq OWNED BY public.competicao.codcompeticao;


--
-- Name: competidor; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.competidor (
    codcompetidor integer NOT NULL,
    nome character varying(150) NOT NULL,
    tipo character varying(20) NOT NULL
);


--
-- Name: competidor_codcompetidor_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.competidor_codcompetidor_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: competidor_codcompetidor_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.competidor_codcompetidor_seq OWNED BY public.competidor.codcompetidor;


--
-- Name: esporte; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.esporte (
    codesporte integer NOT NULL,
    nome character varying(100) NOT NULL,
    max_competidores_evento integer DEFAULT 2 NOT NULL,
    CONSTRAINT chk_esporte_max_competidores CHECK ((max_competidores_evento >= 2))
);


--
-- Name: esporte_codesporte_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.esporte_codesporte_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: esporte_codesporte_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.esporte_codesporte_seq OWNED BY public.esporte.codesporte;


--
-- Name: evento_esportivo; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.evento_esportivo (
    codevento integer NOT NULL,
    data_hora timestamp without time zone NOT NULL,
    status character varying(20) NOT NULL,
    codcompeticao integer NOT NULL,
    descricao character varying(200)
);


--
-- Name: evento_esportivo_codevento_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.evento_esportivo_codevento_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: evento_esportivo_codevento_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.evento_esportivo_codevento_seq OWNED BY public.evento_esportivo.codevento;


--
-- Name: item_aposta; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.item_aposta (
    coditem integer NOT NULL,
    oddcadastrada numeric(6,2) NOT NULL,
    resultado character varying(20) DEFAULT 'PENDENTE'::character varying NOT NULL,
    codaposta integer NOT NULL,
    codopcao integer NOT NULL,
    CONSTRAINT chk_item_aposta_resultado CHECK (((resultado)::text = ANY ((ARRAY['PENDENTE'::character varying, 'GANHA'::character varying, 'PERDIDA'::character varying, 'CANCELADA'::character varying])::text[])))
);


--
-- Name: item_aposta_coditem_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.item_aposta_coditem_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: item_aposta_coditem_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.item_aposta_coditem_seq OWNED BY public.item_aposta.coditem;


--
-- Name: mercado; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.mercado (
    codmercado integer NOT NULL,
    status character varying(20) NOT NULL,
    tipo character varying(50) NOT NULL,
    codevento integer NOT NULL
);


--
-- Name: mercado_codmercado_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.mercado_codmercado_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: mercado_codmercado_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.mercado_codmercado_seq OWNED BY public.mercado.codmercado;


--
-- Name: opcao_aposta; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.opcao_aposta (
    codopcao integer NOT NULL,
    resultado character varying(100) NOT NULL,
    odd numeric(6,2) NOT NULL,
    descricao character varying(200),
    codmercado integer NOT NULL
);


--
-- Name: opcao_aposta_codopcao_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.opcao_aposta_codopcao_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: opcao_aposta_codopcao_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.opcao_aposta_codopcao_seq OWNED BY public.opcao_aposta.codopcao;


--
-- Name: participacao; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.participacao (
    codevento integer NOT NULL,
    codcompetidor integer NOT NULL
);


--
-- Name: usuario; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.usuario (
    codusuario integer NOT NULL,
    nome character varying(150) NOT NULL,
    email character varying(150) NOT NULL,
    senha character varying(255) NOT NULL,
    data_cadastro timestamp without time zone DEFAULT now() NOT NULL,
    status character varying(20) DEFAULT 'ATIVO'::character varying NOT NULL,
    CONSTRAINT chk_usuario_status CHECK (((status)::text = ANY ((ARRAY['ATIVO'::character varying, 'BLOQUEADO'::character varying, 'INATIVO'::character varying])::text[])))
);


--
-- Name: usuario_codusuario_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.usuario_codusuario_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: usuario_codusuario_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.usuario_codusuario_seq OWNED BY public.usuario.codusuario;


--
-- Name: aposta codaposta; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.aposta ALTER COLUMN codaposta SET DEFAULT nextval('public.aposta_codaposta_seq'::regclass);


--
-- Name: competicao codcompeticao; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.competicao ALTER COLUMN codcompeticao SET DEFAULT nextval('public.competicao_codcompeticao_seq'::regclass);


--
-- Name: competidor codcompetidor; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.competidor ALTER COLUMN codcompetidor SET DEFAULT nextval('public.competidor_codcompetidor_seq'::regclass);


--
-- Name: esporte codesporte; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.esporte ALTER COLUMN codesporte SET DEFAULT nextval('public.esporte_codesporte_seq'::regclass);


--
-- Name: evento_esportivo codevento; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.evento_esportivo ALTER COLUMN codevento SET DEFAULT nextval('public.evento_esportivo_codevento_seq'::regclass);


--
-- Name: item_aposta coditem; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.item_aposta ALTER COLUMN coditem SET DEFAULT nextval('public.item_aposta_coditem_seq'::regclass);


--
-- Name: mercado codmercado; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.mercado ALTER COLUMN codmercado SET DEFAULT nextval('public.mercado_codmercado_seq'::regclass);


--
-- Name: opcao_aposta codopcao; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.opcao_aposta ALTER COLUMN codopcao SET DEFAULT nextval('public.opcao_aposta_codopcao_seq'::regclass);


--
-- Name: usuario codusuario; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.usuario ALTER COLUMN codusuario SET DEFAULT nextval('public.usuario_codusuario_seq'::regclass);


--
-- Data for Name: aposta; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.aposta (codaposta, status, data_hora, valor, valor_retorno, codusuario) FROM stdin;
2	PERDIDA	2026-09-13 15:00:00	50.00	0.00	2
3	GANHA	2026-09-13 16:45:00	50.00	238.00	3
4	PENDENTE	2026-10-01 10:20:00	150.00	\N	5
5	PENDENTE	2026-10-02 14:10:00	80.00	\N	6
6	PENDENTE	2026-10-03 09:40:00	200.00	\N	7
7	PENDENTE	2026-10-04 11:15:00	100.00	\N	9
9	PENDENTE	2026-10-05 18:00:00	50.00	\N	11
8	CANCELADA	2026-10-04 16:30:00	60.00	60.00	10
11	PENDENTE	2026-09-21 04:28:00.832785	10.00	\N	1
12	PENDENTE	2026-09-21 04:29:06.55661	10.00	\N	2
13	PENDENTE	2026-09-21 04:41:53.594029	50.00	\N	1
14	PENDENTE	2026-09-21 05:39:46.64972	11.00	\N	1
15	PENDENTE	2026-09-21 06:36:42.123473	10.00	\N	1
16	PENDENTE	2026-09-29 22:19:11.276129	10.00	\N	1
17	PENDENTE	2026-09-29 23:14:33.919736	10.00	\N	1
10	PERDIDA	2026-09-20 12:00:00	100.00	0.00	13
18	PENDENTE	2026-10-01 13:28:45.10795	10.00	\N	1
19	PENDENTE	2026-10-01 13:38:28.078853	19.00	\N	1
20	GANHA	2026-10-01 14:24:45.342428	15.00	24.75	1
\.


--
-- Data for Name: competicao; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.competicao (codcompeticao, nome, pais, data_inicio, data_fim, codesporte) FROM stdin;
1	Brasileirao Serie A	\N	2026-04-10	2026-12-06	1
2	Champions League	\N	2026-09-15	2027-05-29	1
3	Copa do Brasil	\N	2026-02-20	2026-10-25	1
4	NBA Regular Season	\N	2026-10-20	2027-04-15	2
5	ATP Masters 1000	\N	2026-08-01	2026-11-15	3
6	Superliga de Volei	\N	2026-10-01	2027-04-30	4
7	UFC Fight Night	\N	2026-01-01	2026-12-31	5
8	Formula 1 - Temporada	\N	2026-03-01	2026-11-30	6
9	IEM CS:GO Major	\N	2026-10-10	2026-10-28	7
10	Copa do Mundo de Rugby 2026	Franca	2026-10-01	2026-11-30	9
\.


--
-- Data for Name: competidor; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.competidor (codcompetidor, nome, tipo) FROM stdin;
1	Flamengo	TIME
2	Palmeiras	TIME
3	Corinthians	TIME
4	Sao Paulo	TIME
5	Real Madrid	TIME
6	Manchester City	TIME
7	Bayern de Munique	TIME
8	PSG	TIME
9	Los Angeles Lakers	TIME
10	Boston Celtics	TIME
11	Golden State Warriors	TIME
12	Chicago Bulls	TIME
13	Carlos Alcaraz	INDIVIDUAL
14	Novak Djokovic	INDIVIDUAL
15	Jannik Sinner	INDIVIDUAL
16	Sada Cruzeiro	TIME
17	Minas Volei	TIME
18	Alex Poatan	INDIVIDUAL
19	Israel Adesanya	INDIVIDUAL
20	Islam Makhachev	INDIVIDUAL
21	Max Verstappen	INDIVIDUAL
22	Lewis Hamilton	INDIVIDUAL
23	Furia Esports	TIME
24	Natus Vincere	TIME
25	Jhonatan	INDIVIDUAL
26	Gremio	TIME
27	Internacional	TIME
28	All Blacks	TIME
29	Springboks	TIME
30	Barcelona	TIME
\.


--
-- Data for Name: esporte; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.esporte (codesporte, nome, max_competidores_evento) FROM stdin;
7	CS:GO (Esports)	2
8	Futebol Americano	2
1	Futebol	2
2	Basquete	2
3	Tenis	2
4	Volei	2
5	MMA	2
6	Formula 1	20
9	Rugby	15
\.


--
-- Data for Name: evento_esportivo; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.evento_esportivo (codevento, data_hora, status, codcompeticao, descricao) FROM stdin;
1	2026-10-15 16:00:00	AGENDADO	1	Rodada 26 - Flamengo x Palmeiras
2	2026-09-13 18:30:00	FINALIZADO	1	Rodada 25 - Corinthians x Sao Paulo
3	2026-10-20 16:00:00	AGENDADO	2	Fase de Grupos - Real Madrid x Manchester City
4	2026-10-21 16:00:00	AGENDADO	2	Fase de Grupos - PSG x Bayern de Munique
5	2026-09-25 20:00:00	FINALIZADO	3	Oitavas de Final - Gremio x Internacional
6	2026-11-05 21:00:00	AGENDADO	4	Temporada Regular - Lakers x Celtics
7	2026-09-10 21:00:00	FINALIZADO	4	Temporada Regular - Warriors x Bulls
8	2026-10-18 14:00:00	AGENDADO	5	Final Masculina - Carlos Alcaraz x Novak Djokovic
9	2026-09-14 23:00:00	FINALIZADO	7	Card Principal - Alex Poatan x Israel Adesanya
10	2026-11-08 14:00:00	AGENDADO	8	Grand Prix de Sao Paulo (Interlagos)
11	2026-10-28 17:00:00	AGENDADO	9	Grande Final Major - Furia Esports x Natus Vincere
12	2026-10-12 19:30:00	AGENDADO	6	Semifinal - Sada Cruzeiro x Minas Volei
13	2026-10-15 16:00:00	AGENDADO	10	Fase de Grupos - All Blacks x Springboks
14	2026-10-01 16:00:00	AGENDADO	2	Real Madrid x Barca
\.


--
-- Data for Name: item_aposta; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.item_aposta (coditem, oddcadastrada, resultado, codaposta, codopcao) FROM stdin;
2	2.60	PERDIDA	2	8
3	1.70	GANHA	3	26
4	2.80	GANHA	3	10
5	2.15	PENDENTE	4	1
6	2.20	PENDENTE	5	13
7	1.75	PENDENTE	5	22
8	1.55	PENDENTE	6	30
9	2.25	PENDENTE	7	32
11	1.85	PENDENTE	9	18
12	1.65	PENDENTE	9	16
10	1.85	CANCELADA	8	4
14	2.15	PENDENTE	11	1
15	3.30	PENDENTE	11	2
16	3.30	PENDENTE	12	2
17	2.15	PENDENTE	13	1
18	1.85	PENDENTE	13	18
19	1.95	PENDENTE	14	5
20	1.85	PENDENTE	14	18
21	1.55	PENDENTE	15	30
22	3.10	PENDENTE	15	15
23	2.15	PENDENTE	16	1
24	2.15	PENDENTE	17	1
25	1.75	PENDENTE	17	6
13	3.40	PERDIDA	10	3
26	2.20	PENDENTE	18	13
27	1.60	PENDENTE	19	39
28	1.65	GANHA	20	46
\.


--
-- Data for Name: mercado; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.mercado (codmercado, status, tipo, codevento) FROM stdin;
1	ABERTO	Vencedor da Partida (1X2)	1
2	ABERTO	Total de Gols (Mais/Menos 2.5)	1
3	ABERTO	Ambas as Equipes Marcam	1
4	FECHADO	Vencedor da Partida (1X2)	2
5	FECHADO	Total de Gols (Mais/Menos 2.5)	2
6	ABERTO	Vencedor da Partida (1X2)	3
7	ABERTO	Total de Gols (Mais/Menos 2.5)	3
8	ABERTO	Vencedor da Partida	6
9	ABERTO	Total de Pontos (Mais/Menos 220.5)	6
10	ABERTO	Vencedor da Partida	8
11	ABERTO	Total de Sets (Mais/Menos 3.5)	8
12	FECHADO	Vencedor da Luta	9
13	FECHADO	Metodo da Vitoria (KO / Decisao)	9
14	ABERTO	Vencedor do GP de F1	10
15	ABERTO	Vencedor do Confronto (MD3)	11
16	ABERTO	Total de Mapas (Mais/Menos 2.5)	11
18	ABERTO	Real Madrid Ganha	14
\.


--
-- Data for Name: opcao_aposta; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.opcao_aposta (codopcao, resultado, odd, descricao, codmercado) FROM stdin;
1	Flamengo Vence	2.15	Vitoria do Flamengo no tempo normal	1
2	Empate	3.30	Empate no tempo normal	1
3	Palmeiras Vence	3.40	Vitoria do Palmeiras no tempo normal	1
4	Mais de 2.5 Gols	1.85	Partida com 3 ou mais gols marcados	2
5	Menos de 2.5 Gols	1.95	Partida com 2 gols ou menos marcados	2
6	Sim (Ambas Marcam)	1.75	Flamengo e Palmeiras marcam ao menos 1 gol	3
7	Nao (Ambas Marcam)	2.05	Pelo menos uma equipe termina sem marcar	3
8	Corinthians Vence	2.60	Vitoria do Corinthians	4
9	Empate	3.10	Empate classico	4
10	Sao Paulo Vence	2.80	Vitoria do Sao Paulo	4
11	Mais de 2.5 Gols	2.10	Jogo com mais de 2 gols	5
12	Menos de 2.5 Gols	1.70	Jogo com menos de 3 gols	5
13	Real Madrid Vence	2.20	Vitoria do Real Madrid no Santiago Bernabeu	6
14	Empate	3.60	Empate no jogo de ida	6
15	Manchester City Vence	3.10	Vitoria do Manchester City	6
16	Mais de 2.5 Gols	1.65	Partida ofensiva com 3 ou mais gols	7
17	Menos de 2.5 Gols	2.25	Partida truncada com ate 2 gols	7
18	Los Angeles Lakers Vence	1.85	Vitoria dos Lakers em casa	8
19	Boston Celtics Vence	1.95	Vitoria dos Celtics fora de casa	8
20	Mais de 220.5 Pontos	1.90	Placar combinado superior a 220 pontos	9
21	Menos de 220.5 Pontos	1.90	Placar combinado ate 220 pontos	9
22	Carlos Alcaraz Campeao	1.75	Vitoria de Carlos Alcaraz	10
23	Novak Djokovic Campeao	2.10	Vitoria de Novak Djokovic	10
24	Mais de 3.5 Sets	1.80	Jogo decidido em 4 ou 5 sets	11
25	Menos de 3.5 Sets	2.00	Jogo decidido em sets diretos (3x0)	11
26	Alex Poatan Vence	1.70	Vitoria de Alex Poatan	12
27	Israel Adesanya Vence	2.15	Vitoria de Israel Adesanya	12
28	Vitoria por Nocaute (KO/TKO)	1.85	Fim por nocaute em qualquer round	13
29	Vitoria por Decisao dos Juizes	3.20	Luta ate o 5o round	13
30	Max Verstappen Vencedor	1.55	Vitoria de Max Verstappen em Interlagos	14
31	Lewis Hamilton Vencedor	4.50	Vitoria de Lewis Hamilton em Interlagos	14
32	Furia Esports Vence	2.25	Vitoria brasileira no Major de CS	15
33	Natus Vincere Vence	1.65	Vitoria da NaVi no Major de CS	15
34	Mais de 2.5 Mapas	1.90	Confronto vai ao 3o mapa decisivo	16
35	Menos de 2.5 Mapas	1.90	Vitoria por 2 a 0 em mapas	16
36	Real Madrid Vence	2.50	Partido de UCL	4
39	Real Madrid Ganha	1.60	Semifinal UCL	6
46	Real Madrid Ganha	1.65	UCL Semifinal	18
48	Real Madrid Empate	2.00	UCL Semifinal	18
\.


--
-- Data for Name: participacao; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.participacao (codevento, codcompetidor) FROM stdin;
1	1
1	2
2	3
2	4
3	5
3	6
6	9
6	10
8	13
8	14
9	18
9	19
10	21
10	22
11	23
11	24
4	8
4	7
5	26
5	27
7	11
7	12
12	16
12	17
13	28
13	29
\.


--
-- Data for Name: usuario; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.usuario (codusuario, nome, email, senha, data_cadastro, status) FROM stdin;
1	Carlos Silva	carlos.silva@email.com	$2b$10$hashfake1	2026-08-01 10:00:00	ATIVO
2	Ana Beatriz Souza	ana.souza@email.com	$2b$10$hashfake2	2026-08-05 11:30:00	ATIVO
3	Joao Pereira	joao.pereira@email.com	$2b$10$hashfake3	2026-08-10 14:15:00	ATIVO
4	Mariana Costa	mariana.costa@email.com	$2b$10$hashfake4	2026-08-12 16:45:00	BLOQUEADO
5	Pedro Almeida	pedro.almeida@email.com	$2b$10$hashfake5	2026-08-15 09:20:00	ATIVO
6	Fernanda Lima	fernanda.lima@email.com	$2b$10$hashfake6	2026-08-18 18:00:00	ATIVO
7	Rafael Santos	rafael.santos@email.com	$2b$10$hashfake7	2026-08-20 20:10:00	ATIVO
8	Juliana Oliveira	juliana.oliveira@email.com	$2b$10$hashfake8	2026-08-22 13:50:00	INATIVO
9	Bruno Ferreira	bruno.ferreira@email.com	$2b$10$hashfake9	2026-08-25 15:30:00	ATIVO
10	Camila Rodrigues	camila.rodrigues@email.com	$2b$10$hashfake10	2026-08-28 17:00:00	ATIVO
11	Lucas Martins	lucas.martins@email.com	$2b$10$hashfake11	2026-09-01 10:00:00	ATIVO
12	Beatriz Carvalho	beatriz.carvalho@email.com	$2b$10$hashfake12	2026-09-02 12:00:00	BLOQUEADO
13	Thiago Gomes	thiago.gomes@email.com	$2b$10$hashfake13	2026-09-03 14:00:00	ATIVO
14	Larissa Ribeiro	larissa.ribeiro@email.com	$2b$10$hashfake14	2026-09-04 15:30:00	ATIVO
15	Diego Barbosa	diego.barbosa@email.com	$2b$10$hashfake15	2026-09-05 16:45:00	ATIVO
\.


--
-- Name: aposta_codaposta_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.aposta_codaposta_seq', 20, true);


--
-- Name: competicao_codcompeticao_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.competicao_codcompeticao_seq', 10, true);


--
-- Name: competidor_codcompetidor_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.competidor_codcompetidor_seq', 31, true);


--
-- Name: esporte_codesporte_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.esporte_codesporte_seq', 9, true);


--
-- Name: evento_esportivo_codevento_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.evento_esportivo_codevento_seq', 14, true);


--
-- Name: item_aposta_coditem_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.item_aposta_coditem_seq', 28, true);


--
-- Name: mercado_codmercado_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.mercado_codmercado_seq', 18, true);


--
-- Name: opcao_aposta_codopcao_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.opcao_aposta_codopcao_seq', 48, true);


--
-- Name: usuario_codusuario_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.usuario_codusuario_seq', 15, true);


--
-- Name: aposta aposta_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.aposta
    ADD CONSTRAINT aposta_pkey PRIMARY KEY (codaposta);


--
-- Name: competicao competicao_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.competicao
    ADD CONSTRAINT competicao_pkey PRIMARY KEY (codcompeticao);


--
-- Name: competidor competidor_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.competidor
    ADD CONSTRAINT competidor_pkey PRIMARY KEY (codcompetidor);


--
-- Name: esporte esporte_nome_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.esporte
    ADD CONSTRAINT esporte_nome_key UNIQUE (nome);


--
-- Name: esporte esporte_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.esporte
    ADD CONSTRAINT esporte_pkey PRIMARY KEY (codesporte);


--
-- Name: evento_esportivo evento_esportivo_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.evento_esportivo
    ADD CONSTRAINT evento_esportivo_pkey PRIMARY KEY (codevento);


--
-- Name: item_aposta item_aposta_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.item_aposta
    ADD CONSTRAINT item_aposta_pkey PRIMARY KEY (coditem);


--
-- Name: mercado mercado_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.mercado
    ADD CONSTRAINT mercado_pkey PRIMARY KEY (codmercado);


--
-- Name: opcao_aposta opcao_aposta_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.opcao_aposta
    ADD CONSTRAINT opcao_aposta_pkey PRIMARY KEY (codopcao);


--
-- Name: participacao participacao_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.participacao
    ADD CONSTRAINT participacao_pkey PRIMARY KEY (codevento, codcompetidor);


--
-- Name: usuario usuario_email_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.usuario
    ADD CONSTRAINT usuario_email_key UNIQUE (email);


--
-- Name: usuario usuario_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.usuario
    ADD CONSTRAINT usuario_pkey PRIMARY KEY (codusuario);


--
-- Name: idx_aposta_usuario; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_aposta_usuario ON public.aposta USING btree (codusuario);


--
-- Name: idx_competicao_esporte; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_competicao_esporte ON public.competicao USING btree (codesporte);


--
-- Name: idx_evento_competicao; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_evento_competicao ON public.evento_esportivo USING btree (codcompeticao);


--
-- Name: idx_item_aposta_aposta; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_item_aposta_aposta ON public.item_aposta USING btree (codaposta);


--
-- Name: idx_item_aposta_opcao; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_item_aposta_opcao ON public.item_aposta USING btree (codopcao);


--
-- Name: idx_mercado_evento; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_mercado_evento ON public.mercado USING btree (codevento);


--
-- Name: idx_opcao_mercado; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_opcao_mercado ON public.opcao_aposta USING btree (codmercado);


--
-- Name: aposta aposta_codusuario_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.aposta
    ADD CONSTRAINT aposta_codusuario_fkey FOREIGN KEY (codusuario) REFERENCES public.usuario(codusuario);


--
-- Name: competicao competicao_codesporte_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.competicao
    ADD CONSTRAINT competicao_codesporte_fkey FOREIGN KEY (codesporte) REFERENCES public.esporte(codesporte);


--
-- Name: evento_esportivo evento_esportivo_codcompeticao_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.evento_esportivo
    ADD CONSTRAINT evento_esportivo_codcompeticao_fkey FOREIGN KEY (codcompeticao) REFERENCES public.competicao(codcompeticao);


--
-- Name: item_aposta item_aposta_codaposta_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.item_aposta
    ADD CONSTRAINT item_aposta_codaposta_fkey FOREIGN KEY (codaposta) REFERENCES public.aposta(codaposta);


--
-- Name: item_aposta item_aposta_codopcao_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.item_aposta
    ADD CONSTRAINT item_aposta_codopcao_fkey FOREIGN KEY (codopcao) REFERENCES public.opcao_aposta(codopcao);


--
-- Name: mercado mercado_codevento_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.mercado
    ADD CONSTRAINT mercado_codevento_fkey FOREIGN KEY (codevento) REFERENCES public.evento_esportivo(codevento);


--
-- Name: opcao_aposta opcao_aposta_codmercado_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.opcao_aposta
    ADD CONSTRAINT opcao_aposta_codmercado_fkey FOREIGN KEY (codmercado) REFERENCES public.mercado(codmercado);


--
-- Name: participacao participacao_codcompetidor_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.participacao
    ADD CONSTRAINT participacao_codcompetidor_fkey FOREIGN KEY (codcompetidor) REFERENCES public.competidor(codcompetidor);


--
-- Name: participacao participacao_codevento_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.participacao
    ADD CONSTRAINT participacao_codevento_fkey FOREIGN KEY (codevento) REFERENCES public.evento_esportivo(codevento);


--
-- PostgreSQL database dump complete
--

\unrestrict lrrWUnmL2ydiSdeNfmAJ208f4sa2uHZsgxztlMzn81qdXpxSTBhaUaeEKM78FpW

