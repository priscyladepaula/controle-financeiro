package br.com.priscyladepaula.desafioitau.service;

import br.com.priscyladepaula.desafioitau.domain.CategoriaEntity;
import br.com.priscyladepaula.desafioitau.dto.CategoriaDTO;
import br.com.priscyladepaula.desafioitau.infrastructure.CategoriaRepository;
import br.com.priscyladepaula.desafioitau.mapper.CategoriaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final CategoriaMapper categoriaMapper;

    public CategoriaService(CategoriaRepository categoriaRepository, CategoriaMapper categoriaMapper) {
        this.categoriaRepository = categoriaRepository;
        this.categoriaMapper = categoriaMapper;
    }

    public boolean existsByNome(String nome) {
        return categoriaRepository.existsByNome(nome);
    }

    public CategoriaDTO criarCategoria(CategoriaDTO categoriaDTO) {

        CategoriaEntity categoria = categoriaMapper.toEntity(categoriaDTO);
        CategoriaEntity categoriaSalva = categoriaRepository.save(categoria);

        return categoriaMapper.toDto(categoriaSalva);
    }

    public List<CategoriaDTO> listarCategorias() {

        List<CategoriaEntity> categorias = categoriaRepository.findAll();

        return categoriaMapper.toDtoList(categorias);
    }

    public CategoriaDTO buscarCategoriaPorId(Long id) {
        CategoriaEntity categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria não encontrada"));

        return categoriaMapper.toDto(categoria);
    }

    public CategoriaDTO buscarCategoriaPorNome(String nome) {

        CategoriaEntity categoria = categoriaRepository.findByNomeIgnoreCase(nome)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria não encontrada"));

        return categoriaMapper.toDto(categoria);
    }


    public CategoriaDTO editarCategoria(Long id, CategoriaDTO categoriaDTO) {

        CategoriaEntity existente = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Categoria não encontrada!"));

        existente.setNome(categoriaDTO.getNome());

        return categoriaMapper.toDto(categoriaRepository.save(existente));
    }

    public void excluirCategoria(Long id) {
        if (!categoriaRepository.existsById(id)) {
            throw new NoSuchElementException("Categoria não encontrada");
        }

        categoriaRepository.deleteById(id);
    }

}
