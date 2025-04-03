package br.com.priscyladepaula.desafioitau.service;

import br.com.priscyladepaula.desafioitau.domain.CategoriaEntity;
import br.com.priscyladepaula.desafioitau.dto.CategoriaDTO;
import br.com.priscyladepaula.desafioitau.infrastructure.CategoriaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoriaServiceTest {

    @InjectMocks
    private CategoriaService categoriaService;

    @Mock
    private CategoriaRepository categoriaRepository;

    @Test
    void deveDeletarCategoria() {
        Long id = 1L;

        lenient().when(categoriaRepository.existsById(id)).thenReturn(true);

        categoriaService.excluirCategoria(id);

        verify(categoriaRepository, times(1)).deleteById(id);
    }

    @Test
    void deveVerificarSeExistePorNome() {

        String nome = "Alimentação";
        when(categoriaRepository.existsByNome(nome)).thenReturn(true);

        boolean resultado = categoriaService.existsByNome(nome);

        assertTrue(resultado);
        verify(categoriaRepository).existsByNome(nome);
    }

    @Test
    void deveVerificarSeNaoExistePorNome() {

        String nome = "";
        when(categoriaRepository.existsByNome(nome)).thenReturn(false);

        boolean resultado = categoriaService.existsByNome(nome);

        assertFalse(resultado);
        verify(categoriaRepository).existsByNome(nome);
    }

    @Test
    void deveCriarCategoria() {
        // Arrange
        CategoriaDTO categoriaDTO = new CategoriaDTO();
        categoriaDTO.setNome("Nova Categoria");

        CategoriaEntity categoriaEntity = new CategoriaEntity(categoriaDTO);
        CategoriaEntity categoriaSalva = new CategoriaEntity(categoriaDTO);
        categoriaSalva.setId(1L);

        when(categoriaRepository.save(any(CategoriaEntity.class))).thenReturn(categoriaSalva);

        // Act
        CategoriaDTO resultado = categoriaService.criarCategoria(categoriaDTO);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdCategoria());
        assertEquals("Nova Categoria", resultado.getNome());
        verify(categoriaRepository).save(any(CategoriaEntity.class));
    }

    @Test
    void deveListarTodasCategorias() {
        // Arrange
        CategoriaEntity categoria1 = new CategoriaEntity();
        categoria1.setId(1L);
        categoria1.setNome("Categoria 1");

        CategoriaEntity categoria2 = new CategoriaEntity();
        categoria2.setId(2L);
        categoria2.setNome("Categoria 2");

        when(categoriaRepository.findAll()).thenReturn(Arrays.asList(categoria1, categoria2));

        // Act
        List<CategoriaDTO> resultado = categoriaService.listarCategorias();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(1L, resultado.get(0).getIdCategoria());
        assertEquals("Categoria 1", resultado.get(0).getNome());
        assertEquals(2L, resultado.get(1).getIdCategoria());
        assertEquals("Categoria 2", resultado.get(1).getNome());
        verify(categoriaRepository).findAll();
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHaCategorias() {

        when(categoriaRepository.findAll()).thenReturn(Collections.emptyList());

        List<CategoriaDTO> resultado = categoriaService.listarCategorias();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(categoriaRepository).findAll();
    }

    @Test
    void deveLancarExcecaoQuandoCategoriaNaoExiste() {
        // Arrange
        Long idCategoria = 1L;
        when(categoriaRepository.existsById(idCategoria)).thenReturn(false);

        // Act & Assert
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () ->
                categoriaService.excluirCategoria(idCategoria)
        );

        assertEquals("Categoria não encontrada", exception.getMessage());
    }

    @Test
    void deveCriarCategoriaComSucesso() {
        CategoriaDTO categoriaDTO = new CategoriaDTO();
        categoriaDTO.setNome("Alimentação");

        CategoriaEntity categoriaEntity = new CategoriaEntity();
        categoriaEntity.setId(1L);
        categoriaEntity.setNome("Alimentação");

        lenient().when(categoriaRepository.save(any(CategoriaEntity.class))).thenReturn(categoriaEntity);

        CategoriaDTO categoriaCriada = categoriaService.criarCategoria(categoriaDTO);

        assertNotNull(categoriaCriada);
        assertEquals("Alimentação", categoriaCriada.getNome());
        assertEquals(1L, categoriaCriada.getIdCategoria());
        verify(categoriaRepository, times(1)).save(any(CategoriaEntity.class));
    }

    @Test
    void deveEditarCategoriaQuandoExiste() {
        Long idCategoria = 1L;
        CategoriaDTO categoriaDTO = new CategoriaDTO();
        categoriaDTO.setNome("Transporte");

        CategoriaEntity categoriaExistente = new CategoriaEntity();
        categoriaExistente.setId(idCategoria);
        categoriaExistente.setNome("Alimentação");

        lenient().when(categoriaRepository.findById(idCategoria)).thenReturn(Optional.of(categoriaExistente));
        lenient().when(categoriaRepository.save(any(CategoriaEntity.class))).thenReturn(categoriaExistente);

        Optional<CategoriaDTO> categoriaEditadaOptional = categoriaService.editarCategoria(idCategoria, categoriaDTO);

        assertTrue(categoriaEditadaOptional.isPresent());

        CategoriaDTO categoriaEditada = categoriaEditadaOptional.get();

        assertNotNull(categoriaEditada);
        assertEquals("Transporte", categoriaEditada.getNome());
        verify(categoriaRepository, times(1)).findById(idCategoria);
        verify(categoriaRepository, times(1)).save(any(CategoriaEntity.class));
    }

    @Test
    @DisplayName("Deve buscar categoria por ID")
    void deveBuscarCategoriaPorId() {
        // Arrange
        Long id = 1L;
        CategoriaEntity categoria = new CategoriaEntity();
        categoria.setId(id);
        categoria.setNome("Categoria Teste");

        when(categoriaRepository.findById(id)).thenReturn(Optional.of(categoria));

        // Act
        Optional<CategoriaDTO> resultado = categoriaService.buscarCategoriaPorId(id);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(id, resultado.get().getIdCategoria());
        assertEquals("Categoria Teste", resultado.get().getNome());
        verify(categoriaRepository).findById(id);
    }

}
