package br.com.apostas.api.cli;

import br.com.apostas.api.model.Esporte;
import br.com.apostas.api.service.EsporteService;
import br.com.apostas.api.util.ConsoleUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

/**
 * Interface de console para gerenciamento das modalidades esportivas.
 */
@Component
@RequiredArgsConstructor
public class EsporteCLI {

    private final EsporteService esporteService;

    // Exibe as opções interativas no console e processa a entrada do usuário
    public void exibirMenu(Scanner scanner) {
        int opcao = -1;
        do {
            System.out.println("\n========================================================");
            System.out.println("               GERENCIAMENTO DE ESPORTES                ");
            System.out.println("========================================================");
            System.out.println(" 1 - Cadastrar novo esporte");
            System.out.println(" 2 - Listar todos os esportes");
            System.out.println(" 3 - Consultar esporte por ID");
            System.out.println(" 4 - Atualizar dados do esporte");
            System.out.println(" 5 - Remover um esporte");
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
        System.out.println("\n--- [ NOVO CADASTRO DE ESPORTE ] ---");
        String nome = ConsoleUtils.lerTextoObrigatorio(scanner, "Nome da modalidade esportiva: ");
        int max = lerMaxCompetidores(scanner, "Numero maximo de competidores por evento [padrao 2]: ", 2);

        Esporte novo = Esporte.builder()
                .nome(nome)
                .maxCompetidoresEvento(max)
                .build();
        Esporte criado = esporteService.criar(novo);
        System.out.printf("\n[OK] Esporte cadastrado com sucesso! ID: %d | Nome: %s | Max Competidores/Evento: %d%n",
                criado.getCodesporte(), criado.getNome(), criado.getMaxCompetidoresEvento());
    }

    private void listar() {
        System.out.println("\n--- [ LISTAGEM DE ESPORTES ] ---");
        List<Esporte> esportes = esporteService.listarTodos();
        if (esportes.isEmpty()) {
            System.out.println("[i] Nenhum esporte cadastrado no banco de dados.");
            return;
        }

        System.out.println("========================================================================");
        System.out.printf("%-7s | %-32s | %s%n", "ID", "Nome do Esporte", "Max Competidores / Evento");
        System.out.println("------------------------------------------------------------------------");
        for (Esporte e : esportes) {
            int max = (e.getMaxCompetidoresEvento() != null) ? e.getMaxCompetidoresEvento() : 2;
            System.out.printf("ID: %-3d | %-32s | %d%n", e.getCodesporte(), e.getNome(), max);
        }
        System.out.println("========================================================================");
    }

    private void consultarPorId(Scanner scanner) {
        int id = ConsoleUtils.lerInteiro(scanner, "\nInforme o ID do esporte: ");
        Esporte e = esporteService.buscarPorId(id);
        int max = (e.getMaxCompetidoresEvento() != null) ? e.getMaxCompetidoresEvento() : 2;
        System.out.printf("[OK] ID: %d | Nome: %s | Max Competidores / Evento: %d%n",
                e.getCodesporte(), e.getNome(), max);
    }

    private void atualizar(Scanner scanner) {
        System.out.println("\n--- [ ATUALIZACAO DE ESPORTE ] ---");
        int id = ConsoleUtils.lerInteiro(scanner, "Informe o ID do esporte a atualizar: ");
        Esporte e = esporteService.buscarPorId(id);

        int maxAtual = (e.getMaxCompetidoresEvento() != null) ? e.getMaxCompetidoresEvento() : 2;
        String nome = ConsoleUtils.lerTextoOpcional(scanner, "Novo Nome [" + e.getNome() + "]: ", e.getNome());
        int max = lerMaxCompetidores(scanner, "Novo Max Competidores / Evento [" + maxAtual + "]: ", maxAtual);

        Esporte atualizado = esporteService.atualizar(id, Esporte.builder()
                .nome(nome)
                .maxCompetidoresEvento(max)
                .build());
        System.out.printf("[OK] Esporte atualizado com sucesso! ID: %d | Nome: %s | Max Competidores/Evento: %d%n",
                atualizado.getCodesporte(), atualizado.getNome(), atualizado.getMaxCompetidoresEvento());
    }

    private void remover(Scanner scanner) {
        int id = ConsoleUtils.lerInteiro(scanner, "\nInforme o ID do esporte a remover: ");
        Esporte e = esporteService.buscarPorId(id);

        boolean conf = ConsoleUtils.lerConfirmacao(scanner, "Tem certeza que deseja excluir o esporte '" + e.getNome() + "'? (S/N): ");
        if (conf) {
            esporteService.deletar(id);
            System.out.println("[OK] Esporte ID " + id + " removido com sucesso!");
        } else {
            System.out.println("[i] Remocao cancelada.");
        }
    }

    private int lerMaxCompetidores(Scanner scanner, String prompt, int valorPadrao) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                return valorPadrao;
            }
            try {
                int val = Integer.parseInt(input);
                if (val >= 2) {
                    return val;
                }
                System.out.println("[!] O numero maximo de competidores por evento deve ser no minimo 2.");
            } catch (NumberFormatException e) {
                System.out.println("[!] Entrada invalida! Digite um numero inteiro valido (minimo 2).");
            }
        }
    }
}
