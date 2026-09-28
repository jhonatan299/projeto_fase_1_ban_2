package br.com.apostas.api;

import br.com.apostas.api.cli.MainMenuCLI;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Ponto de entrada da aplicação Spring Boot em modo Console (CLI).
 */
@SpringBootApplication
@RequiredArgsConstructor
public class ProjetoApi1Application implements CommandLineRunner {

    private final MainMenuCLI mainMenuCLI;

    // Inicializa o contexto da aplicação Spring Boot
    public static void main(String[] args) {

        SpringApplication.run(ProjetoApi1Application.class, args);
    }

    @Override
    // Dispara o fluxo interativo do console no terminal
    public void run(String... args) {

        mainMenuCLI.iniciar();
    }
}
