package br.com.priscyladepaula.desafioitau.service;

import br.com.priscyladepaula.desafioitau.domain.CategoriaEntity;
import br.com.priscyladepaula.desafioitau.dto.CategoriaDTO;
import br.com.priscyladepaula.desafioitau.infrastructure.CategoriaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoriaService {

    private CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public boolean existsByNome(String nome) {
        return categoriaRepository.existsByNome(nome);
    }

    public CategoriaDTO criarCategoria(CategoriaDTO categoriaDTO) {

        CategoriaEntity categoria = new CategoriaEntity(categoriaDTO);

        categoria = categoriaRepository.save(categoria);

        return new CategoriaDTO(categoria);
    }

    public List<CategoriaDTO> listarCategorias() {
        return categoriaRepository.findAll()
                .stream()
                .map(CategoriaDTO::new)
                .toList();
    }

    public Optional<CategoriaDTO> buscarCategoriaPorId(Long id) {
        return categoriaRepository.findById(id)
                .map(CategoriaDTO::new);
    }

    public List<CategoriaDTO> buscarCategoriaPorNome(String nome) {

        List<CategoriaEntity> categorias;

        if (nome != null && !nome.isBlank()) {
            categorias = categoriaRepository.findByNomeIgnoreCase(nome);
        } else {
            categorias = categoriaRepository.findAll();
        }

        return categorias.stream()
                .map(CategoriaDTO::new)
                .collect(Collectors.toList());
    }


    public Optional<CategoriaDTO> editarCategoria(Long id, CategoriaDTO categoriaDTO) {
        return categoriaRepository.findById(id).map(categoria -> {
            categoria.setNome(categoriaDTO.getNome());
            categoriaRepository.save(categoria);
            return new CategoriaDTO(categoria);
        });
    }

    public void excluirCategoria(Long id) {
        // Verifica se a categoria existe
        if (!categoriaRepository.existsById(id)) {
            throw new NoSuchElementException("Categoria não encontrada");
        }

        categoriaRepository.deleteById(id);
    }

}
