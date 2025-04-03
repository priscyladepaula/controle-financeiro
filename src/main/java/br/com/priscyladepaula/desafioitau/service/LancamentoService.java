package br.com.priscyladepaula.desafioitau.service;

import br.com.priscyladepaula.desafioitau.domain.CategoriaEntity;
import br.com.priscyladepaula.desafioitau.domain.LancamentoEntity;
import br.com.priscyladepaula.desafioitau.domain.SubcategoriaEntity;
import br.com.priscyladepaula.desafioitau.dto.BalancoDTO;
import br.com.priscyladepaula.desafioitau.dto.CategoriaDTO;
import br.com.priscyladepaula.desafioitau.dto.LancamentoDTO;
import br.com.priscyladepaula.desafioitau.infrastructure.CategoriaRepository;
import br.com.priscyladepaula.desafioitau.infrastructure.LancamentoRepository;
import br.com.priscyladepaula.desafioitau.infrastructure.SubcategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public LancamentoService(LancamentoRepository lancamentoRepository, CategoriaRepository categoriaRepository, SubcategoriaRepository subcategoriaRepository) {
        this.lancamentoRepository = lancamentoRepository;
        this.categoriaRepository = categoriaRepository;
        this.subcategoriaRepository = subcategoriaRepository;
    }

    public List<LancamentoDTO> listarLancamentos() {
        return lancamentoRepository.findAll().stream().map(LancamentoDTO::new).collect(Collectors.toList());
    }

    public LancamentoDTO criarLancamento(LancamentoDTO lancamentoDTO) {
        SubcategoriaEntity subcategoria = subcategoriaRepository.findById(lancamentoDTO.getIdSubcategoria())
                .orElseThrow(() -> new NoSuchElementException("Subcategoria não encontrada"));

        if(lancamentoDTO.getValor() == null || lancamentoDTO.getValor().compareTo(BigDecimal.ZERO) == 0) {
            throw new IllegalArgumentException("O valor não pode ser zero.");
        }

        LancamentoEntity lancamento = new LancamentoEntity();
        lancamento.setSubcategoria(subcategoria);

        lancamento.setData(lancamentoDTO.getData() != null ? lancamentoDTO.getData() : LocalDate.now());

        lancamento.setValor(lancamentoDTO.getValor());
        lancamento.setComentario(lancamentoDTO.getComentario());

        lancamento = lancamentoRepository.save(lancamento);

        return new LancamentoDTO(lancamento);
    }

    public List<LancamentoDTO> buscarPorSubcategoria(Long idSubcategoria) {

        return lancamentoRepository.findBySubcategoriaId(idSubcategoria)
                .stream()
                .map(LancamentoDTO::new)
                .toList();
    }


    public LancamentoDTO editarLancamento(Long id, LancamentoDTO lancamentoDTO) {
        LancamentoEntity lancamento = lancamentoRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Lançamento não encontrado"));

        if(lancamentoDTO.getValor() != null) {
            lancamento.setValor(lancamentoDTO.getValor());
        }
        if(lancamentoDTO.getComentario() != null) {
            lancamento.setComentario(lancamentoDTO.getComentario());
        }
        if(lancamentoDTO.getData() != null) {
            lancamento.setData(lancamentoDTO.getData());
        }

        lancamento = lancamentoRepository.save(lancamento);
        return new LancamentoDTO(lancamento);

    }

    public BalancoDTO calcularBalanco(LocalDate dataInicial, LocalDate dataFinal, Long idCategoria) {
        List<LancamentoEntity> lancamentos;

        if (idCategoria != null) {
            lancamentos = lancamentoRepository.findBySubcategoriaIdAndDataBetween(idCategoria, dataInicial, dataFinal);
        } else {
            lancamentos = lancamentoRepository.findByDataBetween(dataInicial, dataFinal);
        }

        BigDecimal receita = lancamentos.stream()
                .filter(l -> l.getValor().compareTo(BigDecimal.ZERO) > 0)
                .map(LancamentoEntity::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal despesa = lancamentos.stream()
                .filter(l -> l.getValor().compareTo(BigDecimal.ZERO) < 0)
                .map(LancamentoEntity::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal saldo = despesa.add(receita);

        CategoriaDTO categoriaDTO = null;

        if (idCategoria != null) {
            CategoriaEntity categoriaEntity = categoriaRepository.findById(idCategoria).orElse(null);

            if(categoriaEntity != null) {
                categoriaDTO = new CategoriaDTO(categoriaEntity);
            }
        }

        return new BalancoDTO(categoriaDTO, receita, despesa, saldo);
    }

    public void excluirLancamento(Long id) {

        if (!lancamentoRepository.existsById(id)) {
            throw new NoSuchElementException("Lançamento não encontrado");
        }

        lancamentoRepository.deleteById(id);
    }

}
