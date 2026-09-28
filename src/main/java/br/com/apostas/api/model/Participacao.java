package br.com.apostas.api.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entidade associativa que resolve o relacionamento N:N entre Evento e Competidor.
 */
@Entity
@Table(name = "participacao")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Participacao {

    @EmbeddedId
    // Chave primária composta embutida (@EmbeddedId)
    private ParticipacaoId id;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("codevento")
    @JoinColumn(name = "codevento")
    // Referência ao evento esportivo
    private EventoEsportivo evento;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("codcompetidor")
    @JoinColumn(name = "codcompetidor")
    // Referência ao competidor participante
    private Competidor competidor;
}
