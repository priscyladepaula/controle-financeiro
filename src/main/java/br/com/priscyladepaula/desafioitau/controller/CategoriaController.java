package br.com.priscyladepaula.desafioitau.controller;

import br.com.priscyladepaula.desafioitau.domain.CategoriaEntity;
import br.com.priscyladepaula.desafioitau.dto.CategoriaDTO;
import br.com.priscyladepaula.desafioitau.service.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/v1")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping("/categorias")
    public ResponseEntity<List<CategoriaDTO>> listarCategorias() {
        return ResponseEntity.ok(categoriaService.listarCategorias());
    }

    @GetMapping("/categorias/{id}")
    public ResponseEntity<CategoriaDTO> buscarCategoriaPorId(@PathVariable Long id) {
        return categoriaService.buscarCategoriaPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/categorias")
    public ResponseEntity<CategoriaDTO> criarCategoria(@Valid @RequestBody CategoriaDTO categoriaDTO) {

        CategoriaDTO categoriaCriada = categoriaService.criarCategoria(categoriaDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaCriada);
    }

    @PutMapping("/categorias/{id}")
    public ResponseEntity<CategoriaDTO> editarCategoria(@PathVariable Long id,
                                                           @Valid @RequestBody CategoriaDTO categoriaDTO) {
        return categoriaService.editarCategoria(id, categoriaDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/categorias/buscar")
    public ResponseEntity<List<CategoriaDTO>> buscarCategoriasPorNome(@RequestParam(required = false) String nome) {
        List<CategoriaDTO> categorias = categoriaService.buscarCategoriaPorNome(nome);
        return ResponseEntity.ok(categorias);
    }

    @DeleteMapping("/categorias/{id}")
    public ResponseEntity<String> excluirCategoria(@PathVariable Long id) {

        Optional<CategoriaDTO> categoriaOptional = categoriaService.buscarCategoriaPorId(id);

        if (categoriaOptional.isPresent()) {
            categoriaService.excluirCategoria(id);
            return ResponseEntity.noContent().build();
        } else {
            throw new NoSuchElementException("Categoria não encontrada com o ID: " + id);
        }
    }

}
