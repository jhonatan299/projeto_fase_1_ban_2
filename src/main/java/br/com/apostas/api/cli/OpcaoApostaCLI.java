package br.com.apostas.api.cli;

import br.com.apostas.api.model.Mercado;
import br.com.apostas.api.model.OpcaoAposta;
import br.com.apostas.api.service.OpcaoApostaService;
import br.com.apostas.api.util.ConsoleUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

/**
 * Interface de console para gerenciamento das opções de cotação (odds).
 */
@Component
@RequiredArgsConstructor
public class OpcaoApostaCLI {

    private final OpcaoApostaService opcaoService;

    // Exibe as opções interativas no console e processa a entrada do usuário
    public void exibirMenu(Scanner scanner) {
        int opcao = -1;
        do {
            System.out.println("\n========================================================");
            System.out.println("            GERENCIAMENTO DE OPCOES DE APOSTA           ");
            System.out.println("========================================================");
            System.out.println(" 1 - Cadastrar nova opcao de aposta");
            System.out.println(" 2 - Listar todas as opcoes");
            System.out.println(" 3 - Consultar opcao por ID");
            System.out.println(" 4 - Remover opcao");
            System.out.println(" 0 - Voltar ao menu principal");
            System.out.println("========================================================");

            try {
                opcao = ConsoleUtils.lerInteiro(scanner, "Escolha uma opcao: ");

                switch (opcao) {
                    case 1 -> cadastrar(scanner);
                    case 2 -> listar();
                    case 3 -> consultarPorId(scanner);
                    case 4 -> remover(scanner);
                    case 0 -> System.out.println("Retornando ao menu principal...");
                    default -> System.out.println("[!] Opcao invalida! Escolha um valor entre 0 e 4.");
                }
            } catch (Exception e) {
                System.out.println("\n[X] ERRO: " + e.getMessage());
            }
        } while (opcao != 0);
    }

    private void cadastrar(Scanner scanner) {
        System.out.println("\n--- [ NOVA OPCAO DE APOSTA ] ---");
        String resultado = ConsoleUtils.lerTextoObrigatorio(scanner, "Resultado / Palpite (ex: 'Flamengo Vence', 'Mais de 2.5'): ");
        BigDecimal odd = ConsoleUtils.lerBigDecimal(scanner, "Cotacao da Odd (ex: 1.85, minimo 1.01): ", new BigDecimal("1.01"));
        String desc = ConsoleUtils.lerTextoOpcional(scanner, "Descricao adicional (opcional): ", null);
        int codmercado = ConsoleUtils.lerInteiro(scanner, "ID do Mercado vinculado (codmercado): ");

        OpcaoAposta op = OpcaoAposta.builder()
                .resultado(resultado)
                .odd(odd)
                .descricao(desc)
                .mercado(Mercado.builder().codmercado(codmercado).build())
                .build();

        OpcaoAposta criada = opcaoService.criar(op);
        System.out.println("\n[OK] Opcao cadastrada com sucesso! ID: " + criada.getCodopcao() + " | Odd: " + criada.getOdd());
    }

    private void listar() {
        System.out.println("\n--- [ LISTAGEM DE OPCOES DE APOSTA ] ---");
        List<OpcaoAposta> lista = opcaoService.listarTodas();
        if (lista.isEmpty()) {
            System.out.println("[i] Nenhuma opcao de aposta cadastrada no banco de dados.");
            return;
        }

        System.out.println("========================================================================================================================");
        System.out.printf("%-7s | %-30s | %-6s | %-10s | %s%n",
                "ID", "Palpite / Selecao", "Odd", "Mercado", "Evento Esportivo");
        System.out.println("------------------------------------------------------------------------------------------------------------------------");
        for (OpcaoAposta op : lista) {
            String evDesc = (op.getMercado() != null && op.getMercado().getEvento() != null && op.getMercado().getEvento().getDescricao() != null)
                    ? op.getMercado().getEvento().getDescricao()
                    : "Evento N/A";
            int mercId = (op.getMercado() != null) ? op.getMercado().getCodmercado() : 0;
            String mercStr = (mercId > 0) ? String.format("(ID %d)", mercId) : "N/A";

            System.out.printf("ID: %-3d | %-30s | %-6.2f | %-10s | %s%n",
                    op.getCodopcao(), op.getResultado(), op.getOdd(), mercStr, evDesc);
        }
        System.out.println("========================================================================================================================");
    }

    private void consultarPorId(Scanner scanner) {
        int id = ConsoleUtils.lerInteiro(scanner, "\nInforme o ID da opcao: ");
        OpcaoAposta op = opcaoService.buscarPorId(id);
        String evDesc = (op.getMercado() != null && op.getMercado().getEvento() != null && op.getMercado().getEvento().getDescricao() != null)
                ? op.getMercado().getEvento().getDescricao()
                : "Evento N/A";
        String mercTipo = (op.getMercado() != null) ? op.getMercado().getTipo() : "N/A";

        System.out.println("\n--- [ DETALHES DA OPCAO #" + id + " ] ---");
        System.out.printf("  * ID: %d%n", op.getCodopcao());
        System.out.printf("  * Palpite / Selecao: %s%n", op.getResultado());
        System.out.printf("  * Cotacao Odd: %.2f%n", op.getOdd());
        System.out.printf("  * Mercado: %s%n", mercTipo);
        System.out.printf("  * Evento Esportivo: %s%n", evDesc);
    }

    private void remover(Scanner scanner) {
        int id = ConsoleUtils.lerInteiro(scanner, "\nInforme o ID da opcao a remover: ");
        OpcaoAposta op = opcaoService.buscarPorId(id);

        boolean conf = ConsoleUtils.lerConfirmacao(scanner, "Tem certeza que deseja excluir a opcao '" + op.getResultado() + "'? (S/N): ");
        if (conf) {
            opcaoService.deletar(id);
            System.out.println("[OK] Opcao de aposta ID " + id + " removida com sucesso!");
        } else {
            System.out.println("[i] Remocao cancelada.");
        }
    }
}
