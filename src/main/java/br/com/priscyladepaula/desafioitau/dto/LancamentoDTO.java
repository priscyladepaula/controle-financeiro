package br.com.priscyladepaula.desafioitau.dto;

import br.com.priscyladepaula.desafioitau.domain.LancamentoEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class LancamentoDTO {

    @JsonProperty("id_lancamento")
    private Long id;

    private BigDecimal valor;

    @JsonProperty("id_subcategoria")
    @Digits(integer = 10, fraction = 0, message = "O campo 'id_subcategoria' deve conter apenas números inteiros")
    private Long idSubcategoria;

    private String comentario;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate data;

    public LancamentoDTO () {}

    public LancamentoDTO(LancamentoEntity lancamento) {
        this.id = lancamento.getId();
        this.valor = lancamento.getValor();
        this.data = lancamento.getData();
        this.idSubcategoria = lancamento.getSubcategoria().getId();
        this.comentario = lancamento.getComentario();
    }
}
