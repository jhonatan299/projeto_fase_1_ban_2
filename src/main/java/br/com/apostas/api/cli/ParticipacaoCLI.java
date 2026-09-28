package br.com.apostas.api.cli;

import br.com.apostas.api.dto.ParticipacaoRequestDTO;
import br.com.apostas.api.model.Competidor;
import br.com.apostas.api.model.EventoEsportivo;
import br.com.apostas.api.model.Participacao;
import br.com.apostas.api.service.CompetidorService;
import br.com.apostas.api.service.EventoEsportivoService;
import br.com.apostas.api.service.ParticipacaoService;
import br.com.apostas.api.util.ConsoleUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Interface de console para associação de competidores a eventos esportivos.
 */
@Component
@RequiredArgsConstructor
public class ParticipacaoCLI {

    private static final DateTimeFormatter FMT_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final ParticipacaoService participacaoService;
    private final EventoEsportivoService eventoService;
    private final CompetidorService competidorService;

    // Exibe as opções interativas no console e processa a entrada do usuário
    public void exibirMenu(Scanner scanner) {
        int opcao = -1;
        do {
            System.out.println("\n========================================================");
            System.out.println("      GESTAO DE PARTICIPACOES EM EVENTOS ESPORTIVOS     ");
            System.out.println("========================================================");
            System.out.println(" 1 - Registrar Competidor em um Evento");
            System.out.println(" 2 - Listar Competidores de um Evento");
            System.out.println(" 3 - Listar Eventos de um Competidor");
            System.out.println(" 4 - Listar todas as participacoes registradas");
            System.out.println(" 5 - Remover Competidor de um Evento");
            System.out.println(" 0 - Voltar ao menu principal");
            System.out.println("========================================================");

            try {
                opcao = ConsoleUtils.lerInteiro(scanner, "Escolha uma opcao: ");

                switch (opcao) {
                    case 1 -> registrar(scanner);
                    case 2 -> listarPorEvento(scanner);
                    case 3 -> listarPorCompetidor(scanner);
                    case 4 -> listarTodas();
                    case 5 -> remover(scanner);
                    case 0 -> System.out.println("Retornando ao menu principal...");
                    default -> System.out.println("[!] Opcao invalida! Escolha um valor entre 0 e 5.");
                }
            } catch (Exception e) {
                System.out.println("\n[X] ERRO: " + e.getMessage());
            }
        } while (opcao != 0);
    }

    private void registrar(Scanner scanner) {
        System.out.println("\n--- [ NOVO REGISTRO DE PARTICIPACAO (N:N) ] ---");

        List<EventoEsportivo> todosEventos = eventoService.listarTodos();
        if (todosEventos.isEmpty()) {
            System.out.println("[i] Nenhum evento esportivo cadastrado no banco de dados.");
            return;
        }

        List<EventoEsportivo> eventosDisponiveis = todosEventos.stream()
                .filter(e -> "AGENDADO".equalsIgnoreCase(e.getStatus()) || "AO_VIVO".equalsIgnoreCase(e.getStatus()))
                .toList();

        if (eventosDisponiveis.isEmpty()) {
            eventosDisponiveis = todosEventos;
        }

        System.out.println("\nEventos disponiveis:");
        for (EventoEsportivo e : eventosDisponiveis) {
            String dataStr = (e.getDataHora() != null) ? e.getDataHora().format(FMT_DATA) : "-";
            System.out.printf("  [%d] %s (%s) - %s%n", e.getCodevento(), e.getDescricao(), dataStr, e.getStatus());
        }

        int codevento = ConsoleUtils.lerInteiro(scanner, "\nEscolha o evento (ID): ");
        EventoEsportivo eventoEscolhido = eventosDisponiveis.stream()
                .filter(e -> e.getCodevento().equals(codevento))
                .findFirst()
                .orElse(null);

        if (eventoEscolhido == null) {
            System.out.printf("\n[X] ERRO: Evento com ID %d invalido ou nao disponivel! Escolha um dos eventos listados acima.%n", codevento);
            return;
        }

        List<Participacao> participacoesExistentes = participacaoService.listarPorEvento(codevento);
        Set<Integer> idsJaRegistrados = participacoesExistentes.stream()
                .map(p -> p.getCompetidor().getCodcompetidor())
                .collect(Collectors.toSet());

        List<Competidor> todosCompetidores = competidorService.listarTodos();
        List<Competidor> disponiveis = todosCompetidores.stream()
                .filter(c -> !idsJaRegistrados.contains(c.getCodcompetidor()))
                .toList();

        if (disponiveis.isEmpty()) {
            System.out.printf("\n[i] Todos os competidores cadastrados ja estao vinculados ao evento '%s'.%n", eventoEscolhido.getDescricao());
            return;
        }

        System.out.printf("\nCompetidores disponiveis para o evento '%s':%n", eventoEscolhido.getDescricao());
        for (Competidor c : disponiveis) {
            System.out.printf("  [%d] %s (%s)%n", c.getCodcompetidor(), c.getNome(), c.getTipo());
        }

        int codcompetidor = ConsoleUtils.lerInteiro(scanner, "\nEscolha o competidor (ID): ");
        Competidor competidorEscolhido = disponiveis.stream()
                .filter(c -> c.getCodcompetidor().equals(codcompetidor))
                .findFirst()
                .orElse(null);

        if (competidorEscolhido == null) {
            if (idsJaRegistrados.contains(codcompetidor)) {
                System.out.printf("\n[X] ERRO: O competidor com ID %d ja esta registrado neste evento!%n", codcompetidor);
            } else {
                System.out.printf("\n[X] ERRO: Competidor com ID %d invalido! Escolha um dos competidores disponiveis listados acima.%n", codcompetidor);
            }
            return;
        }

        ParticipacaoRequestDTO dto = new ParticipacaoRequestDTO();
        dto.setCodevento(codevento);
        dto.setCodcompetidor(codcompetidor);

        try {
            Participacao p = participacaoService.registrarParticipacao(dto);
            System.out.printf("\n[OK] Competidor '%s' registrado com sucesso no Evento ID %d!%n",
                    p.getCompetidor().getNome(), p.getEvento().getCodevento());
        } catch (Exception ex) {
            System.out.printf("\n[X] ERRO: %s%n", ex.getMessage());
        }
    }

    private void listarPorEvento(Scanner scanner) {
        List<EventoEsportivo> todosEventos = eventoService.listarTodos();
        if (todosEventos.isEmpty()) {
            System.out.println("[i] Nenhum evento cadastrado no banco de dados.");
            return;
        }

        System.out.println("\nEventos cadastrados:");
        for (EventoEsportivo e : todosEventos) {
            String dataStr = (e.getDataHora() != null) ? e.getDataHora().format(FMT_DATA) : "-";
            System.out.printf("  [%d] %s (%s) - %s%n", e.getCodevento(), e.getDescricao(), dataStr, e.getStatus());
        }

        int id = ConsoleUtils.lerInteiro(scanner, "\nInforme o ID do evento para consultar seus competidores: ");
        EventoEsportivo ev = todosEventos.stream()
                .filter(e -> e.getCodevento().equals(id))
                .findFirst()
                .orElse(null);

        if (ev == null) {
            System.out.printf("\n[X] ERRO: Evento com ID %d nao encontrado! Escolha um dos eventos listados acima.%n", id);
            return;
        }

        List<Participacao> lista = participacaoService.listarPorEvento(id);
        if (lista.isEmpty()) {
            System.out.printf("\n[i] Nenhum competidor vinculado ao evento '%s' (ID %d).%n", ev.getDescricao(), id);
            return;
        }
        System.out.printf("\nCompetidores no Evento ID %d (%s):%n", id, ev.getDescricao());
        for (Participacao p : lista) {
            System.out.printf("  * ID: %-3d | Nome: %-25s | Tipo: %s%n",
                    p.getCompetidor().getCodcompetidor(), p.getCompetidor().getNome(), p.getCompetidor().getTipo());
        }
    }

    private void listarPorCompetidor(Scanner scanner) {
        List<Competidor> todosCompetidores = competidorService.listarTodos();
        if (todosCompetidores.isEmpty()) {
            System.out.println("[i] Nenhum competidor cadastrado no banco de dados.");
            return;
        }

        System.out.println("\nCompetidores cadastrados:");
        for (Competidor c : todosCompetidores) {
            System.out.printf("  [%d] %s (%s)%n", c.getCodcompetidor(), c.getNome(), c.getTipo());
        }

        int id = ConsoleUtils.lerInteiro(scanner, "\nInforme o ID do competidor para consultar seus eventos: ");
        Competidor comp = todosCompetidores.stream()
                .filter(c -> c.getCodcompetidor().equals(id))
                .findFirst()
                .orElse(null);

        if (comp == null) {
            System.out.printf("\n[X] ERRO: Competidor com ID %d nao encontrado! Escolha um dos competidores listados acima.%n", id);
            return;
        }

        List<Participacao> lista = participacaoService.listarPorCompetidor(id);
        if (lista.isEmpty()) {
            System.out.printf("\n[i] O competidor '%s' (ID %d) nao possui eventos vinculados.%n", comp.getNome(), id);
            return;
        }
        System.out.printf("\nEventos do Competidor ID %d (%s):%n", id, comp.getNome());
        for (Participacao p : lista) {
            String dataStr = (p.getEvento().getDataHora() != null) ? p.getEvento().getDataHora().format(FMT_DATA) : "-";
            System.out.printf("  * Evento ID: %-3d | Descricao: %-35s | Data: %s | Status: %s%n",
                    p.getEvento().getCodevento(), p.getEvento().getDescricao(), dataStr, p.getEvento().getStatus());
        }
    }

    private void listarTodas() {
        System.out.println("\n--- [ TODAS AS PARTICIPACOES ] ---");
        List<Participacao> lista = participacaoService.listarTodas();
        if (lista.isEmpty()) {
            System.out.println("[i] Nenhuma participacao cadastrada no banco de dados.");
            return;
        }
        for (Participacao p : lista) {
            System.out.printf("Evento ID: %-3d (%s) <---> Competidor ID: %-3d (%s)%n",
                    p.getEvento().getCodevento(), p.getEvento().getCompeticao().getNome(),
                    p.getCompetidor().getCodcompetidor(), p.getCompetidor().getNome());
        }
    }

    private void remover(Scanner scanner) {
        List<EventoEsportivo> todosEventos = eventoService.listarTodos();
        if (todosEventos.isEmpty()) {
            System.out.println("[i] Nenhum evento cadastrado no banco de dados.");
            return;
        }

        System.out.println("\nEventos cadastrados:");
        for (EventoEsportivo e : todosEventos) {
            String dataStr = (e.getDataHora() != null) ? e.getDataHora().format(FMT_DATA) : "-";
            System.out.printf("  [%d] %s (%s) - %s%n", e.getCodevento(), e.getDescricao(), dataStr, e.getStatus());
        }

        int codevento = ConsoleUtils.lerInteiro(scanner, "\nEscolha o Evento (ID): ");
        EventoEsportivo ev = todosEventos.stream()
                .filter(e -> e.getCodevento().equals(codevento))
                .findFirst()
                .orElse(null);

        if (ev == null) {
            System.out.printf("\n[X] ERRO: Evento com ID %d nao encontrado! Escolha um dos eventos listados acima.%n", codevento);
            return;
        }

        List<Participacao> lista = participacaoService.listarPorEvento(codevento);
        if (lista.isEmpty()) {
            System.out.printf("\n[i] O evento '%s' (ID %d) nao possui competidores vinculados.%n", ev.getDescricao(), codevento);
            return;
        }

        System.out.printf("\nCompetidores vinculados ao Evento ID %d (%s):%n", codevento, ev.getDescricao());
        for (Participacao p : lista) {
            System.out.printf("  [%d] %s (%s)%n",
                    p.getCompetidor().getCodcompetidor(), p.getCompetidor().getNome(), p.getCompetidor().getTipo());
        }

        int codcompetidor = ConsoleUtils.lerInteiro(scanner, "\nEscolha o Competidor a remover (ID): ");
        Participacao part = lista.stream()
                .filter(p -> p.getCompetidor().getCodcompetidor().equals(codcompetidor))
                .findFirst()
                .orElse(null);

        if (part == null) {
            System.out.printf("\n[X] ERRO: Competidor com ID %d nao esta vinculado ao evento '%s'!%n", codcompetidor, ev.getDescricao());
            return;
        }

        participacaoService.removerParticipacao(codevento, codcompetidor);
        System.out.printf("\n[OK] Participacao do competidor '%s' no evento '%s' removida com sucesso!%n",
                part.getCompetidor().getNome(), ev.getDescricao());
    }
}
