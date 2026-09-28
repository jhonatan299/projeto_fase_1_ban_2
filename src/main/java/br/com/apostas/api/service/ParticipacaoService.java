package br.com.apostas.api.service;

import br.com.apostas.api.dto.ParticipacaoRequestDTO;
import br.com.apostas.api.model.Competidor;
import br.com.apostas.api.model.EventoEsportivo;
import br.com.apostas.api.model.Participacao;
import br.com.apostas.api.model.ParticipacaoId;
import br.com.apostas.api.repository.ParticipacaoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Camada de serviço responsável pela gestão de participações de competidores em partidas.
 */
@Service
@RequiredArgsConstructor
public class ParticipacaoService {

    private final ParticipacaoRepository participacaoRepository;
    private final EventoEsportivoService eventoService;
    private final CompetidorService competidorService;

    public List<Participacao> listarTodas() {
        return participacaoRepository.findAll();
    }

    public List<Participacao> listarPorEvento(Integer codevento) {
        return participacaoRepository.findByIdCodevento(codevento);
    }

    public List<Participacao> listarPorCompetidor(Integer codcompetidor) {
        return participacaoRepository.findByIdCodcompetidor(codcompetidor);
    }

    @Transactional
    public Participacao registrarParticipacao(ParticipacaoRequestDTO dto) {
        EventoEsportivo evento = eventoService.buscarPorId(dto.getCodevento());
        Competidor competidor = competidorService.buscarPorId(dto.getCodcompetidor());

        ParticipacaoId id = new ParticipacaoId(dto.getCodevento(), dto.getCodcompetidor());
        if (participacaoRepository.existsById(id)) {
            throw new IllegalArgumentException("O competidor '" + competidor.getNome() + "' ja esta registrado neste evento.");
        }

        br.com.apostas.api.model.Esporte esporte = (evento.getCompeticao() != null) ? evento.getCompeticao().getEsporte() : null;
        int max = (esporte != null && esporte.getMaxCompetidoresEvento() != null)
                ? esporte.getMaxCompetidoresEvento()
                : 2;

        int contagemAtual = participacaoRepository.findByIdCodevento(dto.getCodevento()).size();
        if (contagemAtual >= max) {
            String nomeEsporte = (esporte != null) ? esporte.getNome() : "Desconhecido";
            throw new IllegalArgumentException("Este evento ja atingiu o limite de " + max + " competidores para o esporte '" + nomeEsporte + "'.");
        }

        Participacao participacao = Participacao.builder()
                .id(id)
                .evento(evento)
                .competidor(competidor)
                .build();

        return participacaoRepository.save(participacao);
    }

    @Transactional
    public void removerParticipacao(Integer codevento, Integer codcompetidor) {
        ParticipacaoId id = new ParticipacaoId(codevento, codcompetidor);
        if (!participacaoRepository.existsById(id)) {
            throw new EntityNotFoundException("Participacao nao encontrada.");
        }
        participacaoRepository.deleteById(id);
    }
}
