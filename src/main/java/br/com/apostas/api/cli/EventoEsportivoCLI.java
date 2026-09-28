package br.com.apostas.api.cli;

import br.com.apostas.api.model.Competicao;
import br.com.apostas.api.model.EventoEsportivo;
import br.com.apostas.api.service.EventoEsportivoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

/**
 * Interface de console para gerenciamento de eventos esportivos e partidas.
 */
@Component
@RequiredArgsConstructor
public class EventoEsportivoCLI {

    private final EventoEsportivoService eventoService;

    // Exibe as opções interativas no console e processa a entrada do usuário
    public void exibirMenu(Scanner scanner) {
        int opcao = -1;
        do {
            System.out.println("\n========================================================");
            System.out.println("            GERENCIAMENTO DE EVENTOS ESPORTIVOS         ");
            System.out.println("========================================================");
            System.out.println(" 1 - Cadastrar novo evento esportivo");
            System.out.println(" 2 - Listar todos os eventos");
            System.out.println(" 3 - Consultar evento por ID");
            System.out.println(" 4 - Remover evento");
            System.out.println(" 0 - Voltar ao menu principal");
            System.out.println("========================================================");
            System.out.print("Escolha uma opcao: ");

            try {
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) continue;
                opcao = Integer.parseInt(input);

                switch (opcao) {
                    case 1 -> cadastrar(scanner);
                    case 2 -> listar();
                    case 3 -> consultarPorId(scanner);
                    case 4 -> remover(scanner);
                    case 0 -> System.out.println("Retornando ao menu principal...");
                    default -> System.out.println("[!] Opcao invalida!");
                }
            } catch (NumberFormatException e) {
                System.out.println("[!] Digite apenas numeros inteiros.");
            } catch (Exception e) {
                System.out.println("\n[X] ERRO: " + e.getMessage());
            }
        } while (opcao != 0);
    }

    private void cadastrar(Scanner scanner) {
        System.out.println("\n--- [ NOVO EVENTO ESPORTIVO ] ---");
        System.out.print("Descricao do Evento (ex: Grand Prix de Monaco, Rodada 32 - Real Madrid x Barca): ");
        String descricao = scanner.nextLine().trim();

        System.out.print("Data e Hora (AAAA-MM-DD HH:MM): ");
        String dtStr = scanner.nextLine().trim();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dtHora = LocalDateTime.parse(dtStr, dtf);

        System.out.print("Status (AGENDADO, AO_VIVO, FINALIZADO, CANCELADO) [Padrao: AGENDADO]: ");
        String status = scanner.nextLine().trim();
        if (status.isEmpty()) status = "AGENDADO";

        System.out.print("ID da Competicao vinculada (codcompeticao): ");
        int codcompeticao = Integer.parseInt(scanner.nextLine().trim());

        EventoEsportivo ev = EventoEsportivo.builder()
                .descricao(descricao.isEmpty() ? null : descricao)
                .dataHora(dtHora)
                .status(status)
                .competicao(Competicao.builder().codcompeticao(codcompeticao).build())
                .build();

        EventoEsportivo criado = eventoService.criar(ev);
        System.out.println("[OK] Evento esportivo cadastrado com sucesso! ID: " + criado.getCodevento());
    }

    private void listar() {
        System.out.println("\n--- [ LISTAGEM DE EVENTOS ESPORTIVOS ] ---");
        List<EventoEsportivo> lista = eventoService.listarTodos();
        for (EventoEsportivo ev : lista) {
            String desc = ev.getDescricao() != null ? ev.getDescricao() : "Sem descricao";
            System.out.printf("ID: %-3d | Descricao: %-36s | Data: %s | Status: %-10s | Competicao: %s%n",
                    ev.getCodevento(), desc, ev.getDataHora(), ev.getStatus(), ev.getCompeticao().getNome());
        }
    }

    private void consultarPorId(Scanner scanner) {
        System.out.print("\nInforme o ID do evento: ");
        int id = Integer.parseInt(scanner.nextLine().trim());
        EventoEsportivo ev = eventoService.buscarPorId(id);
        String desc = ev.getDescricao() != null ? ev.getDescricao() : "Sem descricao";
        System.out.printf("[OK] ID: %-3d | Descricao: %s | Data: %s | Status: %s | Competicao: %s%n",
                ev.getCodevento(), desc, ev.getDataHora(), ev.getStatus(), ev.getCompeticao().getNome());
    }

    private void remover(Scanner scanner) {
        System.out.print("\nInforme o ID do evento a remover: ");
        int id = Integer.parseInt(scanner.nextLine().trim());
        eventoService.deletar(id);
        System.out.println("[OK] Evento esportivo removido com sucesso!");
    }
}
