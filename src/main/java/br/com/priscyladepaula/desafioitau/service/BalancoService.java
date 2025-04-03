package br.com.priscyladepaula.desafioitau.service;

import br.com.priscyladepaula.desafioitau.domain.CategoriaEntity;
import br.com.priscyladepaula.desafioitau.domain.LancamentoEntity;
import br.com.priscyladepaula.desafioitau.dto.BalancoDTO;
import br.com.priscyladepaula.desafioitau.dto.CategoriaDTO;
import br.com.priscyladepaula.desafioitau.infrastructure.CategoriaRepository;
import br.com.priscyladepaula.desafioitau.infrastructure.LancamentoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class BalancoService {

    private final LancamentoRepository lancamentoRepository;
    private final CategoriaRepository categoriaRepository;

    public BalancoService(LancamentoRepository lancamentoRepository, CategoriaRepository categoriaRepository) {
        this.lancamentoRepository = lancamentoRepository;
        this.categoriaRepository = categoriaRepository;
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
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .abs();

        CategoriaDTO categoriaDTO = null;

        if (idCategoria != null) {
            categoriaDTO = categoriaRepository.findById(idCategoria)
                    .map(CategoriaDTO::new)
                    .orElse(null);
        }

        return new BalancoDTO(categoriaDTO, receita, despesa);
    }
}
