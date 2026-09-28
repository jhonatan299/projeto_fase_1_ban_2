package br.com.apostas.api.service;

import br.com.apostas.api.model.Usuario;
import br.com.apostas.api.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * Camada de serviço responsável pelas validações cadastrais e regras de negócio de usuários.
 */
@Service
@RequiredArgsConstructor
public class UsuarioService {

    private static final Set<String> STATUS_VALIDOS = Set.of("ATIVO", "BLOQUEADO", "INATIVO");

    private final UsuarioRepository usuarioRepository;

    private void validarStatus(String status) {
        if (status == null || !STATUS_VALIDOS.contains(status.trim().toUpperCase())) {
            throw new IllegalArgumentException("Status invalido: '" + status + "'. Os valores permitidos sao: " + STATUS_VALIDOS);
        }
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Usuario buscarPorId(Integer id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario com ID " + id + " nao encontrado."));
    }

    @Transactional
    // Valida duplicidade de e-mail e cadastra novo apostador com status ATIVO
    public Usuario criar(Usuario usuario) {

        if (usuarioRepository.existsByEmailIgnoreCase(usuario.getEmail())) {
            throw new IllegalArgumentException("Ja existe um usuario cadastrado com este E-mail.");
        }

        usuario.setStatus("ATIVO");
        usuario.setEmail(usuario.getEmail().toLowerCase().trim());
        return usuarioRepository.save(usuario);
    }

    @Transactional
    // Atualiza os dados cadastrais do usuário com validações de unicidade
    public Usuario atualizar(Integer id, Usuario dadosNovos) {
        Usuario existente = buscarPorId(id);

        usuarioRepository.findByEmailIgnoreCase(dadosNovos.getEmail().trim()).ifPresent(outro -> {
            if (!outro.getCodusuario().equals(id)) {
                throw new IllegalArgumentException("Ja existe outro usuario cadastrado com este E-mail.");
            }
        });

        String statusNormalizado = dadosNovos.getStatus().trim().toUpperCase();
        validarStatus(statusNormalizado);

        existente.setNome(dadosNovos.getNome().trim());
        existente.setEmail(dadosNovos.getEmail().toLowerCase().trim());
        if (dadosNovos.getSenha() != null && !dadosNovos.getSenha().isBlank()) {
            existente.setSenha(dadosNovos.getSenha());
        }
        existente.setStatus(statusNormalizado);

        return usuarioRepository.save(existente);
    }

    @Transactional
    public void deletar(Integer id) {
        Usuario existente = buscarPorId(id);
        usuarioRepository.delete(existente);
    }
}
