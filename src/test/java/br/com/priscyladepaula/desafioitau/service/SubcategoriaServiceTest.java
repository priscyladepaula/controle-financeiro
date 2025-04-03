package br.com.priscyladepaula.desafioitau.service;

import br.com.priscyladepaula.desafioitau.domain.CategoriaEntity;
import br.com.priscyladepaula.desafioitau.domain.SubcategoriaEntity;
import br.com.priscyladepaula.desafioitau.dto.SubcategoriaDTO;
import br.com.priscyladepaula.desafioitau.infrastructure.LancamentoRepository;
import br.com.priscyladepaula.desafioitau.infrastructure.SubcategoriaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.InjectMocks;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SubcategoriaServiceTest {

    @InjectMocks
    private SubcategoriaService subcategoriaService;

    @Mock
    private SubcategoriaRepository subcategoriaRepository;

    @Mock
    private LancamentoRepository lancamentoRepository;

    @Test
    void deveDeletarSubcategoriaQuandoNaoHaLancamentos() {
        Long idSubcategoria = 1L;

        when(lancamentoRepository.existsBySubcategoriaId(idSubcategoria)).thenReturn(false);
        doNothing().when(subcategoriaRepository).deleteById(idSubcategoria);

        subcategoriaService.deletarSubcategoria(idSubcategoria);

        verify(subcategoriaRepository, times(1)).deleteById(idSubcategoria);
    }

    @Test
    void deveLancarExcecaoAoDeletarSubcategoriaComLancamentosVinculados() {
        Long idSubcategoria = 1L;
        when(lancamentoRepository.existsBySubcategoriaId(idSubcategoria)).thenReturn(true);

        assertThrows(DataIntegrityViolationException.class, () -> subcategoriaService.deletarSubcategoria(idSubcategoria));
    }

    @Test
    void deveEditarSubcategoriaComSucesso() {
        Long idSubcategoria = 1L;
        SubcategoriaDTO subcategoriaDTO = new SubcategoriaDTO();
        subcategoriaDTO.setNome("Nova Subcategoria");

        SubcategoriaEntity subcategoria = new SubcategoriaEntity();
        subcategoria.setId(idSubcategoria);
        subcategoria.setNome("Subcategoria Antiga");

        when(subcategoriaRepository.findById(idSubcategoria)).thenReturn(Optional.of(subcategoria));
        when(subcategoriaRepository.save(any(SubcategoriaEntity.class))).thenReturn(subcategoria);

        SubcategoriaDTO resultado = subcategoriaService.editarSubcategoria(idSubcategoria, subcategoriaDTO);

        assertEquals("Nova Subcategoria", resultado.getNome());
        verify(subcategoriaRepository, times(1)).save(any(SubcategoriaEntity.class));
    }

    @Test
    void deveLancarExcecaoAoEditarSubcategoriaInexistente() {
        Long idSubcategoria = 1L;
        SubcategoriaDTO subcategoriaDTO = new SubcategoriaDTO();
        subcategoriaDTO.setNome("Teste");

        when(subcategoriaRepository.findById(idSubcategoria)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> subcategoriaService.editarSubcategoria(idSubcategoria, subcategoriaDTO));
    }
}
