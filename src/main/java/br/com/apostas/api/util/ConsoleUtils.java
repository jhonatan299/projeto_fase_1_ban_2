package br.com.apostas.api.util;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/**
 * Utilitário com métodos estáticos para leitura e validação de entradas no terminal.
 */
public class ConsoleUtils {

    public static String lerTextoObrigatorio(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("[!] Este campo e obrigatorio e nao pode ficar em branco. Tente novamente.");
        }
    }

    public static String lerTextoOpcional(Scanner scanner, String prompt, String valorPadrao) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        return input.isEmpty() ? valorPadrao : input;
    }

    public static int lerInteiro(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("[!] Entrada invalida ('" + input + "'). Digite um numero inteiro valido.");
            }
        }
    }

    public static int lerInteiroPositivo(Scanner scanner, String prompt) {
        while (true) {
            int val = lerInteiro(scanner, prompt);
            if (val > 0) {
                return val;
            }
            System.out.println("[!] O valor (" + val + ") deve ser um numero maior que zero. Tente novamente.");
        }
    }

    public static BigDecimal lerBigDecimal(Scanner scanner, String prompt, BigDecimal minimo) {
        while (true) {
            System.out.print(prompt);
            String raw = scanner.nextLine().trim().replace("R$", "").replace("r$", "").replace(" ", "").replace(",", ".");
            if (raw.isEmpty()) {
                System.out.println("[!] O valor e obrigatorio. Digite um numero decimal valido (ex: 50.00).");
                continue;
            }
            try {
                BigDecimal val = new BigDecimal(raw);
                if (minimo != null && val.compareTo(minimo) < 0) {
                    System.out.println("[!] O valor digitado (R$ " + val + ") e menor que o minimo permitido (R$ " + minimo + "). Tente novamente.");
                    continue;
                }
                return val;
            } catch (Exception e) {
                System.out.println("[!] Formato monetario/decimal invalido ('" + raw + "'). Exemplo valido: 50.00 ou 100,50.");
            }
        }
    }

    public static LocalDateTime lerDataHora(Scanner scanner, String prompt) {
        String[] patterns = {
                "yyyy-MM-dd HH:mm",
                "yyyy/MM/dd HH:mm",
                "yyyy-MM-dd'T'HH:mm",
                "yyyy-MM-dd'T'HH:mm:ss",
                "yyyy-MM-dd HH:mm:ss",
                "yyyy/MM/dd HH:mm:ss",
                "dd/MM/yyyy HH:mm",
                "dd/MM/yyyy HH:mm:ss",
                "dd-MM-yyyy HH:mm"
        };

        while (true) {
            System.out.print(prompt);
            String dtStr = scanner.nextLine().trim();

            if (dtStr.isEmpty()) {
                System.out.println("[!] A data e o horario sao obrigatorios. Exemplo: 2026-10-15 18:00 ou 15/10/2026 18:00.");
                continue;
            }

            for (String pattern : patterns) {
                try {
                    return LocalDateTime.parse(dtStr, DateTimeFormatter.ofPattern(pattern));
                } catch (Exception ignored) {
                }
            }

            System.out.println("[!] Formato de data invalido ('" + dtStr + "'). Digite no formato 'AAAA-MM-DD HH:MM' ou 'DD/MM/AAAA HH:MM' (ex: 2026-10-15 18:00).");
        }
    }

    public static java.time.LocalDate lerData(Scanner scanner, String prompt, boolean obrigatorio) {
        String[] patterns = {"yyyy-MM-dd", "yyyy/MM/dd", "dd/MM/yyyy", "dd-MM-yyyy"};

        while (true) {
            System.out.print(prompt);
            String dtStr = scanner.nextLine().trim();

            if (dtStr.isEmpty()) {
                if (!obrigatorio) {
                    return null;
                }
                System.out.println("[!] A data e obrigatoria. Exemplo: 2026-10-15 ou 15/10/2026.");
                continue;
            }

            for (String pattern : patterns) {
                try {
                    return java.time.LocalDate.parse(dtStr, DateTimeFormatter.ofPattern(pattern));
                } catch (Exception ignored) {
                }
            }

            System.out.println("[!] Formato de data invalido ('" + dtStr + "'). Digite no formato 'AAAA-MM-DD' ou 'DD/MM/AAAA' (ex: 2026-10-15).");
        }
    }

    public static boolean lerConfirmacao(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim().toUpperCase();
            if (input.equals("S") || input.equals("SIM") || input.equals("Y") || input.equals("YES")) {
                return true;
            }
            if (input.equals("N") || input.equals("NAO") || input.equals("NO")) {
                return false;
            }
            System.out.println("[!] Resposta invalida ('" + input + "'). Responda com 'S' para Sim ou 'N' para Nao.");
        }
    }
}
