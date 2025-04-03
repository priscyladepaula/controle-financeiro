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

    @NotBlank(message = "O campo 'nome' é obrigatório")
    private String nome;

    @JsonProperty("id_categoria")
    private Long idCategoria;

    public CategoriaDTO(CategoriaEntity categoria) {
        this.nome = categoria.getNome();
        this.idCategoria = categoria.getId();
    }

}
