package br.com.priscyladepaula.desafioitau.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class LancamentoDTO {

    @JsonProperty("id_lancamento")
    private Long id;

    private BigDecimal valor;

    @JsonProperty("id_subcategoria")
    @NotNull(message = "O campo 'id_subcategoria' é obrigatório")
    private Long idSubcategoria;

    private String comentario;

    @Schema(type = "string", pattern = "dd/MM/yyyy")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate data;

    public LancamentoDTO () {}

}
