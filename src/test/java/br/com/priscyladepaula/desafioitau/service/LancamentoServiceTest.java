package br.com.priscyladepaula.desafioitau.service;

import br.com.priscyladepaula.desafioitau.domain.CategoriaEntity;
import br.com.priscyladepaula.desafioitau.domain.LancamentoEntity;
import br.com.priscyladepaula.desafioitau.domain.SubcategoriaEntity;
import br.com.priscyladepaula.desafioitau.dto.BalancoDTO;
import br.com.priscyladepaula.desafioitau.dto.LancamentoDTO;
import br.com.priscyladepaula.desafioitau.infrastructure.CategoriaRepository;
import br.com.priscyladepaula.desafioitau.infrastructure.LancamentoRepository;
import br.com.priscyladepaula.desafioitau.infrastructure.SubcategoriaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.LenientStubber;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
public class LancamentoServiceTest {

    @InjectMocks
    private LancamentoService lancamentoService;

    @Mock
    private LancamentoRepository lancamentoRepository;

    @Mock
    private SubcategoriaRepository subcategoriaRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Test
    void deveCriarLancamentoComSucesso() {
        LancamentoDTO lancamentoDTO = new LancamentoDTO();
        lancamentoDTO.setValor(new BigDecimal("100.00"));
        lancamentoDTO.setData(LocalDate.parse("03/04/2025", formatter));
        lancamentoDTO.setIdSubcategoria(1L);
        lancamentoDTO.setComentario("Teste");

        SubcategoriaEntity subcategoria = new SubcategoriaEntity();
        subcategoria.setId(1L);

        LancamentoEntity lancamentoSalvo = new LancamentoEntity();
        lancamentoSalvo.setId(1L);
        lancamentoSalvo.setValor(lancamentoDTO.getValor());
        lancamentoSalvo.setData(lancamentoDTO.getData());
        lancamentoSalvo.setComentario(lancamentoDTO.getComentario());
        lancamentoSalvo.setSubcategoria(subcategoria);

        when(subcategoriaRepository.findById(lancamentoDTO.getIdSubcategoria())).thenReturn(Optional.of(subcategoria));
        when(lancamentoRepository.save(any(LancamentoEntity.class))).thenReturn(lancamentoSalvo);

        LancamentoDTO resultado = lancamentoService.criarLancamento(lancamentoDTO);

        assertEquals(new BigDecimal("100.00"), resultado.getValor());
        assertEquals("Teste", resultado.getComentario());
        verify(lancamentoRepository, times(1)).save(any(LancamentoEntity.class));
    }

    @Test
    void deveLancarExcecaoAoCriarLancamentoComSubcategoriaInexistente() {
        LancamentoDTO lancamentoDTO = new LancamentoDTO();
        lancamentoDTO.setIdSubcategoria(1L);

        when(subcategoriaRepository.findById(lancamentoDTO.getIdSubcategoria())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> lancamentoService.criarLancamento(lancamentoDTO));
    }

    @Test
    void deveEditarLancamentoComSucesso() {
        Long id = 1L;

        SubcategoriaEntity subcategoria = new SubcategoriaEntity();
        subcategoria.setId(2L); // Definindo um ID para evitar NullPointerException

        LancamentoEntity lancamentoExistente = new LancamentoEntity();
        lancamentoExistente.setId(id);
        lancamentoExistente.setValor(new BigDecimal("100.00"));
        lancamentoExistente.setData(LocalDate.parse("03/04/2025", formatter));
        lancamentoExistente.setComentario("Teste");
        lancamentoExistente.setSubcategoria(subcategoria); // Associando a subcategoria

        LancamentoDTO lancamentoDTO = new LancamentoDTO();
        lancamentoDTO.setValor(new BigDecimal("200.00"));
        lancamentoDTO.setData(LocalDate.parse("03/04/2025", formatter));
        lancamentoDTO.setComentario("Editado");

        when(lancamentoRepository.findById(id)).thenReturn(Optional.of(lancamentoExistente));
        when(lancamentoRepository.save(any(LancamentoEntity.class))).thenReturn(lancamentoExistente);

        LancamentoDTO resultado = lancamentoService.editarLancamento(id, lancamentoDTO);

        assertEquals(new BigDecimal("200.00"), resultado.getValor()); // Corrigindo o valor esperado
        assertEquals("Editado", resultado.getComentario());
        verify(lancamentoRepository, times(1)).save(any(LancamentoEntity.class));
    }

    @Test
    void deveLancarExcecaoQuandoEditarLancamentoInexistente() {
        Long idLancamento = 999L;
        LancamentoDTO lancamentoDTO = new LancamentoDTO();
        lancamentoDTO.setValor(new BigDecimal("150.00"));
        lancamentoDTO.setData(LocalDate.parse("2025-04-04"));
        lancamentoDTO.setComentario("Teste");

        when(lancamentoRepository.findById(idLancamento)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () ->
                lancamentoService.editarLancamento(idLancamento, lancamentoDTO)
        );

        assertEquals("Lançamento não encontrado", exception.getMessage());
        verify(lancamentoRepository, never()).save(any(LancamentoEntity.class));
    }

    @Test
    void deveDeletarLancamentoComSucesso() {
        Long idLancamento = 1L;

        when(lancamentoRepository.existsById(idLancamento)).thenReturn(true);
        doNothing().when(lancamentoRepository).deleteById(idLancamento);

        lancamentoService.excluirLancamento(idLancamento);

        verify(lancamentoRepository, times(1)).deleteById(idLancamento);
    }

    @Test
    void deveLancarExcecaoQuandoLancamentoNaoExiste() {
        Long idLancamento = 1L;
        when(lancamentoRepository.existsById(idLancamento)).thenReturn(false);

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () ->
                lancamentoService.excluirLancamento(idLancamento)
        );

        assertEquals("Lançamento não encontrado", exception.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoTentarDeletarLancamentoInexistente() {
        // Arrange
        Long idLancamento = 99L;
        when(lancamentoRepository.existsById(idLancamento)).thenReturn(false);

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> {
            lancamentoService.excluirLancamento(idLancamento);
        });

        // Verifica que o método deleteById nunca foi chamado
        verify(lancamentoRepository, never()).deleteById(anyLong());
    }

    @Test
    void deveDeletarLancamentoMesmoSemSubcategoria() {
        Long idLancamento = 2L;

        when(lancamentoRepository.existsById(idLancamento)).thenReturn(true);

        lancamentoService.excluirLancamento(idLancamento);

        verify(lancamentoRepository, times(1)).deleteById(idLancamento);
    }

    @Test
    void deveRetornarBalancoZeradoQuandoNaoHaLancamentos() {
        LocalDate dataInicial = LocalDate.of(2025, 4, 1);
        LocalDate dataFinal = LocalDate.of(2025, 4, 30);

        when(lancamentoRepository.findByDataBetween(dataInicial, dataFinal)).thenReturn(Collections.emptyList());

        BalancoDTO resultado = lancamentoService.calcularBalanco(dataInicial, dataFinal, null);

        assertEquals(BigDecimal.ZERO, resultado.getReceita());
        assertEquals(BigDecimal.ZERO, resultado.getDespesa());
        assertEquals(BigDecimal.ZERO, resultado.getSaldo());
    }

    @Test
    void deveRetornarBalancoComCategoriaNulaQuandoNaoExisteCategoria() {
        LocalDate dataInicial = LocalDate.of(2025, 4, 1);
        LocalDate dataFinal = LocalDate.of(2025, 4, 30);
        Long idCategoria = 99L;

        when(lancamentoRepository.findBySubcategoriaIdAndDataBetween(idCategoria, dataInicial, dataFinal)).thenReturn(Collections.emptyList());
        when(categoriaRepository.findById(idCategoria)).thenReturn(Optional.empty());

        BalancoDTO resultado = lancamentoService.calcularBalanco(dataInicial, dataFinal, idCategoria);

        assertNull(resultado.getCategoria());
        assertEquals(BigDecimal.ZERO, resultado.getReceita());
        assertEquals(BigDecimal.ZERO, resultado.getDespesa());
        assertEquals(BigDecimal.ZERO, resultado.getSaldo());
    }
}

