package br.com.apostas.api.service;

import br.com.apostas.api.model.Esporte;
import br.com.apostas.api.repository.EsporteRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Camada de serviço responsável pelo gerenciamento das modalidades esportivas.
 */
@Service
@RequiredArgsConstructor
public class EsporteService {

    private final EsporteRepository esporteRepository;

    public List<Esporte> listarTodos() {
        return esporteRepository.findAll();
    }

    public Esporte buscarPorId(Integer id) {
        return esporteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Esporte com ID " + id + " nao encontrado."));
    }

    @Transactional
    public Esporte criar(Esporte esporte) {
        if (esporteRepository.existsByNomeIgnoreCase(esporte.getNome())) {
            throw new IllegalArgumentException("Ja existe uma modalidade esportiva cadastrada com o nome '" + esporte.getNome() + "'.");
        }
        if (esporte.getMaxCompetidoresEvento() == null || esporte.getMaxCompetidoresEvento() < 2) {
            esporte.setMaxCompetidoresEvento(2);
        }
        return esporteRepository.save(esporte);
    }

    @Transactional
    public Esporte atualizar(Integer id, Esporte dadosNovos) {
        Esporte existente = buscarPorId(id);
        if (dadosNovos.getNome() != null && !dadosNovos.getNome().isBlank()) {
            esporteRepository.findByNomeIgnoreCase(dadosNovos.getNome()).ifPresent(outro -> {
                if (!outro.getCodesporte().equals(id)) {
                    throw new IllegalArgumentException("Ja existe outro esporte com o nome '" + dadosNovos.getNome() + "'.");
                }
            });
            existente.setNome(dadosNovos.getNome().trim());
        }
        if (dadosNovos.getMaxCompetidoresEvento() != null) {
            if (dadosNovos.getMaxCompetidoresEvento() < 2) {
                throw new IllegalArgumentException("O numero maximo de competidores por evento deve ser no minimo 2.");
            }
            existente.setMaxCompetidoresEvento(dadosNovos.getMaxCompetidoresEvento());
        }
        return esporteRepository.save(existente);
    }

    @Transactional
    public void deletar(Integer id) {
        Esporte esporte = buscarPorId(id);
        try {
            esporteRepository.delete(esporte);
            esporteRepository.flush();
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            throw new IllegalStateException("Nao e possivel excluir o esporte '" + esporte.getNome() + "' pois existem competicoes vinculadas a ele (violacao de integridade referencial).");
        }
    }
}
