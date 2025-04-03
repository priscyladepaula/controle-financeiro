package br.com.priscyladepaula.desafioitau.controller;

import br.com.priscyladepaula.desafioitau.dto.BalancoDTO;
import br.com.priscyladepaula.desafioitau.service.BalancoService;
import br.com.priscyladepaula.desafioitau.service.LancamentoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/v1")
public class BalancoController {

    private final LancamentoService lancamentoService;

    public BalancoController(LancamentoService lancamentoService) {
        this.lancamentoService = lancamentoService;
    }

    @GetMapping("/balanco")
    public ResponseEntity<BalancoDTO> calcularBalanco(@RequestParam("data_inicio") LocalDate dataInicial,
                                                      @RequestParam("data_fim") LocalDate dataFinal,
                                                      @RequestParam(value = "id_categoria", required = false) Long idCategoria) {

        BalancoDTO balancoDTO = lancamentoService.calcularBalanco(dataInicial, dataFinal, idCategoria);

        return ResponseEntity.ok(balancoDTO);
    }
}
