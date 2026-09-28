package br.com.apostas.api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO para vincular um competidor a um evento esportivo (relação N:N).
 */
@Data
public class ParticipacaoRequestDTO {

    @NotNull(message = "O ID do evento esportivo e obrigatorio")
    private Integer codevento;

    @NotNull(message = "O ID do competidor e obrigatorio")
    private Integer codcompetidor;
}
