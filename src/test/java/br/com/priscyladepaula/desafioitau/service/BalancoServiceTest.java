package br.com.priscyladepaula.desafioitau.service;

import br.com.priscyladepaula.desafioitau.domain.CategoriaEntity;
import br.com.priscyladepaula.desafioitau.domain.LancamentoEntity;
import br.com.priscyladepaula.desafioitau.dto.BalancoDTO;
import br.com.priscyladepaula.desafioitau.dto.CategoriaDTO;
import br.com.priscyladepaula.desafioitau.infrastructure.CategoriaRepository;
import br.com.priscyladepaula.desafioitau.infrastructure.LancamentoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BalancoServiceTest {

    @Mock
    private LancamentoRepository lancamentoRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private BalancoService balancoService;

    private CategoriaEntity categoriaEntity;

    private LocalDate dataInicial;
    private LocalDate dataFinal;
    private Long idCategoria;
    private CategoriaDTO categoriaDTO;
    private List<LancamentoEntity> lancamentosComReceitas;
    private List<LancamentoEntity> lancamentosComDespesas;
    private List<LancamentoEntity> lancamentosMistos;

    @BeforeEach
    void setUp() {
        dataInicial = LocalDate.of(2023, 1, 1);
        dataFinal = LocalDate.of(2023, 1, 31);
        idCategoria = 1L;

        categoriaDTO = new CategoriaDTO();
        categoriaDTO.setIdCategoria(idCategoria);
        categoriaDTO.setNome("Alimentação");

        LancamentoEntity receita1 = new LancamentoEntity();
        receita1.setValor(new BigDecimal("100.00"));
        receita1.setData(LocalDate.of(2023, 1, 15));

        LancamentoEntity receita2 = new LancamentoEntity();
        receita2.setValor(new BigDecimal("200.00"));
        receita2.setData(LocalDate.of(2023, 1, 20));

        lancamentosComReceitas = Arrays.asList(receita1, receita2);

        LancamentoEntity despesa1 = new LancamentoEntity();
        despesa1.setValor(new BigDecimal("-50.00"));
        despesa1.setData(LocalDate.of(2023, 1, 10));

        LancamentoEntity despesa2 = new LancamentoEntity();
        despesa2.setValor(new BigDecimal("-150.00"));
        despesa2.setData(LocalDate.of(2023, 1, 25));

        lancamentosComDespesas = Arrays.asList(despesa1, despesa2);

        lancamentosMistos = Arrays.asList(receita1, receita2, despesa1, despesa2);
    }

    @Test
    void deveCalcularBalancoSemCategoria() {

        when(lancamentoRepository.findByDataBetween(eq(dataInicial), eq(dataFinal)))
                .thenReturn(lancamentosMistos);

        BalancoDTO resultado = balancoService.calcularBalanco(dataInicial, dataFinal, null);

        assertNotNull(resultado);
        assertNull(resultado.getCategoria());
        assertEquals(new BigDecimal("300.00"), resultado.getReceita());
        assertEquals(new BigDecimal("200.00"), resultado.getDespesa());

        verify(lancamentoRepository).findByDataBetween(dataInicial, dataFinal);
        verifyNoMoreInteractions(lancamentoRepository);
        verifyNoInteractions(categoriaRepository);
    }

    @Test
    void deveRetornarReceitaZeroQuandoNaoHaLancamentosPositivos() {

        when(lancamentoRepository.findByDataBetween(eq(dataInicial), eq(dataFinal)))
                .thenReturn(lancamentosComDespesas);

        BalancoDTO resultado = balancoService.calcularBalanco(dataInicial, dataFinal, null);

        assertNotNull(resultado);
        assertEquals(BigDecimal.ZERO, resultado.getReceita());
        assertEquals(new BigDecimal("200.00"), resultado.getDespesa());
    }

    @Test
    void deveRetornarDespesaZeroQuandoNaoHaLancamentosNegativos() {
        // Arrange
        when(lancamentoRepository.findByDataBetween(eq(dataInicial), eq(dataFinal)))
                .thenReturn(lancamentosComReceitas);

        // Act
        BalancoDTO resultado = balancoService.calcularBalanco(dataInicial, dataFinal, null);

        // Assert
        assertNotNull(resultado);
        assertEquals(new BigDecimal("300.00"), resultado.getReceita());
        assertEquals(BigDecimal.ZERO, resultado.getDespesa());
    }

    @Test
    void deveRetornarReceitaEDespesaZeroQuandoNaoHaLancamentos() {

        when(lancamentoRepository.findByDataBetween(eq(dataInicial), eq(dataFinal)))
                .thenReturn(Collections.emptyList());

        BalancoDTO resultado = balancoService.calcularBalanco(dataInicial, dataFinal, null);

        assertNotNull(resultado);
        assertEquals(BigDecimal.ZERO, resultado.getReceita());
        assertEquals(BigDecimal.ZERO, resultado.getDespesa());
    }

    @Test
    void deveRetornarCategoriaNullQuandoIdCategoriaNaoExiste() {
        // Arrange
        when(categoriaRepository.findById(eq(idCategoria))).thenReturn(Optional.empty());
        when(lancamentoRepository.findBySubcategoriaIdAndDataBetween(
                eq(idCategoria), eq(dataInicial), eq(dataFinal))).thenReturn(lancamentosMistos);

        // Act
        BalancoDTO resultado = balancoService.calcularBalanco(dataInicial, dataFinal, idCategoria);

        // Assert
        assertNotNull(resultado);
        assertNull(resultado.getCategoria());
        assertEquals(new BigDecimal("300.00"), resultado.getReceita());
        assertEquals(new BigDecimal("200.00"), resultado.getDespesa());
    }

    @Test
    void deveLidarComValoresDecimaisCorretamente() {

        LancamentoEntity receita = new LancamentoEntity();
        receita.setValor(new BigDecimal("123.45"));

        LancamentoEntity despesa = new LancamentoEntity();
        despesa.setValor(new BigDecimal("-67.89"));

        when(lancamentoRepository.findByDataBetween(eq(dataInicial), eq(dataFinal)))
                .thenReturn(Arrays.asList(receita, despesa));

        BalancoDTO resultado = balancoService.calcularBalanco(dataInicial, dataFinal, null);

        assertNotNull(resultado);
        assertEquals(new BigDecimal("123.45"), resultado.getReceita());
        assertEquals(new BigDecimal("67.89"), resultado.getDespesa());
    }
}
