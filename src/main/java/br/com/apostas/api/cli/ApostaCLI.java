package br.com.apostas.api.cli;

import br.com.apostas.api.model.Aposta;
import br.com.apostas.api.model.ItemAposta;
import br.com.apostas.api.service.ApostaService;
import br.com.apostas.api.util.ConsoleUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Interface de console para consultas e listagens de bilhetes de aposta.
 */
@Component
@RequiredArgsConstructor
public class ApostaCLI {

    private static final DateTimeFormatter FMT_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final ApostaService apostaService;

    // Exibe as opções interativas no console e processa a entrada do usuário
    public void exibirMenu(Scanner scanner) {
        int opcao = -1;
        do {
            System.out.println("\n========================================================");
            System.out.println("              ADMINISTRACAO DE APOSTAS                  ");
            System.out.println("========================================================");
            System.out.println(" 1 - Listar todas as apostas");
            System.out.println(" 2 - Consultar bilhete por ID");
            System.out.println(" 3 - Cancelar aposta com estorno");
            System.out.println(" 4 - Remover aposta");
            System.out.println(" 0 - Voltar ao menu principal");
            System.out.println("========================================================");

            try {
                opcao = ConsoleUtils.lerInteiro(scanner, "Escolha uma opcao: ");

                switch (opcao) {
                    case 1 -> listar();
                    case 2 -> consultar(scanner);
                    case 3 -> cancelar(scanner);
                    case 4 -> remover(scanner);
                    case 0 -> System.out.println("Retornando ao menu principal...");
                    default -> System.out.println("[!] Opcao invalida! Escolha um valor entre 0 e 4.");
                }
            } catch (Exception e) {
                System.out.println("\n[X] ERRO: " + e.getMessage());
            }
        } while (opcao != 0);
    }

    private void listar() {
        System.out.println("\n--- [ LISTAGEM DE APOSTAS ] ---");
        List<Aposta> lista = apostaService.listarTodas();
        if (lista.isEmpty()) {
            System.out.println("[i] Nenhuma aposta cadastrada no banco de dados.");
            return;
        }

        System.out.println("==================================================================================================================================================================================");
        System.out.printf("%-5s | %-16s | %-10s | %-10s | %-11s | %-20s | %s%n",
                "ID", "Data/Hora", "Status", "Valor", "Retorno", "Apostador", "Evento(s) Esportivo(s)");
        System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

        for (Aposta a : lista) {
            String retStr = (a.getValorRetorno() != null) ? String.format("R$ %.2f", a.getValorRetorno()) : "Pendente";
            String dataStr = (a.getDataHora() != null) ? a.getDataHora().format(FMT_DATA) : "-";

            List<ItemAposta> itens = apostaService.buscarItensDaAposta(a.getCodaposta());
            List<String> eventos = new ArrayList<>();
            for (ItemAposta item : itens) {
                if (item.getOpcao() != null && item.getOpcao().getMercado() != null && item.getOpcao().getMercado().getEvento() != null) {
                    String ev = (item.getOpcao().getMercado().getEvento().getDescricao() != null)
                            ? item.getOpcao().getMercado().getEvento().getDescricao()
                            : "Evento #" + item.getOpcao().getMercado().getEvento().getCodevento();
                    if (!eventos.contains(ev)) {
                        eventos.add(ev);
                    }
                }
            }
            String resumoEventos = eventos.isEmpty() ? "Sem evento vinculado" : String.join(" + ", eventos);

            System.out.printf("#%-4d | %-16s | %-10s | R$ %-7.2f | %-11s | %-20s | %s%n",
                    a.getCodaposta(), dataStr, a.getStatus(), a.getValor(), retStr, a.getUsuario().getNome(), resumoEventos);
        }
        System.out.println("==================================================================================================================================================================================");
    }

    private void consultar(Scanner scanner) {
        int id = ConsoleUtils.lerInteiro(scanner, "\nInforme o ID da aposta: ");
        Aposta a = apostaService.buscarPorId(id);
        List<ItemAposta> itens = apostaService.buscarItensDaAposta(id);

        String retStr = (a.getValorRetorno() != null) ? String.format("R$ %.2f", a.getValorRetorno()) : "Pendente";
        String dataStr = (a.getDataHora() != null) ? a.getDataHora().format(FMT_DATA) : "-";

        System.out.println("\n================================================ BILHETE DE APOSTA #" + id + " ================================================");
        System.out.printf("ID: %-4d | Data: %s | Status: %-10s | Valor: R$ %-7.2f | Retorno: %-10s | Apostador: %s%n",
                a.getCodaposta(), dataStr, a.getStatus(), a.getValor(), retStr, a.getUsuario().getNome());
        System.out.println("--------------------------------------------------------------------------------------------------------------------------------");
        System.out.println("Itens / Palpites vinculados:");
        for (ItemAposta item : itens) {
            String evDesc = item.getOpcao().getMercado() != null && item.getOpcao().getMercado().getEvento() != null
                    ? (item.getOpcao().getMercado().getEvento().getDescricao() != null ? item.getOpcao().getMercado().getEvento().getDescricao() : "Evento #" + item.getOpcao().getMercado().getEvento().getCodevento())
                    : "N/A";
            String mercTipo = item.getOpcao().getMercado() != null ? item.getOpcao().getMercado().getTipo() : "N/A";

            System.out.printf("  * Item ID: %-3d | Evento: %-35s | Mercado: %-18s | Palpite: %-18s | Odd: %-6.2f | Status: %s%n",
                    item.getCoditem(), evDesc, mercTipo, item.getOpcao().getResultado(), item.getOddcadastrada(), item.getResultado());
        }
        System.out.println("================================================================================================================================");
    }

    private void cancelar(Scanner scanner) {
        int id = ConsoleUtils.lerInteiro(scanner, "\nInforme o ID da aposta a cancelar com estorno: ");
        boolean conf = ConsoleUtils.lerConfirmacao(scanner, "Tem certeza que deseja cancelar e estornar a aposta #" + id + "? (S/N): ");
        if (conf) {
            Aposta a = apostaService.liquidarAposta(id, "CANCELADA");
            System.out.printf("[OK] Aposta ID %d cancelada com sucesso! Valor estornado: R$ %.2f%n", id, a.getValor());
        } else {
            System.out.println("[i] Operacao cancelada.");
        }
    }

    private void remover(Scanner scanner) {
        int id = ConsoleUtils.lerInteiro(scanner, "\nInforme o ID da aposta a remover: ");
        boolean conf = ConsoleUtils.lerConfirmacao(scanner, "Tem certeza que deseja excluir permanentemente a aposta #" + id + "? (S/N): ");
        if (conf) {
            apostaService.deletar(id);
            System.out.println("[OK] Aposta removida com sucesso!");
        } else {
            System.out.println("[i] Remocao cancelada.");
        }
    }
}
