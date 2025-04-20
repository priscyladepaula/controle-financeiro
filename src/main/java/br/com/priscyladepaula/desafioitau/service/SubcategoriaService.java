package br.com.priscyladepaula.desafioitau.service;

import br.com.priscyladepaula.desafioitau.domain.SubcategoriaEntity;
import br.com.priscyladepaula.desafioitau.dto.SubcategoriaDTO;
import br.com.priscyladepaula.desafioitau.exception.DuplicationException;
import br.com.priscyladepaula.desafioitau.exception.NotFoundException;
import br.com.priscyladepaula.desafioitau.exception.InvalidOperationException;
import br.com.priscyladepaula.desafioitau.exception.ValidationException;
import br.com.priscyladepaula.desafioitau.infrastructure.LancamentoRepository;
import br.com.priscyladepaula.desafioitau.infrastructure.SubcategoriaRepository;
import br.com.priscyladepaula.desafioitau.mapper.SubcategoriaMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubcategoriaService {

    private final SubcategoriaRepository subcategoriaRepository;
    private final SubcategoriaMapper subcategoriaMapper;
    private final LancamentoRepository lancamentoRepository;

    public SubcategoriaService(SubcategoriaRepository subcategoriaRepository, LancamentoRepository lancamentoRepository, SubcategoriaMapper subcategoriaMapper) {
        this.subcategoriaRepository = subcategoriaRepository;
        this.lancamentoRepository = lancamentoRepository;
        this.subcategoriaMapper = subcategoriaMapper;
    }

    public SubcategoriaDTO criarSubcategoria(SubcategoriaDTO subcategoriaDTO) {

        if(subcategoriaRepository.existsByNome(subcategoriaDTO.getNome())){
            throw new DuplicationException("Já existe subcategoria com este nome!");
        } if(subcategoriaDTO.getNome() == null || subcategoriaDTO.getNome().isEmpty()){
            throw new ValidationException("O campo 'nome' é obrigatório.");
        } if(subcategoriaDTO.getIdCategoria() == null || subcategoriaDTO.getIdCategoria() == 0){
            throw new ValidationException("Selecione uma categoria existente!");
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
