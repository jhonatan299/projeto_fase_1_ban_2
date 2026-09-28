package br.com.apostas.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

/**
 * Entidade de domínio mapeada para a tabela relacional 'competidor' (times e atletas).
 */
@Entity
@Table(name = "competidor")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Competidor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // Chave primária do competidor
    private Integer codcompetidor;

    @NotBlank(message = "O nome do competidor e obrigatorio")
    @Column(nullable = false, length = 150)
    private String nome;

    @NotBlank(message = "O tipo e obrigatorio (TIME ou INDIVIDUAL)")
    @Pattern(regexp = "^(TIME|INDIVIDUAL)$", message = "O tipo deve ser TIME ou INDIVIDUAL")
    @Column(nullable = false, length = 20)
    private String tipo;
}
