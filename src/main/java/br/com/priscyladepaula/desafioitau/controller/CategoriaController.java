package br.com.priscyladepaula.desafioitau.controller;

import br.com.priscyladepaula.desafioitau.dto.CategoriaDTO;
import br.com.priscyladepaula.desafioitau.service.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping
    public ResponseEntity<List<CategoriaDTO>> listarCategorias() {

        return ResponseEntity.ok(categoriaService.listarCategorias());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaDTO> buscarCategoriaPorId(@PathVariable Long id) {

        CategoriaDTO categoriaDTO = categoriaService.buscarCategoriaPorId(id);

        return ResponseEntity.ok(categoriaDTO);
    }

    @PostMapping
    public ResponseEntity<CategoriaDTO> criarCategoria(@Valid @RequestBody CategoriaDTO categoriaDTO) {

        CategoriaDTO categoriaCriada = categoriaService.criarCategoria(categoriaDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaCriada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaDTO> editarCategoria(@PathVariable Long id,
                                                           @Valid @RequestBody CategoriaDTO categoriaDTO) {

        CategoriaDTO atualizado = categoriaService.editarCategoria(id, categoriaDTO);

        return ResponseEntity.ok(atualizado);
    }

    @GetMapping("/busca")
    public ResponseEntity<CategoriaDTO> buscarCategoriasPorNome(@RequestParam(required = false) String nome) {

        CategoriaDTO categorias = categoriaService.buscarCategoriaPorNome(nome);

        return ResponseEntity.ok(categorias);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirCategoria(@PathVariable Long id) {

        categoriaService.excluirCategoria(id);

        return ResponseEntity.noContent().build();
    }

}
