package br.com.apostas.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

/**
 * Entidade de domínio mapeada para a tabela relacional 'competicao'.
 */
@Entity
@Table(name = "competicao")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Competicao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // Chave primária do torneio
    private Integer codcompeticao;

    @NotBlank(message = "O nome da competicao e obrigatorio")
    @Column(nullable = false, length = 150)
    private String nome;

    @Column(length = 60)
    private String pais;

    @NotNull(message = "A data de inicio e obrigatoria")
    @Column(name = "data_inicio", nullable = false)
    private LocalDate dataInicio;

    @Column(name = "data_fim")
    private LocalDate dataFim;

    @NotNull(message = "O esporte e obrigatorio")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "codesporte", nullable = false)
    // Relacionamento N:1 com a modalidade esportiva (FK codesporte)
    private Esporte esporte;
}
