package br.com.apostas.api.service;

import br.com.apostas.api.model.Competicao;
import br.com.apostas.api.model.EventoEsportivo;
import br.com.apostas.api.repository.EventoEsportivoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Camada de serviço responsável pelo controle de eventos esportivos e partidas.
 */
@Service
@RequiredArgsConstructor
public class EventoEsportivoService {

    private final EventoEsportivoRepository eventoRepository;
    private final CompeticaoService competicaoService;

    public List<EventoEsportivo> listarTodos() {
        return eventoRepository.findAll();
    }

    public EventoEsportivo buscarPorId(Integer id) {
        return eventoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Evento Esportivo com ID " + id + " nao encontrado."));
    }

    @Transactional
    public EventoEsportivo criar(EventoEsportivo evento) {
        Competicao competicao = competicaoService.buscarPorId(evento.getCompeticao().getCodcompeticao());
        evento.setCompeticao(competicao);
        if (evento.getDescricao() != null) {
            evento.setDescricao(evento.getDescricao().trim());
        }
        evento.setStatus("AGENDADO");
        return eventoRepository.save(evento);
    }

    @Transactional
    public EventoEsportivo atualizar(Integer id, EventoEsportivo dadosNovos) {
        EventoEsportivo existente = buscarPorId(id);
        Competicao competicao = competicaoService.buscarPorId(dadosNovos.getCompeticao().getCodcompeticao());

        if (dadosNovos.getDescricao() != null) {
            existente.setDescricao(dadosNovos.getDescricao().trim());
        }
        if (dadosNovos.getDataHora() != null) {
            existente.setDataHora(dadosNovos.getDataHora());
        }
        if (dadosNovos.getStatus() != null && !dadosNovos.getStatus().isBlank()) {
            existente.setStatus(dadosNovos.getStatus().toUpperCase().trim());
        }
        existente.setCompeticao(competicao);

        return eventoRepository.save(existente);
    }

    @Transactional
    public void deletar(Integer id) {
        EventoEsportivo evento = buscarPorId(id);
        eventoRepository.delete(evento);
    }
}
