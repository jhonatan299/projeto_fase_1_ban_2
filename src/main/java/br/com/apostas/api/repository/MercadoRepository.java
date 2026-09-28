package br.com.apostas.api.repository;

import br.com.apostas.api.model.Mercado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositório de dados Spring Data JPA para a entidade Mercado.
 */
@Repository
public interface MercadoRepository extends JpaRepository<Mercado, Integer> {

    List<Mercado> findByEventoCodevento(Integer codevento);

    List<Mercado> findByStatusIgnoreCase(String status);
}
