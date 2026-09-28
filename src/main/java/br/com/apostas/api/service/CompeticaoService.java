package br.com.apostas.api.service;

import br.com.apostas.api.model.Competicao;
import br.com.apostas.api.model.Esporte;
import br.com.apostas.api.repository.CompeticaoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Camada de serviço responsável pelas regras de negócio de competições esportivas.
 */
@Service
@RequiredArgsConstructor
public class CompeticaoService {

    private final CompeticaoRepository competicaoRepository;
    private final EsporteService esporteService;

    public List<Competicao> listarTodas() {
        return competicaoRepository.findAll();
    }

    public Competicao buscarPorId(Integer id) {
        return competicaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Competicao com ID " + id + " nao encontrada."));
    }

    @Transactional
    public Competicao criar(Competicao competicao) {
        validarDatas(competicao);
        Esporte esporte = esporteService.buscarPorId(competicao.getEsporte().getCodesporte());
        competicao.setEsporte(esporte);
        return competicaoRepository.save(competicao);
    }

    @Transactional
    public Competicao atualizar(Integer id, Competicao dadosNovos) {
        Competicao existente = buscarPorId(id);
        validarDatas(dadosNovos);

        Esporte esporte = esporteService.buscarPorId(dadosNovos.getEsporte().getCodesporte());

        existente.setNome(dadosNovos.getNome());
        existente.setPais(dadosNovos.getPais());
        existente.setDataInicio(dadosNovos.getDataInicio());
        existente.setDataFim(dadosNovos.getDataFim());
        existente.setEsporte(esporte);

        return competicaoRepository.save(existente);
    }

    @Transactional
    public void deletar(Integer id) {
        Competicao competicao = buscarPorId(id);
        competicaoRepository.delete(competicao);
    }

    private void validarDatas(Competicao c) {
        if (c.getDataFim() != null && c.getDataFim().isBefore(c.getDataInicio())) {
            throw new IllegalArgumentException("A data de termino (" + c.getDataFim() + ") nao pode ser anterior a data de inicio (" + c.getDataInicio() + ").");
        }
    }
}
