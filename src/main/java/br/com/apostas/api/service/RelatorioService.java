package br.com.apostas.api.service;

import br.com.apostas.api.repository.RelatorioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Camada de serviço responsável pela emissão dos relatórios estatísticos e analíticos.
 */
@Service
@RequiredArgsConstructor
public class RelatorioService {

    private final RelatorioRepository relatorioRepository;
    private final UsuarioService usuarioService;

    public List<Map<String, Object>> obterHistoricoApostasUsuario(Integer codusuario) {
        usuarioService.buscarPorId(codusuario);
        return relatorioRepository.historicoApostasUsuario(codusuario);
    }

    public List<Map<String, Object>> obterVolumePorCompeticao() {
        return relatorioRepository.volumeApostadoPorCompeticao();
    }

    public List<Map<String, Object>> obterParticipacaoCompetidores() {
        return relatorioRepository.participacaoCompetidores();
    }

    public List<Map<String, Object>> obterOddMediaPorEsporteMercado() {
        return relatorioRepository.oddMediaPorEsporteMercado();
    }

    public List<Map<String, Object>> obterRankingLucroUsuarios() {
        return relatorioRepository.rankingLucroUsuarios();
    }
}
