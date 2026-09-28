package br.com.apostas.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entidade de domínio mapeada para a tabela relacional 'usuario'.
 */
@Entity
@Table(name = "usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // Chave primária auto-incrementada (SERIAL)
    private Integer codusuario;

    @NotBlank(message = "O nome e obrigatorio")
    @Size(min = 2, max = 150, message = "O nome deve ter entre 2 e 150 caracteres")
    @Column(nullable = false, length = 150)
    private String nome;

    @NotBlank(message = "O email e obrigatorio")
    @Email(message = "Email invalido")
    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(nullable = false, length = 255)
    private String senha;

    @Column(name = "data_cadastro", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime dataCadastro = LocalDateTime.now();

    @Column(nullable = false, length = 20)
    @Builder.Default
    // Status cadastral (ATIVO, BLOQUEADO, INATIVO)
    private String status = "ATIVO";

    // Valida se o usuário está ativo para efetuar apostas
    public boolean isAtivo() {
        return "ATIVO".equalsIgnoreCase(this.status);
    }
}
