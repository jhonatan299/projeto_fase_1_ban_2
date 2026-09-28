package br.com.apostas.api.cli;

import br.com.apostas.api.model.Competidor;
import br.com.apostas.api.service.CompetidorService;
import br.com.apostas.api.util.ConsoleUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

/**
 * Interface de console para cadastro e consulta de competidores.
 */
@Component
@RequiredArgsConstructor
public class CompetidorCLI {

    private final CompetidorService competidorService;

    // Exibe as opções interativas no console e processa a entrada do usuário
    public void exibirMenu(Scanner scanner) {
        int opcao = -1;
        do {
            System.out.println("\n========================================================");
            System.out.println("              GERENCIAMENTO DE COMPETIDORES             ");
            System.out.println("========================================================");
            System.out.println(" 1 - Cadastrar novo competidor");
            System.out.println(" 2 - Listar todos os competidores");
            System.out.println(" 3 - Consultar competidor por ID");
            System.out.println(" 4 - Remover competidor");
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
        System.out.println("\n--- [ NOVO COMPETIDOR ] ---");
        String nome = ConsoleUtils.lerTextoObrigatorio(scanner, "Nome do Competidor / Time: ");
        String tipo = lerTipoCompetidor(scanner);

        Competidor c = competidorService.criar(Competidor.builder().nome(nome).tipo(tipo).build());
        System.out.println("\n[OK] Competidor cadastrado com sucesso! ID: " + c.getCodcompetidor() + " | Nome: " + c.getNome());
    }

    private void listar() {
        System.out.println("\n--- [ LISTAGEM DE COMPETIDORES ] ---");
        List<Competidor> lista = competidorService.listarTodos();
        if (lista.isEmpty()) {
            System.out.println("[i] Nenhum competidor cadastrado no banco de dados.");
            return;
        }
        for (Competidor c : lista) {
            System.out.printf("ID: %-3d | Nome: %-25s | Tipo: %-12s%n", c.getCodcompetidor(), c.getNome(), c.getTipo());
        }
    }

    private void consultarPorId(Scanner scanner) {
        int id = ConsoleUtils.lerInteiro(scanner, "\nInforme o ID do competidor: ");
        Competidor c = competidorService.buscarPorId(id);
        System.out.printf("[OK] ID: %-3d | Nome: %s | Tipo: %s%n", c.getCodcompetidor(), c.getNome(), c.getTipo());
    }

    private void remover(Scanner scanner) {
        int id = ConsoleUtils.lerInteiro(scanner, "\nInforme o ID do competidor a remover: ");
        Competidor c = competidorService.buscarPorId(id);

        boolean conf = ConsoleUtils.lerConfirmacao(scanner, "Tem certeza que deseja excluir o competidor '" + c.getNome() + "'? (S/N): ");
        if (conf) {
            competidorService.deletar(id);
            System.out.println("[OK] Competidor ID " + id + " removido com sucesso!");
        } else {
            System.out.println("[i] Remocao cancelada.");
        }
    }

    private String lerTipoCompetidor(Scanner scanner) {
        System.out.println("Tipo de Competidor:");
        System.out.println("  1 - TIME (Equipe / Clube)");
        System.out.println("  2 - INDIVIDUAL (Atleta / Piloto)");
        System.out.print("Escolha o tipo (1/2) [Padrao: 1 - TIME]: ");
        String input = scanner.nextLine().trim();
        if (input.isEmpty() || input.equals("1") || input.equalsIgnoreCase("TIME")) {
            return "TIME";
        }
        if (input.equals("2") || input.equalsIgnoreCase("INDIVIDUAL")) {
            return "INDIVIDUAL";
        }
        System.out.println("[!] Opcao invalida ('" + input + "'). Definindo como TIME.");
        return "TIME";
    }
}
