package br.com.apostas.api.service;

import br.com.apostas.api.dto.ApostaRequestDTO;
import br.com.apostas.api.dto.LiquidarApostaDTO;
import br.com.apostas.api.model.Aposta;
import br.com.apostas.api.model.ItemAposta;
import br.com.apostas.api.model.OpcaoAposta;
import br.com.apostas.api.model.Usuario;
import br.com.apostas.api.repository.ApostaRepository;
import br.com.apostas.api.repository.ItemApostaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Camada de serviço responsável pelas regras de negócio e transações de apostas.
 */
@Service
@RequiredArgsConstructor
public class ApostaService {

    private final ApostaRepository apostaRepository;
    private final ItemApostaRepository itemApostaRepository;
    private final UsuarioService usuarioService;
    private final OpcaoApostaService opcaoService;

    public List<Aposta> listarTodas() {
        return apostaRepository.findAll();
    }

    public Aposta buscarPorId(Integer id) {
        return apostaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Aposta com ID " + id + " nao encontrada."));
    }

    public List<ItemAposta> buscarItensDaAposta(Integer codaposta) {
        return itemApostaRepository.findByApostaCodaposta(codaposta);
    }

    @Transactional
    // Transação atômica ACID: valida apostador, congela odds e registra o bilhete
    public Aposta efetuarAposta(ApostaRequestDTO dto) {

        Usuario usuario = usuarioService.buscarPorId(dto.getCodusuario());
        if (!usuario.isAtivo()) {
            throw new IllegalArgumentException("Operacao bloqueada: O usuario '" + usuario.getNome() + "' nao esta ATIVO (Status atual: " + usuario.getStatus() + ").");
        }

        List<OpcaoAposta> opcoesSelecionadas = new ArrayList<>();
        BigDecimal oddMultiplicada = BigDecimal.ONE;

        for (Integer codOpcao : dto.getCodigosOpcoes()) {
            OpcaoAposta opcao = opcaoService.buscarPorId(codOpcao);
            if (!opcao.getMercado().isAberto()) {
                throw new IllegalArgumentException("A opcao '" + opcao.getResultado() + "' (ID " + codOpcao + ") nao pode ser selecionada: o mercado correspondente '" +
                        opcao.getMercado().getTipo() + "' nao esta ABERTO (Status atual: " + opcao.getMercado().getStatus() + ").");
            }
            opcoesSelecionadas.add(opcao);
            oddMultiplicada = oddMultiplicada.multiply(opcao.getOdd());
        }

        Aposta novaAposta = Aposta.builder()
                .usuario(usuario)
                .valor(dto.getValor())
                .status("PENDENTE")
                .dataHora(LocalDateTime.now())
                .build();

        Aposta apostaSalva = apostaRepository.save(novaAposta);

        List<ItemAposta> itens = new ArrayList<>();
        for (OpcaoAposta opcao : opcoesSelecionadas) {
            ItemAposta item = ItemAposta.builder()
                    .aposta(apostaSalva)
                    .opcao(opcao)
                    .oddcadastrada(opcao.getOdd())
                    .resultado("PENDENTE")
                    .build();
            itens.add(itemApostaRepository.save(item));
        }

        apostaSalva.setItens(itens);
        return apostaSalva;
    }

    @Transactional
    // Processa a liquidação financeira da aposta (GANHA ou PERDIDA)
    public Aposta liquidarAposta(Integer codaposta, LiquidarApostaDTO dto) {
        Aposta aposta = buscarPorId(codaposta);
        String novoStatus = dto.getStatus().toUpperCase();

        if ("GANHA".equals(novoStatus)) {
            List<ItemAposta> itens = buscarItensDaAposta(codaposta);
            BigDecimal oddTotal = BigDecimal.ONE;
            for (ItemAposta item : itens) {
                oddTotal = oddTotal.multiply(item.getOddcadastrada());
                item.setResultado("GANHA");
                itemApostaRepository.save(item);
            }
            BigDecimal retorno = aposta.getValor().multiply(oddTotal).setScale(2, RoundingMode.HALF_UP);
            aposta.setValorRetorno(retorno);
            aposta.setStatus("GANHA");
        } else if ("PERDIDA".equals(novoStatus)) {
            List<ItemAposta> itens = buscarItensDaAposta(codaposta);
            for (ItemAposta item : itens) {
                item.setResultado("PERDIDA");
                itemApostaRepository.save(item);
            }
            aposta.setValorRetorno(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
            aposta.setStatus("PERDIDA");
        } else if ("CANCELADA".equals(novoStatus)) {
            List<ItemAposta> itens = buscarItensDaAposta(codaposta);
            for (ItemAposta item : itens) {
                item.setResultado("CANCELADA");
                itemApostaRepository.save(item);
            }
            aposta.setValorRetorno(aposta.getValor());
            aposta.setStatus("CANCELADA");
        }

        return apostaRepository.save(aposta);
    }

    @Transactional
    // Transação atômica ACID: valida apostador, congela odds e registra o bilhete
    public Aposta efetuarAposta(Integer codusuario, BigDecimal valor, List<Integer> codigosOpcoes) {
        ApostaRequestDTO dto = new ApostaRequestDTO();
        dto.setCodusuario(codusuario);
        dto.setValor(valor);
        dto.setCodigosOpcoes(codigosOpcoes);
        return efetuarAposta(dto);
    }

    @Transactional
    // Processa a liquidação financeira da aposta (GANHA ou PERDIDA)
    public Aposta liquidarAposta(Integer codaposta, String status) {
        LiquidarApostaDTO dto = new LiquidarApostaDTO();
        dto.setStatus(status);
        return liquidarAposta(codaposta, dto);
    }

    @Transactional
    public void deletar(Integer id) {
        Aposta aposta = buscarPorId(id);
        apostaRepository.delete(aposta);
    }
}
