package br.com.apostas.api.service;

import br.com.apostas.api.model.EventoEsportivo;
import br.com.apostas.api.model.Mercado;
import br.com.apostas.api.repository.MercadoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Camada de serviço responsável pelos mercados de aposta vinculados aos eventos.
 */
@Service
@RequiredArgsConstructor
public class MercadoService {

    private final MercadoRepository mercadoRepository;
    private final EventoEsportivoService eventoService;

    public List<Mercado> listarTodos() {
        return mercadoRepository.findAll();
    }

    public List<Mercado> listarPorEvento(Integer codevento) {
        return mercadoRepository.findByEventoCodevento(codevento);
    }

    public Mercado buscarPorId(Integer id) {
        return mercadoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Mercado com ID " + id + " nao encontrado."));
    }

    @Transactional
    public Mercado criar(Mercado mercado) {
        EventoEsportivo evento = eventoService.buscarPorId(mercado.getEvento().getCodevento());
        mercado.setEvento(evento);
        mercado.setStatus("ABERTO");
        return mercadoRepository.save(mercado);
    }

    @Transactional
    public Mercado atualizar(Integer id, Mercado dadosNovos) {
        Mercado existente = buscarPorId(id);
        EventoEsportivo evento = eventoService.buscarPorId(dadosNovos.getEvento().getCodevento());

        if (dadosNovos.getTipo() != null) {
            existente.setTipo(dadosNovos.getTipo().trim());
        }
        if (dadosNovos.getStatus() != null && !dadosNovos.getStatus().isBlank()) {
            existente.setStatus(dadosNovos.getStatus().toUpperCase().trim());
        }
        existente.setEvento(evento);

        return mercadoRepository.save(existente);
    }

    @Transactional
    public void deletar(Integer id) {
        Mercado mercado = buscarPorId(id);
        mercadoRepository.delete(mercado);
    }
}
