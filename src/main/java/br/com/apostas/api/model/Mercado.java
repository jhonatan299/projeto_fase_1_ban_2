package br.com.apostas.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

/**
 * Entidade de domínio mapeada para a tabela relacional 'mercado'.
 */
@Entity
@Table(name = "mercado")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Mercado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // Chave primária do mercado de palpites
    private Integer codmercado;

    @NotBlank(message = "O tipo do mercado e obrigatorio")
    @Column(nullable = false, length = 50)
    private String tipo;

    @NotNull(message = "O status do mercado e obrigatorio")
    @Pattern(regexp = "^(ABERTO|SUSPENSO|FECHADO)$",
             message = "Status invalido (Valores aceitos: ABERTO, SUSPENSO, FECHADO)")
    @Column(nullable = false, length = 20)
    @Builder.Default
    private String status = "ABERTO";

    @NotNull(message = "O evento esportivo e obrigatorio")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "codevento", nullable = false)
    // Evento esportivo correspondente (FK codevento)
    private EventoEsportivo evento;

    public boolean isAberto() {
        return "ABERTO".equalsIgnoreCase(this.status);
    }
}
