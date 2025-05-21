package br.com.priscyladepaula.desafioitau.service;

import br.com.priscyladepaula.desafioitau.domain.CategoriaEntity;
import br.com.priscyladepaula.desafioitau.domain.SubcategoriaEntity;
import br.com.priscyladepaula.desafioitau.dto.SubcategoriaDTO;
import br.com.priscyladepaula.desafioitau.exception.DuplicationException;
import br.com.priscyladepaula.desafioitau.exception.NotFoundException;
import br.com.priscyladepaula.desafioitau.exception.InvalidOperationException;
import br.com.priscyladepaula.desafioitau.infrastructure.CategoriaRepository;
import br.com.priscyladepaula.desafioitau.infrastructure.LancamentoRepository;
import br.com.priscyladepaula.desafioitau.infrastructure.SubcategoriaRepository;
import br.com.priscyladepaula.desafioitau.mapper.SubcategoriaMapper;
import br.com.priscyladepaula.desafioitau.validation.ValidationRules;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubcategoriaService {

    private final SubcategoriaRepository subcategoriaRepository;
    private final SubcategoriaMapper subcategoriaMapper;
    private final LancamentoRepository lancamentoRepository;
    private final CategoriaRepository categoriaRepository;

    public SubcategoriaService(SubcategoriaRepository subcategoriaRepository, LancamentoRepository lancamentoRepository, SubcategoriaMapper subcategoriaMapper, CategoriaRepository categoriaRepository) {
        this.subcategoriaRepository = subcategoriaRepository;
        this.lancamentoRepository = lancamentoRepository;
        this.subcategoriaMapper = subcategoriaMapper;
        this.categoriaRepository = categoriaRepository;
    }

    public SubcategoriaDTO criarSubcategoria(SubcategoriaDTO subcategoriaDTO) {

        CategoriaEntity existente = categoriaRepository.findById(subcategoriaDTO.getIdCategoria())
                .orElseThrow(() -> new NotFoundException("Categoria não encontrada!"));

        ValidationRules.com(subcategoriaDTO)
                .notEmpty(SubcategoriaDTO::getNome, "O campo 'nome' é obrigatório.")
                .validId(SubcategoriaDTO::getIdCategoria, "Selecione uma categoria existente!")
                .execute();

        if(subcategoriaRepository.existsByNome(subcategoriaDTO.getNome())){
            throw new DuplicationException("Já existe subcategoria com este nome!");
        }

        SubcategoriaEntity subcategoria = subcategoriaMapper.toEntity(subcategoriaDTO);
        SubcategoriaEntity subcategoriaSalva = subcategoriaRepository.save(subcategoria);

        return subcategoriaMapper.toDto(subcategoriaSalva);
    }

    public List<SubcategoriaDTO> listarSubcategorias() {

        List<SubcategoriaEntity> subcategorias = subcategoriaRepository.findAll();

        return subcategoriaMapper.toDtoList(subcategorias);
    }

    public SubcategoriaDTO buscarSubcategoriaPorId(Long id) {

        SubcategoriaEntity subcategoria = subcategoriaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Subcategoria não encontrada!"));

        return subcategoriaMapper.toDto(subcategoria);
    }

    public SubcategoriaDTO buscarPorNome(String nome) {

        SubcategoriaEntity subcategoria = subcategoriaRepository.findByNomeIgnoreCase(nome)
                .orElseThrow(() -> new NotFoundException("Subcategoria não encontrada!"));

        return subcategoriaMapper.toDto(subcategoria);
    }

    public SubcategoriaDTO editarSubcategoria(Long id, SubcategoriaDTO subcategoriaDTO) {

        SubcategoriaEntity existente = subcategoriaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Subcategoria não encontrada!"));

        ValidationRules.com(subcategoriaDTO)
                .notEmpty(SubcategoriaDTO::getNome, "O campo 'nome' é obrigatório.")
                .validId(SubcategoriaDTO::getIdCategoria, "Selecione uma categoria existente!")
                .execute();

        if(subcategoriaRepository.existsByNome(subcategoriaDTO.getNome())){
            throw new DuplicationException("Já existe subcategoria com este nome!");
        }

        existente.setNome(subcategoriaDTO.getNome());

        return subcategoriaMapper.toDto(subcategoriaRepository.save(existente));
    }

    public void excluirSubcategoria(Long id) {

        if (lancamentoRepository.existsBySubcategoriaId(id)) {
            throw new InvalidOperationException("Não é possível excluir a subcategoria, pois há lançamentos vinculados a ela.");
        }

        subcategoriaRepository.deleteById(id);
    }

}
