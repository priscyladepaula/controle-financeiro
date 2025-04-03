package br.com.priscyladepaula.desafioitau.controller;

import br.com.priscyladepaula.desafioitau.dto.SubcategoriaDTO;
import br.com.priscyladepaula.desafioitau.service.SubcategoriaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1")
public class SubcategoriaController {

    private final SubcategoriaService subcategoriaService;

    public SubcategoriaController(SubcategoriaService subcategoriaService) {
        this.subcategoriaService = subcategoriaService;
    }

    @PostMapping("/subcategorias")
    public ResponseEntity<SubcategoriaDTO> criarSubcategoria(@Valid @RequestBody SubcategoriaDTO subcategoriaDTO) {
        SubcategoriaDTO subcategoriaCriada = subcategoriaService.criarSubcategoria(subcategoriaDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(subcategoriaCriada);
    }

    @GetMapping("/subcategorias")
    public ResponseEntity<List<SubcategoriaDTO>> listarSubcategorias() {
        List<SubcategoriaDTO> subcategorias = subcategoriaService.listarSubcategorias();

        return ResponseEntity.status(HttpStatus.OK).body(subcategorias);
    }

    @GetMapping("/subcategorias/buscar")
    public ResponseEntity<List<SubcategoriaDTO>> buscarSubcategoriasPorNome(@RequestParam String nome) {
        return ResponseEntity.ok(subcategoriaService.buscarPorNome(nome));
    }

    @GetMapping("/subcategorias/{id}")
    public ResponseEntity<SubcategoriaDTO> buscarSubcategoria(@PathVariable Long id) {
        SubcategoriaDTO subcategoria = subcategoriaService.buscarSubcategoriaPorId(id);

        return ResponseEntity.status(HttpStatus.OK).body(subcategoria);
    }

    @PutMapping("/subcategorias/{id}")
    public ResponseEntity<SubcategoriaDTO> editarSubcategoria(@PathVariable Long id,
                                                              @RequestBody SubcategoriaDTO subcategoriaDTO) {
        SubcategoriaDTO subcategoriaAtualizada = subcategoriaService.editarSubcategoria(id, subcategoriaDTO);
        return ResponseEntity.ok(subcategoriaAtualizada);
    }

    @DeleteMapping("/subcategorias/{id}")
    public ResponseEntity<Void> deletarSubcategoria(@PathVariable Long id) {
        subcategoriaService.deletarSubcategoria(id);
        return ResponseEntity.noContent().build();
    }

}
