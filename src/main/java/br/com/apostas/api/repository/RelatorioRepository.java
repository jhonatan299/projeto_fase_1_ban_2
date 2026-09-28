package br.com.apostas.api.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

/**
 * Repositório para consultas analíticas e relatórios agregados via SQL nativo.
 */
@org.springframework.stereotype.Repository
public interface RelatorioRepository extends Repository<br.com.apostas.api.model.Usuario, Integer> {

    @Query(value = "SELECT a.codaposta, TO_CHAR(a.data_hora, 'DD/MM/YYYY HH24:MI') AS data_aposta, " +
                   "a.valor AS valor_aposta, a.status AS status_aposta, a.valor_retorno, " +
                   "i.coditem, i.oddcadastrada, i.resultado AS status_item, " +
                   "o.resultado AS palpite, m.tipo AS tipo_mercado, " +
                   "TO_CHAR(e.data_hora, 'DD/MM/YYYY HH24:MI') AS data_evento, " +
                   "comp.nome AS nome_competicao, esp.nome AS nome_esporte " +
                   "FROM aposta a " +
                   "JOIN item_aposta i ON a.codaposta = i.codaposta " +
                   "JOIN opcao_aposta o ON i.codopcao = o.codopcao " +
                   "JOIN mercado m ON o.codmercado = m.codmercado " +
                   "JOIN evento_esportivo e ON m.codevento = e.codevento " +
                   "JOIN competicao comp ON e.codcompeticao = comp.codcompeticao " +
                   "JOIN esporte esp ON comp.codesporte = esp.codesporte " +
                   "WHERE a.codusuario = :codusuario " +
                   "ORDER BY a.data_hora DESC, a.codaposta DESC, i.coditem ASC", nativeQuery = true)
    // Relatório 1: Histórico completo e detalhado de apostas por usuário
    List<Map<String, Object>> historicoApostasUsuario(@Param("codusuario") int codusuario);

    @Query(value = "SELECT comp.codcompeticao, comp.nome AS nome_competicao, esp.nome AS nome_esporte, " +
                   "COUNT(DISTINCT a.codaposta) AS total_apostas, " +
                   "SUM(a.valor) AS volume_total, " +
                   "ROUND(AVG(a.valor), 2) AS ticket_medio " +
                   "FROM competicao comp " +
                   "JOIN esporte esp ON comp.codesporte = esp.codesporte " +
                   "JOIN evento_esportivo e ON comp.codcompeticao = e.codcompeticao " +
                   "JOIN mercado m ON e.codevento = m.codevento " +
                   "JOIN opcao_aposta o ON m.codmercado = o.codmercado " +
                   "JOIN item_aposta i ON o.codopcao = i.codopcao " +
                   "JOIN aposta a ON i.codaposta = a.codaposta " +
                   "GROUP BY comp.codcompeticao, comp.nome, esp.nome " +
                   "ORDER BY volume_total DESC", nativeQuery = true)
    // Relatório 2: Volume financeiro total e ticket médio agrupado por competição
    List<Map<String, Object>> volumeApostadoPorCompeticao();

    @Query(value = "SELECT c.codcompetidor, c.nome AS nome_competidor, c.tipo AS tipo_competidor, " +
                   "COUNT(p.codevento) AS total_participacoes, " +
                   "COUNT(CASE WHEN e.status = 'FINALIZADO' THEN 1 END) AS eventos_finalizados, " +
                   "COUNT(CASE WHEN e.status = 'AGENDADO' THEN 1 END) AS eventos_agendados, " +
                   "COUNT(CASE WHEN e.status = 'AO_VIVO' THEN 1 END) AS eventos_ao_vivo " +
                   "FROM competidor c " +
                   "LEFT JOIN participacao p ON c.codcompetidor = p.codcompetidor " +
                   "LEFT JOIN evento_esportivo e ON p.codevento = e.codevento " +
                   "GROUP BY c.codcompetidor, c.nome, c.tipo " +
                   "ORDER BY total_participacoes DESC, c.nome ASC", nativeQuery = true)
    // Relatório 3: Desempenho e contagem de eventos por competidor
    List<Map<String, Object>> participacaoCompetidores();

    @Query(value = "SELECT esp.nome AS nome_esporte, m.tipo AS tipo_mercado, " +
                   "COUNT(o.codopcao) AS total_opcoes, " +
                   "ROUND(AVG(o.odd), 2) AS odd_media, " +
                   "MIN(o.odd) AS odd_minima, " +
                   "MAX(o.odd) AS odd_maxima " +
                   "FROM esporte esp " +
                   "JOIN competicao c ON esp.codesporte = c.codesporte " +
                   "JOIN evento_esportivo e ON c.codcompeticao = e.codcompeticao " +
                   "JOIN mercado m ON e.codevento = m.codevento " +
                   "JOIN opcao_aposta o ON m.codmercado = o.codmercado " +
                   "GROUP BY esp.nome, m.tipo " +
                   "ORDER BY esp.nome ASC, m.tipo ASC", nativeQuery = true)
    // Relatório 4: Análise estatística de odds (média, mínima, máxima) por mercado
    List<Map<String, Object>> oddMediaPorEsporteMercado();

    @Query(value = "SELECT u.codusuario, u.nome, " +
                   "COUNT(a.codaposta) AS total_apostas_liquidadas, " +
                   "COUNT(CASE WHEN a.status = 'GANHA' THEN 1 END) AS apostas_ganhas, " +
                   "SUM(a.valor) AS total_investido, " +
                   "COALESCE(SUM(CASE WHEN a.status = 'GANHA' THEN a.valor_retorno ELSE 0 END), 0) AS total_retornado, " +
                   "COALESCE(SUM(CASE WHEN a.status = 'GANHA' THEN (a.valor_retorno - a.valor) ELSE -a.valor END), 0) AS saldo_lucro " +
                   "FROM usuario u " +
                   "JOIN aposta a ON u.codusuario = a.codusuario " +
                   "WHERE a.status IN ('GANHA', 'PERDIDA') " +
                   "GROUP BY u.codusuario, u.nome " +
                   "ORDER BY saldo_lucro DESC", nativeQuery = true)
    List<Map<String, Object>> rankingLucroUsuarios();
}
