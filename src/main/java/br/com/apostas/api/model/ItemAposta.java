package br.com.apostas.api.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * Entidade que registra o palpite individual e congela a odd no bilhete (tabela 'item_aposta').
 */
@Entity
@Table(name = "item_aposta")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemAposta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // Chave primária do item
    private Integer coditem;

    @Column(nullable = false, precision = 6, scale = 2)
    // Cotação contratada congelada na emissão
    private BigDecimal oddcadastrada;

    @Column(nullable = false, length = 20)
    @Builder.Default
    private String resultado = "PENDENTE";

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codaposta", nullable = false)
    // Bilhete de aposta pai (FK codaposta)
    private Aposta aposta;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "codopcao", nullable = false)
    private OpcaoAposta opcao;
}
