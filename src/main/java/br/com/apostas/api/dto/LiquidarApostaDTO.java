package br.com.apostas.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * DTO para recebimento do resultado e liquidação de uma aposta (GANHA ou PERDIDA).
 */
@Data
public class LiquidarApostaDTO {

    @NotBlank(message = "O status de liquidacao e obrigatorio (GANHA ou PERDIDA)")
    @Pattern(regexp = "^(GANHA|PERDIDA|CANCELADA)$", message = "Status deve ser GANHA, PERDIDA ou CANCELADA")
    private String status;
}
