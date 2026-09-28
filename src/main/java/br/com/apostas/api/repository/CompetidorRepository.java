package br.com.apostas.api.repository;

import br.com.apostas.api.model.Competidor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositório de dados Spring Data JPA para a entidade Competidor.
 */
@Repository
public interface CompetidorRepository extends JpaRepository<Competidor, Integer> {

    List<Competidor> findByTipoIgnoreCase(String tipo);
}
