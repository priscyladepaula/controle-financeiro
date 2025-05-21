package br.com.priscyladepaula.desafioitau.controller;

import br.com.priscyladepaula.desafioitau.annotations.BrazilianDateFormat;
import br.com.priscyladepaula.desafioitau.dto.BalancoDTO;
import br.com.priscyladepaula.desafioitau.service.LancamentoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/v1/balanco")
public class BalancoController {

    private final LancamentoService lancamentoService;

    public BalancoController(LancamentoService lancamentoService) {
        this.lancamentoService = lancamentoService;
    }

    @GetMapping
    public ResponseEntity<BalancoDTO> calcularBalanco(@Valid @RequestParam("data_inicio") @BrazilianDateFormat LocalDate dataInicial,
                                                      @RequestParam("data_fim") @BrazilianDateFormat LocalDate dataFinal,
                                                      @RequestParam(value = "id_categoria", required = false) Long idCategoria) {

        BalancoDTO balancoDTO = lancamentoService.calcularBalanco(dataInicial, dataFinal, idCategoria);

        return ResponseEntity.ok(balancoDTO);
    }
}
