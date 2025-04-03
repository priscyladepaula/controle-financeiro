package br.com.priscyladepaula.desafioitau.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BalancoDTO {

    private CategoriaDTO categoria;

    private BigDecimal receita;

    private BigDecimal despesa;

    private BigDecimal saldo;

    public BalancoDTO(CategoriaDTO categoria, BigDecimal receita, BigDecimal despesa) {
        this.categoria = categoria;
        this.receita = receita;
        this.despesa = despesa;
        this.saldo = BigDecimal.ZERO;
    }

}
