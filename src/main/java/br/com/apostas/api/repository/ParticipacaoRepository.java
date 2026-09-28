package br.com.apostas.api.repository;

import br.com.apostas.api.model.Participacao;
import br.com.apostas.api.model.ParticipacaoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositório de dados Spring Data JPA para a entidade associativa Participacao.
 */
@Repository
public interface ParticipacaoRepository extends JpaRepository<Participacao, ParticipacaoId> {

    List<Participacao> findByIdCodevento(Integer codevento);

    List<Participacao> findByIdCodcompetidor(Integer codcompetidor);
}
