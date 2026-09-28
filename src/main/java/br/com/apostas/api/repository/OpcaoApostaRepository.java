package br.com.apostas.api.repository;

import br.com.apostas.api.model.OpcaoAposta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositório de dados Spring Data JPA para a entidade OpcaoAposta.
 */
@Repository
public interface OpcaoApostaRepository extends JpaRepository<OpcaoAposta, Integer> {

    List<OpcaoAposta> findByMercadoCodmercado(Integer codmercado);
}
