package br.com.priscyladepaula.desafioitau.controller;

import br.com.priscyladepaula.desafioitau.dto.SubcategoriaDTO;
import br.com.priscyladepaula.desafioitau.service.SubcategoriaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/subcategorias")
public class SubcategoriaController {

    private final SubcategoriaService subcategoriaService;

    public SubcategoriaController(SubcategoriaService subcategoriaService) {
        this.subcategoriaService = subcategoriaService;
    }

    @PostMapping
    public ResponseEntity<SubcategoriaDTO> criarSubcategoria(@Valid @RequestBody SubcategoriaDTO subcategoriaDTO) {

        SubcategoriaDTO subcategoriaCriada = subcategoriaService.criarSubcategoria(subcategoriaDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(subcategoriaCriada);
    }

    @GetMapping
    public ResponseEntity<List<SubcategoriaDTO>> listarSubcategorias() {

        return ResponseEntity.ok(subcategoriaService.listarSubcategorias());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubcategoriaDTO> buscarSubcategoriaPorId(@PathVariable Long id) {

        SubcategoriaDTO subcategoriaDTO = subcategoriaService.buscarSubcategoriaPorId(id);

        return ResponseEntity.ok(subcategoriaDTO);
    }

    @GetMapping("/buscar")
    public ResponseEntity<SubcategoriaDTO> buscarSubcategoriasPorNome(@RequestParam String nome) {

        return ResponseEntity.ok(subcategoriaService.buscarPorNome(nome));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubcategoriaDTO> editarSubcategoria(@PathVariable Long id,
                                                              @RequestBody SubcategoriaDTO subcategoriaDTO) {

        SubcategoriaDTO atualizado = subcategoriaService.editarSubcategoria(id, subcategoriaDTO);

        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarSubcategoria(@PathVariable Long id) {

        subcategoriaService.excluirSubcategoria(id);

        return ResponseEntity.noContent().build();
    }

}
