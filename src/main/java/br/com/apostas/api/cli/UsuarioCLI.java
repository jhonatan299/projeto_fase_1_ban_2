package br.com.apostas.api.cli;

import br.com.apostas.api.model.Usuario;
import br.com.apostas.api.service.UsuarioService;
import br.com.apostas.api.util.ConsoleUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

/**
 * Interface de console para operações de CRUD de usuários apostadores.
 */
@Component
@RequiredArgsConstructor
public class UsuarioCLI {

    private final UsuarioService usuarioService;

    // Exibe as opções interativas no console e processa a entrada do usuário
    public void exibirMenu(Scanner scanner) {
        int opcao = -1;
        do {
            System.out.println("\n========================================================");
            System.out.println("               GERENCIAMENTO DE USUARIOS                ");
            System.out.println("========================================================");
            System.out.println(" 1 - Cadastrar novo usuario");
            System.out.println(" 2 - Listar todos os usuarios");
            System.out.println(" 3 - Consultar usuario por ID");
            System.out.println(" 4 - Atualizar dados de um usuario");
            System.out.println(" 5 - Remover um usuario");
            System.out.println(" 0 - Voltar ao menu principal");
            System.out.println("========================================================");

            try {
                opcao = ConsoleUtils.lerInteiro(scanner, "Escolha uma opcao: ");

                switch (opcao) {
                    case 1 -> cadastrarUsuario(scanner);
                    case 2 -> listarUsuarios();
                    case 3 -> consultarPorId(scanner);
                    case 4 -> atualizarUsuario(scanner);
                    case 5 -> removerUsuario(scanner);
                    case 0 -> System.out.println("Retornando ao menu principal...");
                    default -> System.out.println("[!] Opcao invalida! Escolha um valor entre 0 e 5.");
                }
            } catch (Exception e) {
                System.out.println("\n[X] ERRO: " + e.getMessage());
            }
        } while (opcao != 0);
    }

    private void cadastrarUsuario(Scanner scanner) {
        System.out.println("\n--- [ NOVO CADASTRO DE USUARIO ] ---");
        String nome = ConsoleUtils.lerTextoObrigatorio(scanner, "Nome completo: ");
        String email = ConsoleUtils.lerTextoObrigatorio(scanner, "E-mail: ");
        String senha = ConsoleUtils.lerTextoObrigatorio(scanner, "Senha: ");

        Usuario novo = Usuario.builder()
                .nome(nome.trim())
                .email(email.trim())
                .senha(senha)
                .status("ATIVO")
                .build();

        Usuario criado = usuarioService.criar(novo);
        System.out.println("\n[OK] Usuario cadastrado com sucesso! ID gerado: " + criado.getCodusuario() + " | Status: " + criado.getStatus());
        System.out.println("    " + criado);
    }

    private void listarUsuarios() {
        System.out.println("\n--- [ LISTAGEM DE USUARIOS ] ---");
        List<Usuario> usuarios = usuarioService.listarTodos();

        if (usuarios.isEmpty()) {
            System.out.println("[i] Nenhum usuario cadastrado no banco de dados.");
            return;
        }

        System.out.println(String.format("Total encontrado: %d usuarios", usuarios.size()));
        System.out.println("--------------------------------------------------------------------------------------------------");
        for (Usuario u : usuarios) {
            System.out.printf("ID: %-3d | Nome: %-25s | Email: %-32s | Status: %-10s%n",
                    u.getCodusuario(), u.getNome(), u.getEmail(), u.getStatus());
        }
        System.out.println("--------------------------------------------------------------------------------------------------");
    }

    private void consultarPorId(Scanner scanner) {
        System.out.println("\n--- [ CONSULTA POR ID ] ---");
        int id = ConsoleUtils.lerInteiro(scanner, "Informe o ID do usuario (codusuario): ");

        Usuario u = usuarioService.buscarPorId(id);
        System.out.println("\n[OK] Usuario encontrado:");
        System.out.printf("    ID: %-3d | Nome: %-25s | Email: %-32s | Status: %-10s%n",
                u.getCodusuario(), u.getNome(), u.getEmail(), u.getStatus());
    }

    private void atualizarUsuario(Scanner scanner) {
        System.out.println("\n--- [ ATUALIZACAO DE USUARIO ] ---");
        int id = ConsoleUtils.lerInteiro(scanner, "Informe o ID do usuario que deseja atualizar: ");

        Usuario atual = usuarioService.buscarPorId(id);
        System.out.println("\nDados atuais: " + atual.getNome() + " (" + atual.getEmail() + ")");
        System.out.println("(Pressione ENTER para manter o valor atual)");

        String nome = ConsoleUtils.lerTextoOpcional(scanner, "Novo Nome [" + atual.getNome() + "]: ", atual.getNome());
        String email = ConsoleUtils.lerTextoOpcional(scanner, "Novo E-mail [" + atual.getEmail() + "]: ", atual.getEmail());
        String senha = ConsoleUtils.lerTextoOpcional(scanner, "Nova Senha (deixe em branco para manter a atual): ", atual.getSenha());
        String status = lerStatus(scanner, atual.getStatus());

        Usuario dados = Usuario.builder()
                .nome(nome)
                .email(email)
                .senha(senha)
                .status(status)
                .build();

        usuarioService.atualizar(id, dados);
        System.out.println("\n[OK] Usuario ID " + id + " atualizado com sucesso!");
    }

    private String lerStatus(Scanner scanner, String statusPadrao) {
        System.out.println("Status disponiveis:");
        System.out.println("  1 - ATIVO");
        System.out.println("  2 - BLOQUEADO");
        System.out.println("  3 - INATIVO");
        while (true) {
            String prompt = statusPadrao != null
                    ? "Escolha o status (1/2/3) [Padrao: " + statusPadrao + "]: "
                    : "Escolha o status (1/2/3): ";
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.isEmpty() && statusPadrao != null) {
                return statusPadrao;
            }
            switch (input.toUpperCase()) {
                case "1", "ATIVO" -> { return "ATIVO"; }
                case "2", "BLOQUEADO" -> { return "BLOQUEADO"; }
                case "3", "INATIVO" -> { return "INATIVO"; }
                default -> System.out.println("[!] Opcao invalida! Digite 1 (ATIVO), 2 (BLOQUEADO) ou 3 (INATIVO). Tente novamente.");
            }
        }
    }

    private void removerUsuario(Scanner scanner) {
        System.out.println("\n--- [ REMOCAO DE USUARIO ] ---");
        int id = ConsoleUtils.lerInteiro(scanner, "Informe o ID do usuario a ser removido: ");

        Usuario atual = usuarioService.buscarPorId(id);
        System.out.println("Usuario selecionado: " + atual.getNome() + " (ID " + id + ")");
        boolean conf = ConsoleUtils.lerConfirmacao(scanner, "Tem certeza que deseja excluir este usuario? (S/N): ");

        if (conf) {
            usuarioService.deletar(id);
            System.out.println("\n[OK] Usuario ID " + id + " removido com sucesso!");
        } else {
            System.out.println("[i] Remocao cancelada.");
        }
    }
}
