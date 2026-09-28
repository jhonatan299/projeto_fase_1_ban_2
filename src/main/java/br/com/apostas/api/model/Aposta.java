package br.com.apostas.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidade raiz que representa o bilhete de aposta emitido pelo usuário (tabela 'aposta').
 */
@Entity
@Table(name = "aposta")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Aposta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // Chave primária do bilhete
    private Integer codaposta;

    @Column(nullable = false, length = 20)
    @Builder.Default
    private String status = "PENDENTE";

    @Column(name = "data_hora", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime dataHora = LocalDateTime.now();

    @NotNull(message = "O valor e obrigatorio")
    @DecimalMin(value = "0.01", message = "O valor da aposta deve ser positivo")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    @Column(name = "valor_retorno", precision = 10, scale = 2)
    private BigDecimal valorRetorno;

    @NotNull(message = "O usuario e obrigatorio")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "codusuario", nullable = false)
    // Apostador titular do bilhete (FK codusuario)
    private Usuario usuario;

    @OneToMany(mappedBy = "aposta", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    // Palpites individuais que compõem o bilhete
    private List<ItemAposta> itens = new ArrayList<>();
}
