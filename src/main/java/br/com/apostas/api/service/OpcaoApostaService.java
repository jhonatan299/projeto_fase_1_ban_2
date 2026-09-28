package br.com.apostas.api.service;

import br.com.apostas.api.model.Mercado;
import br.com.apostas.api.model.OpcaoAposta;
import br.com.apostas.api.repository.OpcaoApostaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * Camada de serviço responsável pelas opções de palpites e cotações (odds).
 */
@Service
@RequiredArgsConstructor
public class OpcaoApostaService {

    private final OpcaoApostaRepository opcaoRepository;
    private final MercadoService mercadoService;

    public List<OpcaoAposta> listarTodas() {
        return opcaoRepository.findAll();
    }

    public List<OpcaoAposta> listarPorMercado(Integer codmercado) {
        return opcaoRepository.findByMercadoCodmercado(codmercado);
    }

    public OpcaoAposta buscarPorId(Integer id) {
        return opcaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Opcao de Aposta com ID " + id + " nao encontrada."));
    }

    @Transactional
    public OpcaoAposta criar(OpcaoAposta opcao) {
        if (opcao.getOdd().compareTo(BigDecimal.ONE) <= 0) {
            throw new IllegalArgumentException("A odd deve ser estritamente superior a 1.00.");
        }
        Mercado mercado = mercadoService.buscarPorId(opcao.getMercado().getCodmercado());
        opcao.setMercado(mercado);
        return opcaoRepository.save(opcao);
    }

    @Transactional
    public OpcaoAposta atualizar(Integer id, OpcaoAposta dadosNovos) {
        OpcaoAposta existente = buscarPorId(id);
        if (dadosNovos.getOdd().compareTo(BigDecimal.ONE) <= 0) {
            throw new IllegalArgumentException("A odd deve ser estritamente superior a 1.00.");
        }

        Mercado mercado = mercadoService.buscarPorId(dadosNovos.getMercado().getCodmercado());

        existente.setResultado(dadosNovos.getResultado());
        existente.setOdd(dadosNovos.getOdd());
        existente.setDescricao(dadosNovos.getDescricao());
        existente.setMercado(mercado);

        return opcaoRepository.save(existente);
    }

    @Transactional
    public void deletar(Integer id) {
        OpcaoAposta opcao = buscarPorId(id);
        opcaoRepository.delete(opcao);
    }
}
