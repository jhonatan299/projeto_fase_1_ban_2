package br.com.apostas.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entidade de domínio mapeada para a tabela relacional 'evento_esportivo'.
 */
@Entity
@Table(name = "evento_esportivo")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventoEsportivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // Chave primária da partida
    private Integer codevento;

    @Column(length = 200)
    private String descricao;

    @NotNull(message = "A data e hora do evento e obrigatoria")
    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;

    @NotNull(message = "O status e obrigatorio")
    @Pattern(regexp = "^(AGENDADO|AO_VIVO|FINALIZADO|CANCELADO)$",
             message = "Status invalido (Valores aceitos: AGENDADO, AO_VIVO, FINALIZADO, CANCELADO)")
    @Column(nullable = false, length = 20)
    private String status;

    @NotNull(message = "A competicao e obrigatoria")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "codcompeticao", nullable = false)
    // Torneio ao qual o evento pertence (FK codcompeticao)
    private Competicao competicao;
}
