package br.com.apostas.api.repository;

import br.com.apostas.api.model.EventoEsportivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositório de dados Spring Data JPA para a entidade EventoEsportivo.
 */
@Repository
public interface EventoEsportivoRepository extends JpaRepository<EventoEsportivo, Integer> {

    List<EventoEsportivo> findByStatusIgnoreCase(String status);

    List<EventoEsportivo> findByCompeticaoCodcompeticao(Integer codcompeticao);
}
