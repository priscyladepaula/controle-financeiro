package br.com.priscyladepaula.desafioitau.service;

import br.com.priscyladepaula.desafioitau.domain.CategoriaEntity;
import br.com.priscyladepaula.desafioitau.domain.SubcategoriaEntity;
import br.com.priscyladepaula.desafioitau.dto.SubcategoriaDTO;
import br.com.priscyladepaula.desafioitau.infrastructure.CategoriaRepository;
import br.com.priscyladepaula.desafioitau.infrastructure.LancamentoRepository;
import br.com.priscyladepaula.desafioitau.infrastructure.SubcategoriaRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class SubcategoriaService {

    private final SubcategoriaRepository subcategoriaRepository;
    private final CategoriaRepository categoriaRepository;
    private final LancamentoRepository lancamentoRepository;

    public SubcategoriaService(SubcategoriaRepository subcategoriaRepository, CategoriaRepository categoriaRepository, LancamentoRepository lancamentoRepository) {
        this.subcategoriaRepository = subcategoriaRepository;
        this.categoriaRepository = categoriaRepository;
        this.lancamentoRepository = lancamentoRepository;
    }

    public List<SubcategoriaDTO> listarSubcategorias() {
        List<SubcategoriaEntity> subcategorias = subcategoriaRepository.findAll();

        return subcategorias.stream().map(SubcategoriaDTO::new).collect(Collectors.toList());
    }

    public SubcategoriaDTO criarSubcategoria(SubcategoriaDTO subcategoriaDTO) {
        CategoriaEntity categoriaEntity = categoriaRepository.findById(subcategoriaDTO.getIdCategoria())
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        SubcategoriaEntity subcategoria = new SubcategoriaEntity();
        subcategoria.setNome(subcategoriaDTO.getNome());
        subcategoria.setCategoria(categoriaEntity);

        subcategoria = subcategoriaRepository.save(subcategoria);

        return new SubcategoriaDTO(subcategoria);
    }

    public SubcategoriaDTO buscarSubcategoriaPorId(Long id) {
        SubcategoriaEntity subcategoria = subcategoriaRepository.findById(id).get();

        return new SubcategoriaDTO(subcategoria);
    }

    public List<SubcategoriaDTO> buscarPorNome(String nome) {
        return subcategoriaRepository.findByNomeIgnoreCase(nome)
                .map(SubcategoriaDTO::new)
                .stream().toList();
    }

    public SubcategoriaDTO editarSubcategoria(Long idSubcategoria, SubcategoriaDTO subcategoriaDTO) {
        SubcategoriaEntity subcategoria = subcategoriaRepository.findById(idSubcategoria)
                .orElseThrow(() -> new NoSuchElementException("Subcategoria não encontrada"));

        subcategoria.setNome(subcategoriaDTO.getNome());
        subcategoriaRepository.save(subcategoria);

        return new SubcategoriaDTO(subcategoria);
    }

    public void deletarSubcategoria(Long idSubcategoria) {
        if (lancamentoRepository.existsBySubcategoriaId(idSubcategoria)) {
            throw new DataIntegrityViolationException("Não é possível excluir a subcategoria, pois há lançamentos atrelados a ela.");
        }
        subcategoriaRepository.deleteById(idSubcategoria);
    }

}
