package br.com.apostas.api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

/**
 * Chave primária composta da tabela associativa 'participacao'.
 */
@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParticipacaoId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "codevento")
    // Chave estrangeira para evento_esportivo
    private Integer codevento;

    @Column(name = "codcompetidor")
    // Chave estrangeira para competidor
    private Integer codcompetidor;
}
