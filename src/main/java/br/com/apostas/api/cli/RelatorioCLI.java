package br.com.apostas.api.cli;

import br.com.apostas.api.service.RelatorioService;
import br.com.apostas.api.util.ConsoleUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Interface de console para exibição dos 5 relatórios analíticos do sistema.
 */
@Component
@RequiredArgsConstructor
public class RelatorioCLI {

    private final RelatorioService relatorioService;

    // Exibe as opções interativas no console e processa a entrada do usuário
    public void exibirMenu(Scanner scanner) {
        int opcao = -1;
        do {
            System.out.println("\n========================================================");
            System.out.println("                 RELATORIOS ANALITICOS                  ");
            System.out.println("========================================================");
            System.out.println(" 1 - Historico Completo de Apostas por Usuario");
            System.out.println(" 2 - Volume Financeiro Apostado por Competicao");
            System.out.println(" 3 - Participacao e Desempenho de Competidores");
            System.out.println(" 4 - Odd Media por Esporte e Tipo de Mercado");
            System.out.println(" 5 - Ranking de Lucratividade dos Usuarios");
            System.out.println(" 0 - Voltar ao menu principal");
            System.out.println("========================================================");

            try {
                opcao = ConsoleUtils.lerInteiro(scanner, "Escolha um relatorio: ");

                switch (opcao) {
                    case 1 -> historicoUsuario(scanner);
                    case 2 -> volumeCompeticao();
                    case 3 -> participacaoCompetidores();
                    case 4 -> oddMedia();
                    case 5 -> rankingLucro();
                    case 0 -> System.out.println("Retornando ao menu principal...");
                    default -> System.out.println("[!] Opcao invalida! Escolha um valor entre 0 e 5.");
                }
            } catch (Exception e) {
                System.out.println("\n[X] ERRO: " + e.getMessage());
            }
        } while (opcao != 0);
    }

    private void historicoUsuario(Scanner scanner) {
        System.out.println("\n--- [ RELATORIO: HISTORICO DE APOSTAS DO USUARIO ] ---");
        int codusuario = ConsoleUtils.lerInteiro(scanner, "Informe o ID do usuario (codusuario): ");

        List<Map<String, Object>> dados = relatorioService.obterHistoricoApostasUsuario(codusuario);
        if (dados.isEmpty()) {
            System.out.println("[i] Nenhum historico de apostas para este usuario.");
            return;
        }

        System.out.printf("\nHistorico detalhado do Usuario ID %d (%d selecoes encontradas):%n", codusuario, dados.size());
        System.out.println("======================================================================================================================================================");
        System.out.printf("%-8s | %-16s | %-10s | %-10s | %-11s | %-20s | %-18s | %-6s | %-10s | %-20s%n",
                "ApostaID", "Data Aposta", "Valor", "Status", "Retorno", "Palpite/Selecao", "Mercado", "Odd", "ItemStatus", "Competicao");
        System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------");

        for (Map<String, Object> r : dados) {
            String retStr = (r.get("valor_retorno") != null) ? String.format("R$ %.2f", (BigDecimal) r.get("valor_retorno")) : "-";
            System.out.printf("%-8s | %-16s | R$ %-7.2f | %-10s | %-11s | %-20s | %-18s | %-6.2f | %-10s | %-20s%n",
                    r.get("codaposta"), r.get("data_aposta"), (BigDecimal) r.get("valor_aposta"),
                    r.get("status_aposta"), retStr, r.get("palpite"), r.get("tipo_mercado"),
                    (BigDecimal) r.get("oddcadastrada"), r.get("status_item"), r.get("nome_competicao"));
        }
        System.out.println("======================================================================================================================================================");
    }

    private void volumeCompeticao() {
        System.out.println("\n--- [ RELATORIO: VOLUME APOSTADO POR COMPETICAO ] ---");
        List<Map<String, Object>> dados = relatorioService.obterVolumePorCompeticao();
        if (dados.isEmpty()) {
            System.out.println("[i] Nenhuma aposta registrada para computar volume.");
            return;
        }

        System.out.println("==================================================================================================");
        System.out.printf("%-4s | %-30s | %-15s | %-15s | %-15s | %-12s%n",
                "ID", "Competicao", "Esporte", "Total Apostas", "Volume Total", "Ticket Medio");
        System.out.println("--------------------------------------------------------------------------------------------------");

        for (Map<String, Object> r : dados) {
            System.out.printf("%-4s | %-30s | %-15s | %-15s | R$ %-12.2f | R$ %-9.2f%n",
                    r.get("codcompeticao"), r.get("nome_competicao"), r.get("nome_esporte"),
                    r.get("total_apostas"), (BigDecimal) r.get("volume_total"), (BigDecimal) r.get("ticket_medio"));
        }
        System.out.println("==================================================================================================");
    }

    private void participacaoCompetidores() {
        System.out.println("\n--- [ RELATORIO: PARTICIPACAO E DESEMPENHO DE COMPETIDORES ] ---");
        List<Map<String, Object>> dados = relatorioService.obterParticipacaoCompetidores();
        if (dados.isEmpty()) {
            System.out.println("[i] Nenhum competidor encontrado.");
            return;
        }

        System.out.println("===============================================================================================================");
        System.out.printf("%-4s | %-28s | %-12s | %-15s | %-14s | %-12s | %-10s%n",
                "ID", "Nome Competidor", "Tipo", "Participacoes", "Finalizados", "Agendados", "Ao Vivo");
        System.out.println("---------------------------------------------------------------------------------------------------------------");

        for (Map<String, Object> r : dados) {
            System.out.printf("%-4s | %-28s | %-12s | %-15s | %-14s | %-12s | %-10s%n",
                    r.get("codcompetidor"), r.get("nome_competidor"), r.get("tipo_competidor"),
                    r.get("total_participacoes"), r.get("eventos_finalizados"), r.get("eventos_agendados"), r.get("eventos_ao_vivo"));
        }
        System.out.println("===============================================================================================================");
    }

    private void oddMedia() {
        System.out.println("\n--- [ RELATORIO: ODD MEDIA POR ESPORTE E MERCADO ] ---");
        List<Map<String, Object>> dados = relatorioService.obterOddMediaPorEsporteMercado();
        if (dados.isEmpty()) {
            System.out.println("[i] Nenhuma odd cadastrada no sistema.");
            return;
        }

        System.out.println("============================================================================================");
        System.out.printf("%-18s | %-22s | %-12s | %-10s | %-10s | %-10s%n",
                "Esporte", "Tipo Mercado", "Total Opcoes", "Odd Media", "Odd Minima", "Odd Maxima");
        System.out.println("--------------------------------------------------------------------------------------------");

        for (Map<String, Object> r : dados) {
            System.out.printf("%-18s | %-22s | %-12s | %-10.2f | %-10.2f | %-10.2f%n",
                    r.get("nome_esporte"), r.get("tipo_mercado"), r.get("total_opcoes"),
                    (BigDecimal) r.get("odd_media"), (BigDecimal) r.get("odd_minima"), (BigDecimal) r.get("odd_maxima"));
        }
        System.out.println("============================================================================================");
    }

    private void rankingLucro() {
        System.out.println("\n--- [ RELATORIO: RANKING DE LUCRATIVIDADE DOS USUARIOS ] ---");
        List<Map<String, Object>> dados = relatorioService.obterRankingLucroUsuarios();
        if (dados.isEmpty()) {
            System.out.println("[i] Nenhuma aposta liquidada (GANHA/PERDIDA) no sistema.");
            return;
        }

        System.out.println("============================================================================================");
        System.out.printf("%-4s | %-20s | %-6s | %-6s | %-12s | %-12s | %-14s%n",
                "ID", "Nome Apostador", "Liq.", "Ganhas", "Investido", "Retornado", "Lucro/Prejuizo");
        System.out.println("--------------------------------------------------------------------------------------------");

        for (Map<String, Object> r : dados) {
            System.out.printf("%-4s | %-20s | %-6s | %-6s | R$ %-9.2f | R$ %-9.2f | R$ %-11.2f%n",
                    r.get("codusuario"), r.get("nome"),
                    r.get("total_apostas_liquidadas"), r.get("apostas_ganhas"),
                    (BigDecimal) r.get("total_investido"), (BigDecimal) r.get("total_retornado"), (BigDecimal) r.get("saldo_lucro"));
        }
        System.out.println("============================================================================================");
    }
}
