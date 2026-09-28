package br.com.apostas.api.cli;

import br.com.apostas.api.model.Competicao;
import br.com.apostas.api.model.Esporte;
import br.com.apostas.api.service.CompeticaoService;
import br.com.apostas.api.util.ConsoleUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

/**
 * Interface de console para gerenciamento de competições esportivas.
 */
@Component
@RequiredArgsConstructor
public class CompeticaoCLI {

    private final CompeticaoService competicaoService;

    // Exibe as opções interativas no console e processa a entrada do usuário
    public void exibirMenu(Scanner scanner) {
        int opcao = -1;
        do {
            System.out.println("\n========================================================");
            System.out.println("              GERENCIAMENTO DE COMPETICOES              ");
            System.out.println("========================================================");
            System.out.println(" 1 - Cadastrar nova competicao");
            System.out.println(" 2 - Listar todas as competicoes");
            System.out.println(" 3 - Consultar competicao por ID");
            System.out.println(" 4 - Remover competicao");
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
        System.out.println("\n--- [ NOVA COMPETICAO ] ---");
        String nome = ConsoleUtils.lerTextoObrigatorio(scanner, "Nome da Competicao: ");
        String pais = ConsoleUtils.lerTextoObrigatorio(scanner, "Pais / Regiao: ");
        LocalDate dtInicio = ConsoleUtils.lerData(scanner, "Data de Inicio (AAAA-MM-DD ou DD/MM/AAAA): ", true);
        LocalDate dtFim = ConsoleUtils.lerData(scanner, "Data de Termino (deixe em branco se nao finalizada): ", false);
        int codesporte = ConsoleUtils.lerInteiro(scanner, "ID do Esporte vinculado (codesporte): ");

        Competicao nova = Competicao.builder()
                .nome(nome)
                .pais(pais)
                .dataInicio(dtInicio)
                .dataFim(dtFim)
                .esporte(Esporte.builder().codesporte(codesporte).build())
                .build();

        Competicao criada = competicaoService.criar(nova);
        System.out.println("\n[OK] Competicao cadastrada com sucesso! ID: " + criada.getCodcompeticao() + " | Nome: " + criada.getNome());
    }

    private void listar() {
        System.out.println("\n--- [ LISTAGEM DE COMPETICOES ] ---");
        List<Competicao> lista = competicaoService.listarTodas();
        if (lista.isEmpty()) {
            System.out.println("[i] Nenhuma competicao cadastrada no banco de dados.");
            return;
        }
        for (Competicao c : lista) {
            System.out.printf("ID: %-3d | Nome: %-25s | Esporte: %-15s | Inicio: %s | Fim: %s%n",
                    c.getCodcompeticao(), c.getNome(), c.getEsporte().getNome(), c.getDataInicio(), c.getDataFim());
        }
    }

    private void consultarPorId(Scanner scanner) {
        int id = ConsoleUtils.lerInteiro(scanner, "\nInforme o ID da competicao: ");
        Competicao c = competicaoService.buscarPorId(id);
        System.out.printf("[OK] ID: %-3d | Nome: %s | Esporte: %s | Pais: %s%n",
                c.getCodcompeticao(), c.getNome(), c.getEsporte().getNome(), c.getPais());
    }

    private void remover(Scanner scanner) {
        int id = ConsoleUtils.lerInteiro(scanner, "\nInforme o ID da competicao a remover: ");
        Competicao c = competicaoService.buscarPorId(id);

        boolean conf = ConsoleUtils.lerConfirmacao(scanner, "Tem certeza que deseja excluir a competicao '" + c.getNome() + "'? (S/N): ");
        if (conf) {
            competicaoService.deletar(id);
            System.out.println("[OK] Competicao ID " + id + " removida com sucesso!");
        } else {
            System.out.println("[i] Remocao cancelada.");
        }
    }
}
