package br.com.apostas.api.repository;

import br.com.apostas.api.model.Esporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório de dados Spring Data JPA para a entidade Esporte.
 */
@Repository
public interface EsporteRepository extends JpaRepository<Esporte, Integer> {

    Optional<Esporte> findByNomeIgnoreCase(String nome);

    boolean existsByNomeIgnoreCase(String nome);
}
