package br.com.apostas.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

/**
 * Entidade que mapeia as cotações/odds possíveis de um mercado (tabela 'opcao_aposta').
 */
@Entity
@Table(name = "opcao_aposta")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OpcaoAposta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // Chave primária da cotação
    private Integer codopcao;

    @NotBlank(message = "O resultado/palpite e obrigatorio")
    @Column(nullable = false, length = 100)
    private String resultado;

    @NotNull(message = "A odd e obrigatoria")
    @DecimalMin(value = "1.01", message = "A odd deve ser estritamente superior a 1.00")
    @Column(nullable = false, precision = 6, scale = 2)
    private BigDecimal odd;

    @Column(length = 200)
    private String descricao;

    @NotNull(message = "O mercado e obrigatorio")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "codmercado", nullable = false)
    // Mercado pai da opção (FK codmercado)
    private Mercado mercado;
}
