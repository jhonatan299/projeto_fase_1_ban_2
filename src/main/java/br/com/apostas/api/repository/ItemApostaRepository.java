package br.com.apostas.api.repository;

import br.com.apostas.api.model.ItemAposta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositório de dados Spring Data JPA para a entidade ItemAposta.
 */
@Repository
public interface ItemApostaRepository extends JpaRepository<ItemAposta, Integer> {

    List<ItemAposta> findByApostaCodaposta(Integer codaposta);
}
