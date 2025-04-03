package br.com.priscyladepaula.desafioitau.controller;

import br.com.priscyladepaula.desafioitau.dto.LancamentoDTO;
import br.com.priscyladepaula.desafioitau.service.LancamentoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1")
public class LancamentoController {

    private final LancamentoService lancamentoService;

    public LancamentoController(LancamentoService lancamentoService) {
        this.lancamentoService = lancamentoService;
    }

    @GetMapping("/lancamentos")
    public ResponseEntity<List<LancamentoDTO>> listarLancamentos() {
        List<LancamentoDTO> lancamentos = lancamentoService.listarLancamentos();

        return ResponseEntity.ok(lancamentos);
    }

    @GetMapping("/lancamentos/buscar")
    public ResponseEntity<List<LancamentoDTO>> buscarLancamentoPorSubcategoria(@RequestParam Long idSubcategoria) {

        return ResponseEntity.ok(lancamentoService.buscarPorSubcategoria(idSubcategoria));
    }

    @PostMapping("/lancamentos")
    public ResponseEntity<LancamentoDTO> criarLancamento(@RequestBody LancamentoDTO lancamentoDTO) {
        LancamentoDTO novoLancamentoDTO = lancamentoService.criarLancamento(lancamentoDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(novoLancamentoDTO);
    }

    @PutMapping("/lancamentos/{id}")
    public ResponseEntity<LancamentoDTO> editarLancamento(@PathVariable Long id, @RequestBody LancamentoDTO lancamentoDTO) {
        LancamentoDTO lancamentoAtualizado = lancamentoService.editarLancamento(id, lancamentoDTO);

        return ResponseEntity.status(HttpStatus.OK).body(lancamentoAtualizado);
    }

    @DeleteMapping("/lancamentos/{id}")
    public ResponseEntity<Void> excluirLancamento(@PathVariable Long id) {
        lancamentoService.excluirLancamento(id);

        return ResponseEntity.noContent().build();
    }

}
