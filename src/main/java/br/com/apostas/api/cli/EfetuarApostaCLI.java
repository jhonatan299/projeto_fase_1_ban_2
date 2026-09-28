package br.com.apostas.api.cli;

import br.com.apostas.api.model.Aposta;
import br.com.apostas.api.model.EventoEsportivo;
import br.com.apostas.api.model.ItemAposta;
import br.com.apostas.api.model.Mercado;
import br.com.apostas.api.model.OpcaoAposta;
import br.com.apostas.api.model.Usuario;
import br.com.apostas.api.service.ApostaService;
import br.com.apostas.api.service.EventoEsportivoService;
import br.com.apostas.api.service.MercadoService;
import br.com.apostas.api.service.OpcaoApostaService;
import br.com.apostas.api.service.UsuarioService;
import br.com.apostas.api.util.ConsoleUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Fluxo transacional interativo no console para realização e liquidação de apostas.
 */
@Component
@RequiredArgsConstructor
public class EfetuarApostaCLI {

    private static final DateTimeFormatter FMT_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final ApostaService apostaService;
    private final OpcaoApostaService opcaoService;
    private final EventoEsportivoService eventoService;
    private final MercadoService mercadoService;
    private final UsuarioService usuarioService;

    // Exibe as opções interativas no console e processa a entrada do usuário
    public void exibirMenu(Scanner scanner) {
        int opcao = -1;
        do {
            System.out.println("\n========================================================");
            System.out.println("     SISTEMA TRANSACIONAL - EFETUAR & LIQUIDAR APOSTAS  ");
            System.out.println("========================================================");
            System.out.println(" 1 - Realizar Nova Aposta (Simples ou Multipla / ACID)");
            System.out.println(" 2 - Consultar Bilhete / Itens de uma Aposta");
            System.out.println(" 3 - Liquidar Aposta (Marcar como GANHA ou PERDIDA)");
            System.out.println(" 0 - Voltar ao menu principal");
            System.out.println("========================================================");

            try {
                opcao = ConsoleUtils.lerInteiro(scanner, "Escolha uma opcao: ");

                switch (opcao) {
                    case 1 -> realizarAposta(scanner);
                    case 2 -> consultarBilhete(scanner);
                    case 3 -> liquidar(scanner);
                    case 0 -> System.out.println("Retornando ao menu principal...");
                    default -> System.out.println("[!] Opcao invalida! Escolha um valor entre 0 e 3.");
                }
            } catch (Exception e) {
                System.out.println("\n[X] ERRO: " + e.getMessage());
            }
        } while (opcao != 0);
    }

    private void realizarAposta(Scanner scanner) {
        System.out.println("\n--- [ NOVA APOSTA (TRANSACAO ATOMICA) ] ---");
        int codusuario = ConsoleUtils.lerInteiro(scanner, "Informe o ID do Usuario apostador (codusuario): ");

        Usuario usuario;
        try {
            usuario = usuarioService.buscarPorId(codusuario);
        } catch (Exception ex) {
            System.out.printf("\n[X] ERRO: Usuario com ID %d nao encontrado no banco de dados.%n", codusuario);
            return;
        }

        if (!"ATIVO".equalsIgnoreCase(usuario.getStatus())) {
            System.out.printf("\n[X] ERRO: O usuario '%s' (ID %d) nao esta ATIVO (Status atual: %s). Apostas bloqueadas.%n",
                    usuario.getNome(), codusuario, usuario.getStatus());
            return;
        }

        System.out.printf("  [i] Apostador identificado: %s (Status: %s)%n", usuario.getNome(), usuario.getStatus());

        BigDecimal valor = ConsoleUtils.lerBigDecimal(scanner, "\nInforme o Valor a apostar (R$): ", new BigDecimal("0.01"));

        List<Integer> codigos = new ArrayList<>();
        List<OpcaoAposta> opcoesEscolhidas = new ArrayList<>();
        boolean continuarAdicionando = true;
        int selecaoNum = 1;

        while (continuarAdicionando) {
            System.out.println("\n--------------------------------------------------------");
            System.out.printf("  SELECAO #%d (Passo a Passo: Evento -> Mercado -> Opcao)%n", selecaoNum);
            System.out.println("--------------------------------------------------------");

            List<EventoEsportivo> todosEventos = eventoService.listarTodos();
            List<EventoEsportivo> eventosDisponiveis = todosEventos.stream()
                    .filter(e -> "AGENDADO".equalsIgnoreCase(e.getStatus()) || "AO_VIVO".equalsIgnoreCase(e.getStatus()))
                    .toList();

            if (eventosDisponiveis.isEmpty()) {
                eventosDisponiveis = todosEventos;
            }

            if (eventosDisponiveis.isEmpty()) {
                System.out.println("[!] Nenhum evento esportivo disponivel para aposta.");
                return;
            }

            System.out.println("Eventos disponiveis para aposta:");
            for (EventoEsportivo e : eventosDisponiveis) {
                String dataStr = (e.getDataHora() != null) ? e.getDataHora().format(FMT_DATA) : "-";
                System.out.printf("  [%d] %s (%s) - %s%n", e.getCodevento(), e.getDescricao(), dataStr, e.getStatus());
            }

            int codevento = ConsoleUtils.lerInteiro(scanner, "\nEscolha o Evento (ID): ");
            EventoEsportivo eventoEscolhido = eventosDisponiveis.stream()
                    .filter(e -> e.getCodevento().equals(codevento))
                    .findFirst()
                    .orElse(null);

            if (eventoEscolhido == null) {
                System.out.printf("\n[X] ERRO: Evento com ID %d invalido ou indisponivel! Escolha um dos IDs listados acima.%n", codevento);
                continue;
            }

            List<Mercado> mercadosAbertos = mercadoService.listarPorEvento(codevento).stream()
                    .filter(m -> "ABERTO".equalsIgnoreCase(m.getStatus()))
                    .toList();

            if (mercadosAbertos.isEmpty()) {
                System.out.printf("\n[!] Nao ha mercados abertos para o evento selecionado '%s'.%n", eventoEscolhido.getDescricao());
                continue;
            }

            System.out.printf("\nMercados abertos para o evento '%s':%n", eventoEscolhido.getDescricao());
            for (Mercado m : mercadosAbertos) {
                System.out.printf("  [%d] %s%n", m.getCodmercado(), m.getTipo());
            }

            int codmercado = ConsoleUtils.lerInteiro(scanner, "\nEscolha o Mercado (ID): ");
            Mercado mercadoEscolhido = mercadosAbertos.stream()
                    .filter(m -> m.getCodmercado().equals(codmercado))
                    .findFirst()
                    .orElse(null);

            if (mercadoEscolhido == null) {
                System.out.printf("\n[X] ERRO: Mercado com ID %d invalido para este evento! Escolha um dos mercados abertos listados acima.%n", codmercado);
                continue;
            }

            List<OpcaoAposta> opcoesMercado = opcaoService.listarPorMercado(codmercado);
            if (opcoesMercado.isEmpty()) {
                System.out.printf("\n[!] Nenhuma opcao de aposta cadastrada para o mercado '%s'.%n", mercadoEscolhido.getTipo());
                continue;
            }

            System.out.printf("\nOpcoes de palpite disponiveis para o mercado '%s':%n", mercadoEscolhido.getTipo());
            for (OpcaoAposta op : opcoesMercado) {
                System.out.printf("  [%d] %-25s | Odd: %.2f%n", op.getCodopcao(), op.getResultado(), op.getOdd());
            }

            int codopcao = ConsoleUtils.lerInteiro(scanner, "\nEscolha a Opcao de Aposta (ID): ");
            OpcaoAposta opEscolhida = opcoesMercado.stream()
                    .filter(op -> op.getCodopcao().equals(codopcao))
                    .findFirst()
                    .orElse(null);

            if (opEscolhida == null) {
                System.out.printf("\n[X] ERRO: Opcao com ID %d invalida para este mercado! Escolha um dos palpites listados acima.%n", codopcao);
                continue;
            }

            if (codigos.contains(codopcao)) {
                System.out.println("\n[!] Opcao ja adicionada neste mesmo bilhete!");
                continue;
            }

            System.out.printf("\n  [OK] Selecao #%d adicionada: %s (Odd: %.2f | Mercado: %s | Evento: %s)%n",
                    selecaoNum, opEscolhida.getResultado(), opEscolhida.getOdd(), mercadoEscolhido.getTipo(), eventoEscolhido.getDescricao());

            codigos.add(codopcao);
            opcoesEscolhidas.add(opEscolhida);
            selecaoNum++;

            continuarAdicionando = ConsoleUtils.lerConfirmacao(scanner, "\nDeseja adicionar mais um palpite neste bilhete (Aposta Multipla)? (S/N): ");
        }

        if (codigos.isEmpty()) {
            System.out.println("[i] Nenhuma selecao adicionada. Operacao cancelada.");
            return;
        }

        BigDecimal oddAcumulada = BigDecimal.ONE;
        StringBuilder formulaOdds = new StringBuilder();
        for (int idx = 0; idx < opcoesEscolhidas.size(); idx++) {
            OpcaoAposta op = opcoesEscolhidas.get(idx);
            oddAcumulada = oddAcumulada.multiply(op.getOdd());
            if (idx > 0) formulaOdds.append(" x ");
            formulaOdds.append(op.getOdd());
        }
        BigDecimal retornoPrevisto = valor.multiply(oddAcumulada).setScale(2, java.math.RoundingMode.HALF_UP);

        System.out.println("\n--- [ RESUMO DO BILHETE ] ---");
        System.out.println("Tipo de Aposta: " + (codigos.size() > 1 ? "COMBINADA / MULTIPLA (" + codigos.size() + " selecoes)" : "SIMPLES (1 selecao)"));
        if (codigos.size() > 1) {
            System.out.println("Calculo da Odd Combinada (PRODUTO DAS ODDS): " + formulaOdds + " = " + oddAcumulada.setScale(2, java.math.RoundingMode.HALF_UP));
        } else {
            System.out.println("Odd da Selecao: " + oddAcumulada.setScale(2, java.math.RoundingMode.HALF_UP));
        }
        System.out.printf("Valor Apostado: R$ %.2f%n", valor);
        System.out.printf("Retorno Potencial: R$ %.2f%n", retornoPrevisto);

        boolean conf = ConsoleUtils.lerConfirmacao(scanner, "\nConfirma a efetivacao da aposta? (S/N): ");

        if (conf) {
            Aposta aposta = apostaService.efetuarAposta(codusuario, valor, codigos);
            String dataStr = (aposta.getDataHora() != null) ? aposta.getDataHora().format(FMT_DATA) : "-";
            System.out.println("\n[OK] APOSTA EFETUADA COM SUCESSO!");
            System.out.println("    Bilhete Gerado: ID " + aposta.getCodaposta());
            System.out.printf("    Data/Hora: %s | Status: %s | Valor: R$ %.2f | Apostador: %s%n",
                    dataStr, aposta.getStatus(), aposta.getValor(), aposta.getUsuario().getNome());
        } else {
            System.out.println("[i] Operacao cancelada pelo apostador.");
        }
    }

    private void consultarBilhete(Scanner scanner) {
        int id = ConsoleUtils.lerInteiro(scanner, "\nInforme o ID da aposta: ");
        Aposta aposta = apostaService.buscarPorId(id);
        List<ItemAposta> itens = apostaService.buscarItensDaAposta(id);

        String retStr = (aposta.getValorRetorno() != null) ? String.format("R$ %.2f", aposta.getValorRetorno()) : "Pendente";
        String dataStr = (aposta.getDataHora() != null) ? aposta.getDataHora().format(FMT_DATA) : "-";

        System.out.println("\n================================================ BILHETE DE APOSTA #" + id + " ================================================");
        System.out.printf("ID: %-4d | Data: %s | Status: %-10s | Valor: R$ %-7.2f | Retorno: %-10s | Apostador: %s%n",
                aposta.getCodaposta(), dataStr, aposta.getStatus(), aposta.getValor(), retStr, aposta.getUsuario().getNome());
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

    private void liquidar(Scanner scanner) {
        System.out.println("\n--- [ LIQUIDACAO DE APOSTA ] ---");
        int id = ConsoleUtils.lerInteiro(scanner, "Informe o ID da aposta a liquidar: ");

        Aposta aposta = apostaService.buscarPorId(id);
        System.out.printf("Aposta Selecionada: ID %d | Valor: R$ %.2f | Status Atual: %s%n",
                aposta.getCodaposta(), aposta.getValor(), aposta.getStatus());

        String status = lerStatusLiquidacao(scanner);

        Aposta liquidada = apostaService.liquidarAposta(id, status);
        System.out.printf("\n[OK] Aposta ID %d liquidada com sucesso como '%s'!%n", id, liquidada.getStatus());
        System.out.printf("    Novo estado: Status: %s | Retorno: R$ %.2f%n",
                liquidada.getStatus(), liquidada.getValorRetorno());
    }

    private String lerStatusLiquidacao(Scanner scanner) {
        System.out.println("Resultados disponiveis:");
        System.out.println("  1 - GANHA");
        System.out.println("  2 - PERDIDA");
        while (true) {
            System.out.print("Informe o resultado final da aposta (1 - GANHA / 2 - PERDIDA): ");
            String input = scanner.nextLine().trim().toUpperCase();
            switch (input) {
                case "1", "GANHA" -> { return "GANHA"; }
                case "2", "PERDIDA" -> { return "PERDIDA"; }
                default -> System.out.println("[!] Opcao invalida! Digite 1 (GANHA) ou 2 (PERDIDA). Tente novamente.");
            }
        }
    }
}
