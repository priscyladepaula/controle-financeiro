package br.com.priscyladepaula.desafioitau.service;

import br.com.priscyladepaula.desafioitau.domain.CategoriaEntity;
import br.com.priscyladepaula.desafioitau.domain.LancamentoEntity;
import br.com.priscyladepaula.desafioitau.domain.SubcategoriaEntity;
import br.com.priscyladepaula.desafioitau.dto.BalancoDTO;
import br.com.priscyladepaula.desafioitau.dto.CategoriaDTO;
import br.com.priscyladepaula.desafioitau.dto.LancamentoDTO;
import br.com.priscyladepaula.desafioitau.exception.CustomException;
import br.com.priscyladepaula.desafioitau.exception.NotFoundException;
import br.com.priscyladepaula.desafioitau.exception.ValidationException;
import br.com.priscyladepaula.desafioitau.infrastructure.CategoriaRepository;
import br.com.priscyladepaula.desafioitau.infrastructure.LancamentoRepository;
import br.com.priscyladepaula.desafioitau.infrastructure.SubcategoriaRepository;
import br.com.priscyladepaula.desafioitau.mapper.LancamentoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LancamentoService {

    private final LancamentoRepository lancamentoRepository;
    private final CategoriaRepository categoriaRepository;
    private final SubcategoriaRepository subcategoriaRepository;
    private final LancamentoMapper lancamentoMapper;

    public LancamentoService(LancamentoRepository lancamentoRepository, CategoriaRepository categoriaRepository, SubcategoriaRepository subcategoriaRepository, LancamentoMapper lancamentoMapper) {
        this.lancamentoRepository = lancamentoRepository;
        this.categoriaRepository = categoriaRepository;
        this.subcategoriaRepository = subcategoriaRepository;
        this.lancamentoMapper = lancamentoMapper;
    }

    public List<LancamentoDTO> listarLancamentos() {

        List<LancamentoEntity> lancamentos = lancamentoRepository.findAll();

        return lancamentoMapper.toDtoList(lancamentos);
    }

    public LancamentoDTO criarLancamento(LancamentoDTO lancamentoDTO) {

        LancamentoEntity lancamento = lancamentoMapper.toEntity(lancamentoDTO);

        SubcategoriaEntity subcategoria = subcategoriaRepository.findById(lancamentoDTO.getIdSubcategoria())
                .orElseThrow(() -> new NotFoundException("Subcategoria não encontrada"));

        if(lancamentoDTO.getValor() == null || lancamentoDTO.getValor().compareTo(BigDecimal.ZERO) == 0) {
            throw new ValidationException("O valor não pode ser zero.");
        }

        LancamentoEntity lancamentoSalvo = lancamentoRepository.save(lancamento);

        return lancamentoMapper.toDto(lancamentoSalvo);
    }

    public LancamentoDTO buscarPorSubcategoria(Long idSubcategoria) {

        LancamentoEntity lancamento = lancamentoRepository.findBySubcategoriaId(idSubcategoria)
                .orElseThrow(() -> new NotFoundException("Lançamento não encontrado!"));

        return lancamentoMapper.toDto(lancamento);
    }


    public LancamentoDTO editarLancamento(Long id, LancamentoDTO lancamentoDTO) {

        LancamentoEntity existente = lancamentoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Lançamento não encontrado!"));

        SubcategoriaEntity subcategoria = subcategoriaRepository.findById(lancamentoDTO.getIdSubcategoria())
                .orElseThrow(() -> new NotFoundException("Subcategoria não encontrada!"));

        existente.setValor(lancamentoDTO.getValor());
        existente.setComentario(lancamentoDTO.getComentario());
        existente.setData(lancamentoDTO.getData());
        existente.setSubcategoria(subcategoria);

        LancamentoEntity lancamentoSalvo = lancamentoRepository.save(existente);

        return lancamentoMapper.toDto(lancamentoSalvo);

    }

    public BalancoDTO calcularBalanco(LocalDate dataInicial, LocalDate dataFinal, Long idCategoria) {

        //If ternário
        List<LancamentoEntity> lancamentos = (idCategoria != null)
                ? lancamentoRepository.findBySubcategoriaIdAndDataBetween(idCategoria, dataInicial, dataFinal)
                : lancamentoRepository.findByDataBetween(dataInicial, dataFinal);

        BigDecimal receita = lancamentos.stream()
                .map(LancamentoEntity::getValor)
                .filter(valor -> valor.compareTo(BigDecimal.ZERO) > 0)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal despesa = lancamentos.stream()
                .map(LancamentoEntity::getValor)
                .filter(valor -> valor.compareTo(BigDecimal.ZERO) < 0)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal saldo = despesa.add(receita);

        CategoriaDTO categoriaDTO = idCategoria != null
                ? categoriaRepository.findById(idCategoria)
                .map(CategoriaDTO::new)
                .orElse(null) : null;

        return new BalancoDTO(categoriaDTO, receita, despesa, saldo);
    }

    public void excluirLancamento(Long id) {

        LancamentoEntity lancamento = lancamentoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Lançamento não encontrado!"));

        lancamentoRepository.delete(lancamento);
    }

}
