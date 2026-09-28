package br.com.apostas.api.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO de entrada para registrar uma aposta (usuário, valor e palpites).
 */
@Data
public class ApostaRequestDTO {

    @NotNull(message = "O ID do usuario (codusuario) e obrigatorio")
    private Integer codusuario;

    @NotNull(message = "O valor da aposta e obrigatorio")
    @DecimalMin(value = "0.01", message = "O valor da aposta deve ser maior que zero")
    private BigDecimal valor;

    @NotEmpty(message = "Selecione pelo menos uma opcao de aposta")
    private List<Integer> codigosOpcoes;
}
