package br.com.priscyladepaula.desafioitau.dto;

import br.com.priscyladepaula.desafioitau.domain.SubcategoriaEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubcategoriaDTO {

    @JsonProperty("id_subcategoria")
    private Long id;

    @NotBlank(message = "O campo 'nome' é obrigatório")
    private String nome;

    @JsonProperty("id_categoria")
    @NotNull(message = "O campo 'id_categoria' é obrigatório")
    private Long idCategoria;

    public SubcategoriaDTO(SubcategoriaEntity subcategoria) {
        this.id = subcategoria.getId();
        this.nome = subcategoria.getNome();
        this.idCategoria = subcategoria.getId();
    }
}
