package br.com.priscyladepaula.desafioitau.dto;

import br.com.priscyladepaula.desafioitau.domain.CategoriaEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaDTO {

    private String nome;

    @JsonProperty("id_categoria")
    private Long id;

    public CategoriaDTO(CategoriaEntity categoria) {
        this.nome = categoria.getNome();
        this.id = categoria.getId();
    }

}
