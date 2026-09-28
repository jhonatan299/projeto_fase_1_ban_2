package br.com.apostas.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * Entidade de domínio mapeada para a tabela relacional 'esporte'.
 */
@Entity
@Table(name = "esporte")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Esporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // Chave primária da modalidade esportiva
    private Integer codesporte;

    @NotBlank(message = "O nome do esporte e obrigatorio")
    @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres")
    @Column(nullable = false, unique = true, length = 100)
    private String nome;

    @NotNull(message = "O numero maximo de competidores por evento e obrigatorio")
    @Min(value = 2, message = "O numero maximo de competidores deve ser no minimo 2")
    @Column(name = "max_competidores_evento", nullable = false)
    @Builder.Default
    private Integer maxCompetidoresEvento = 2;
}
