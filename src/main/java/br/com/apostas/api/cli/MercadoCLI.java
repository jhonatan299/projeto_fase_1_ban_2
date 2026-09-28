package br.com.apostas.api.cli;

import br.com.apostas.api.model.EventoEsportivo;
import br.com.apostas.api.model.Mercado;
import br.com.apostas.api.service.MercadoService;
import br.com.apostas.api.util.ConsoleUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

/**
 * Interface de console para gerenciamento dos mercados de apostas.
 */
@Component
@RequiredArgsConstructor
public class MercadoCLI {

    private final MercadoService mercadoService;

    // Exibe as opções interativas no console e processa a entrada do usuário
    public void exibirMenu(Scanner scanner) {
        int opcao = -1;
        do {
            System.out.println("\n========================================================");
            System.out.println("               GERENCIAMENTO DE MERCADOS                ");
            System.out.println("========================================================");
            System.out.println(" 1 - Cadastrar novo mercado");
            System.out.println(" 2 - Listar todos os mercados");
            System.out.println(" 3 - Consultar mercado por ID");
            System.out.println(" 4 - Atualizar dados de um mercado");
            System.out.println(" 5 - Remover mercado");
            System.out.println(" 0 - Voltar ao menu principal");
            System.out.println("========================================================");

            try {
                opcao = ConsoleUtils.lerInteiro(scanner, "Escolha uma opcao: ");

                switch (opcao) {
                    case 1 -> cadastrar(scanner);
                    case 2 -> listar();
                    case 3 -> consultarPorId(scanner);
                    case 4 -> atualizar(scanner);
                    case 5 -> remover(scanner);
                    case 0 -> System.out.println("Retornando ao menu principal...");
                    default -> System.out.println("[!] Opcao invalida! Escolha um valor entre 0 e 5.");
                }
            } catch (Exception e) {
                System.out.println("\n[X] ERRO: " + e.getMessage());
            }
        } while (opcao != 0);
    }

    private void cadastrar(Scanner scanner) {
        System.out.println("\n--- [ NOVO MERCADO ] ---");
        String tipo = ConsoleUtils.lerTextoObrigatorio(scanner, "Tipo/Nome do Mercado (ex: 1X2 / Vencedor, Total Gols, Ambas Marcam): ");
        int codevento = ConsoleUtils.lerInteiro(scanner, "ID do Evento Esportivo vinculado (codevento): ");

        Mercado m = Mercado.builder()
                .tipo(tipo)
                .status("ABERTO")
                .evento(EventoEsportivo.builder().codevento(codevento).build())
                .build();

        Mercado criado = mercadoService.criar(m);
        System.out.println("\n[OK] Mercado cadastrado com sucesso! ID: " + criado.getCodmercado() + " | Tipo: " + criado.getTipo() + " | Status: " + criado.getStatus());
    }

    private void listar() {
        System.out.println("\n--- [ LISTAGEM DE MERCADOS ] ---");
        List<Mercado> lista = mercadoService.listarTodos();
        if (lista.isEmpty()) {
            System.out.println("[i] Nenhum mercado cadastrado no banco de dados.");
            return;
        }

        System.out.println("========================================================================================================================================================");
        System.out.printf("%-5s | %-38s | %-10s | %s%n",
                "ID", "Tipo/Nome do Mercado", "Status", "Evento Esportivo Vinculado");
        System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------");
        for (Mercado m : lista) {
            String evDesc = (m.getEvento() != null && m.getEvento().getDescricao() != null)
                    ? m.getEvento().getDescricao()
                    : (m.getEvento() != null ? "Evento #" + m.getEvento().getCodevento() : "Sem evento");
            int evId = (m.getEvento() != null) ? m.getEvento().getCodevento() : 0;

            System.out.printf("ID: %-3d | %-38s | %-10s | %s (ID %d)%n",
                    m.getCodmercado(), m.getTipo(), m.getStatus(), evDesc, evId);
        }
        System.out.println("========================================================================================================================================================");
    }

    private void consultarPorId(Scanner scanner) {
        int id = ConsoleUtils.lerInteiro(scanner, "\nInforme o ID do mercado: ");
        Mercado m = mercadoService.buscarPorId(id);
        String evDesc = (m.getEvento() != null && m.getEvento().getDescricao() != null)
                ? m.getEvento().getDescricao()
                : (m.getEvento() != null ? "Evento #" + m.getEvento().getCodevento() : "Sem evento");
        int evId = (m.getEvento() != null) ? m.getEvento().getCodevento() : 0;

        System.out.println("\n--- [ DETALHES DO MERCADO #" + id + " ] ---");
        System.out.printf("  * ID: %d%n", m.getCodmercado());
        System.out.printf("  * Tipo: %s%n", m.getTipo());
        System.out.printf("  * Status: %s%n", m.getStatus());
        System.out.printf("  * Evento Vinculado: %s (ID %d)%n", evDesc, evId);
    }

    private void atualizar(Scanner scanner) {
        System.out.println("\n--- [ ATUALIZACAO DE MERCADO ] ---");
        int id = ConsoleUtils.lerInteiro(scanner, "Informe o ID do mercado que deseja atualizar: ");
        Mercado atual = mercadoService.buscarPorId(id);

        String evDescAtual = (atual.getEvento() != null && atual.getEvento().getDescricao() != null)
                ? atual.getEvento().getDescricao()
                : (atual.getEvento() != null ? "Evento #" + atual.getEvento().getCodevento() : "N/A");

        System.out.println("Dados atuais: " + atual.getTipo() + " (Status: " + atual.getStatus() + " | Evento: " + evDescAtual + ")");
        System.out.println("(Pressione ENTER para manter o valor atual)");

        String tipo = ConsoleUtils.lerTextoOpcional(scanner, "Novo Tipo [" + atual.getTipo() + "]: ", atual.getTipo());
        String status = lerStatusMercado(scanner, atual.getStatus());
        int codEvento = ConsoleUtils.lerInteiro(scanner, "ID do Evento vinculado [" + atual.getEvento().getCodevento() + "]: ");

        Mercado dados = Mercado.builder()
                .tipo(tipo)
                .status(status)
                .evento(EventoEsportivo.builder().codevento(codEvento).build())
                .build();

        mercadoService.atualizar(id, dados);
        System.out.println("\n[OK] Mercado ID " + id + " atualizado com sucesso!");
    }

    private void remover(Scanner scanner) {
        int id = ConsoleUtils.lerInteiro(scanner, "\nInforme o ID do mercado a remover: ");
        Mercado m = mercadoService.buscarPorId(id);

        boolean conf = ConsoleUtils.lerConfirmacao(scanner, "Tem certeza que deseja excluir o mercado '" + m.getTipo() + "'? (S/N): ");
        if (conf) {
            mercadoService.deletar(id);
            System.out.println("[OK] Mercado ID " + id + " removido com sucesso!");
        } else {
            System.out.println("[i] Remocao cancelada.");
        }
    }

    private String lerStatusMercado(Scanner scanner, String padrao) {
        System.out.println("Status do Mercado:");
        System.out.println("  1 - ABERTO");
        System.out.println("  2 - SUSPENSO");
        System.out.println("  3 - FECHADO");
        while (true) {
            String prompt = padrao != null
                    ? "Escolha o status (1/2/3) [Padrao: " + padrao + "]: "
                    : "Escolha o status (1/2/3): ";
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.isEmpty() && padrao != null) return padrao;

            switch (input.toUpperCase()) {
                case "1", "ABERTO" -> { return "ABERTO"; }
                case "2", "SUSPENSO" -> { return "SUSPENSO"; }
                case "3", "FECHADO" -> { return "FECHADO"; }
                default -> System.out.println("[!] Opcao invalida! Digite 1 (ABERTO), 2 (SUSPENSO) ou 3 (FECHADO). Tente novamente.");
            }
        }
    }
}
