package br.com.apostas.api.repository;

import br.com.apostas.api.model.Aposta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositório de dados Spring Data JPA para a entidade Aposta.
 */
@Repository
public interface ApostaRepository extends JpaRepository<Aposta, Integer> {

    List<Aposta> findByUsuarioCodusuarioOrderByDataHoraDesc(Integer codusuario);

    List<Aposta> findByStatusIgnoreCase(String status);
}
