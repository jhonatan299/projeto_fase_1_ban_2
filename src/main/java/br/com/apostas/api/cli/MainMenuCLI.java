package br.com.apostas.api.cli;

import br.com.apostas.api.util.ConsoleUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Scanner;

/**
 * Controlador do menu principal interativo da aplicação console (CLI).
 */
@Component
@RequiredArgsConstructor
public class MainMenuCLI {

    private final UsuarioCLI usuarioCLI;
    private final EsporteCLI esporteCLI;
    private final CompeticaoCLI competicaoCLI;
    private final CompetidorCLI competidorCLI;
    private final EventoEsportivoCLI eventoCLI;
    private final MercadoCLI mercadoCLI;
    private final OpcaoApostaCLI opcaoCLI;
    private final ApostaCLI apostaCLI;
    private final ParticipacaoCLI participacaoCLI;
    private final EfetuarApostaCLI efetuarApostaCLI;
    private final RelatorioCLI relatorioCLI;

    // Exibe as opções interativas no console e processa a entrada do usuário
    public void iniciar() {
        Scanner scanner = new Scanner(System.in);
        int opcao = -1;

        System.out.println("================================================================================");
        System.out.println("   SISTEMA DE APOSTAS ESPORTIVAS - CONSOLE CLI (SPRING BOOT 3 + DATA JPA)       ");
        System.out.println("================================================================================");

        do {
            System.out.println("\n+----------------------------------------------------------------+");
            System.out.println("|        SISTEMA DE APOSTAS ESPORTIVAS - MENU PRINCIPAL          |");
            System.out.println("+----------------------------------------------------------------+");
            System.out.println("|  ENTIDADES (CRUD)                                              |");
            System.out.println("|  1 - Gerenciar Usuarios                                        |");
            System.out.println("|  2 - Gerenciar Esportes                                        |");
            System.out.println("|  3 - Gerenciar Competicoes                                     |");
            System.out.println("|  4 - Gerenciar Competidores                                    |");
            System.out.println("|  5 - Gerenciar Eventos Esportivos                              |");
            System.out.println("|  6 - Gerenciar Mercados                                        |");
            System.out.println("|  7 - Gerenciar Opcoes de Aposta                                |");
            System.out.println("|  8 - Gerenciar Apostas                                         |");
            System.out.println("+----------------------------------------------------------------+");
            System.out.println("|  PROCESSOS DE NEGOCIO & RELATORIOS                             |");
            System.out.println("|  9 - Registrar Participacao em Evento                          |");
            System.out.println("| 10 - Efetuar / Liquidar Aposta (Transacao ACID)                |");
            System.out.println("| 11 - Relatorios Analiticos                                     |");
            System.out.println("+----------------------------------------------------------------+");
            System.out.println("|  0 - Sair do Sistema                                           |");
            System.out.println("+----------------------------------------------------------------+");

            try {
                opcao = ConsoleUtils.lerInteiro(scanner, "Selecione uma opcao: ");

                switch (opcao) {
                    case 1 -> usuarioCLI.exibirMenu(scanner);
                    case 2 -> esporteCLI.exibirMenu(scanner);
                    case 3 -> competicaoCLI.exibirMenu(scanner);
                    case 4 -> competidorCLI.exibirMenu(scanner);
                    case 5 -> eventoCLI.exibirMenu(scanner);
                    case 6 -> mercadoCLI.exibirMenu(scanner);
                    case 7 -> opcaoCLI.exibirMenu(scanner);
                    case 8 -> apostaCLI.exibirMenu(scanner);
                    case 9 -> participacaoCLI.exibirMenu(scanner);
                    case 10 -> efetuarApostaCLI.exibirMenu(scanner);
                    case 11 -> relatorioCLI.exibirMenu(scanner);
                    case 0 -> System.out.println("\nFinalizando o sistema Spring Boot CLI. Ate logo!");
                    default -> System.out.println("\n[!] Opcao invalida! Escolha um numero entre 0 e 11.");
                }
            } catch (java.util.NoSuchElementException e) {
                System.out.println("\n[INFO] Fim da entrada de dados. Finalizando o sistema.");
                break;
            } catch (Exception e) {
                System.out.println("\n[X] Ocorreu um erro: " + e.getMessage());
            }
        } while (opcao != 0);
    }
}
