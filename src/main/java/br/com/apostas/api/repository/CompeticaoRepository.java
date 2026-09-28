package br.com.apostas.api.repository;

import br.com.apostas.api.model.Competicao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositório de dados Spring Data JPA para a entidade Competicao.
 */
@Repository
public interface CompeticaoRepository extends JpaRepository<Competicao, Integer> {

    List<Competicao> findByEsporteCodesporte(Integer codesporte);
}
