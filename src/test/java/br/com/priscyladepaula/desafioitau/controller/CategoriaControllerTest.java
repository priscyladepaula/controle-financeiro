package br.com.priscyladepaula.desafioitau.controller;

import br.com.priscyladepaula.desafioitau.domain.CategoriaEntity;
import br.com.priscyladepaula.desafioitau.dto.CategoriaDTO;
import br.com.priscyladepaula.desafioitau.infrastructure.CategoriaRepository;
import br.com.priscyladepaula.desafioitau.service.CategoriaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoriaControllerTest {

    @Mock
    private CategoriaService categoriaService;

    @InjectMocks
    private CategoriaController categoriaController;

    private CategoriaDTO categoria1;
    private CategoriaDTO categoria2;

    @BeforeEach
    void setUp() {
        categoria1 = new CategoriaDTO();
        categoria1.setIdCategoria(1L);
        categoria1.setNome("Alimentação");

        categoria2 = new CategoriaDTO();
        categoria2.setIdCategoria(2L);
        categoria2.setNome("Transporte");
    }

    @Test
    void deveListarTodasCategorias() {
        // Arrange
        List<CategoriaDTO> categorias = Arrays.asList(categoria1, categoria2);
        when(categoriaService.listarCategorias()).thenReturn(categorias);

        // Act
        ResponseEntity<List<CategoriaDTO>> response = categoriaController.listarCategorias();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        assertEquals(categoria1.getIdCategoria(), response.getBody().get(0).getIdCategoria());
        assertEquals(categoria1.getNome(), response.getBody().get(0).getNome());
        assertEquals(categoria2.getIdCategoria(), response.getBody().get(1).getIdCategoria());
        assertEquals(categoria2.getNome(), response.getBody().get(1).getNome());
        verify(categoriaService).listarCategorias();
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHaCategorias() {
        // Arrange
        when(categoriaService.listarCategorias()).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<List<CategoriaDTO>> response = categoriaController.listarCategorias();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
        verify(categoriaService).listarCategorias();
    }

    @Test
    void deveBuscarCategoriaPorId() {
        // Arrange
        Long id = 1L;
        when(categoriaService.buscarCategoriaPorId(id)).thenReturn(Optional.of(categoria1));

        ResponseEntity<CategoriaDTO> response = categoriaController.buscarCategoriaPorId(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(categoria1.getIdCategoria(), response.getBody().getIdCategoria());
        assertEquals(categoria1.getNome(), response.getBody().getNome());
        verify(categoriaService).buscarCategoriaPorId(id);
    }

    @Test
    void deveRetornar404QuandoCategoriaNaoExiste() {

        Long id = 999L;
        when(categoriaService.buscarCategoriaPorId(id)).thenReturn(Optional.empty());

        ResponseEntity<CategoriaDTO> response = categoriaController.buscarCategoriaPorId(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(categoriaService).buscarCategoriaPorId(id);
    }

    @Test
    void deveCriarCategoria() {

        CategoriaDTO novaCategoria = new CategoriaDTO();
        novaCategoria.setNome("Nova Categoria");

        when(categoriaService.criarCategoria(any(CategoriaDTO.class))).thenReturn(categoria1);

        ResponseEntity<CategoriaDTO> response = categoriaController.criarCategoria(novaCategoria);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(categoria1.getIdCategoria(), response.getBody().getIdCategoria());
        assertEquals(categoria1.getNome(), response.getBody().getNome());
        verify(categoriaService).criarCategoria(any(CategoriaDTO.class));
    }

    @Test
    void deveEditarCategoria() {

        Long id = 1L;
        CategoriaDTO categoriaAtualizada = new CategoriaDTO();
        categoriaAtualizada.setNome("Categoria Atualizada");

        when(categoriaService.editarCategoria(eq(id), any(CategoriaDTO.class)))
                .thenReturn(Optional.of(categoriaAtualizada));

        ResponseEntity<CategoriaDTO> response = categoriaController.editarCategoria(id, categoriaAtualizada);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(categoriaAtualizada.getNome(), response.getBody().getNome());
        verify(categoriaService).editarCategoria(eq(id), any(CategoriaDTO.class));
    }

    @Test
    void deveRetornar404AoEditarCategoriaInexistente() {

        Long id = 999L;
        CategoriaDTO categoriaAtualizada = new CategoriaDTO();
        categoriaAtualizada.setNome("Categoria Atualizada");

        when(categoriaService.editarCategoria(eq(id), any(CategoriaDTO.class)))
                .thenReturn(Optional.empty());

        ResponseEntity<CategoriaDTO> response = categoriaController.editarCategoria(id, categoriaAtualizada);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(categoriaService).editarCategoria(eq(id), any(CategoriaDTO.class));
    }

    @Test
    void deveBuscarCategoriasPorNome() {

        String nome = "Alim";
        List<CategoriaDTO> categorias = Arrays.asList(categoria1);

        when(categoriaService.buscarCategoriaPorNome(nome)).thenReturn(categorias);

        ResponseEntity<List<CategoriaDTO>> response = categoriaController.buscarCategoriasPorNome(nome);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("Alimentação", response.getBody().get(0).getNome());
        verify(categoriaService).buscarCategoriaPorNome(nome);
    }

    @Test
    void deveBuscarTodasCategoriasQuandoNomeNaoFornecido() {

        String nome = null;
        List<CategoriaDTO> categorias = Arrays.asList(categoria1, categoria2);

        when(categoriaService.buscarCategoriaPorNome(nome)).thenReturn(categorias);

        ResponseEntity<List<CategoriaDTO>> response = categoriaController.buscarCategoriasPorNome(nome);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(categoriaService).buscarCategoriaPorNome(nome);
    }

    @Test
    void deveExcluirCategoria() {

        Long id = 1L;
        when(categoriaService.buscarCategoriaPorId(id)).thenReturn(Optional.of(categoria1));
        doNothing().when(categoriaService).excluirCategoria(id);

        ResponseEntity<String> response = categoriaController.excluirCategoria(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(categoriaService).buscarCategoriaPorId(id);
        verify(categoriaService).excluirCategoria(id);
    }

    @Test
    void deveLancarExcecaoAoExcluirCategoriaInexistente() {
        Long id = 999L;
        when(categoriaService.buscarCategoriaPorId(id)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            categoriaController.excluirCategoria(id);
        });

        assertTrue(exception.getMessage().contains("Categoria não encontrada"),
                "A mensagem de erro deve conter 'Categoria não encontrada'");

        verify(categoriaService).buscarCategoriaPorId(id);
        verify(categoriaService, never()).excluirCategoria(anyLong());
    }

}
