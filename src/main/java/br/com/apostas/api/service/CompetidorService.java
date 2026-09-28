package br.com.apostas.api.service;

import br.com.apostas.api.model.Competidor;
import br.com.apostas.api.repository.CompetidorRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Camada de serviço responsável pelo cadastro e validações de competidores.
 */
@Service
@RequiredArgsConstructor
public class CompetidorService {

    private final CompetidorRepository competidorRepository;

    public List<Competidor> listarTodos() {
        return competidorRepository.findAll();
    }

    public Competidor buscarPorId(Integer id) {
        return competidorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Competidor com ID " + id + " nao encontrado."));
    }

    @Transactional
    public Competidor criar(Competidor competidor) {

        competidor.setTipo(competidor.getTipo().toUpperCase());
        return competidorRepository.save(competidor);
    }

    @Transactional
    public Competidor atualizar(Integer id, Competidor dadosNovos) {
        Competidor existente = buscarPorId(id);
        existente.setNome(dadosNovos.getNome());
        existente.setTipo(dadosNovos.getTipo().toUpperCase());
        return competidorRepository.save(existente);
    }

    @Transactional
    public void deletar(Integer id) {
        Competidor competidor = buscarPorId(id);
        competidorRepository.delete(competidor);
    }
}
