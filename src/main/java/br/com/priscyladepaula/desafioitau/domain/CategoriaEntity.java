package br.com.priscyladepaula.desafioitau.domain;

import br.com.priscyladepaula.desafioitau.dto.CategoriaDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@Table(name = "tb_categoria", uniqueConstraints = {
        @UniqueConstraint(
                columnNames = "nome", name = "uk_categoria_nome"
        )
})
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id_categoria")
    private Long id;

    @Column(nullable = false, unique = true)
    private String nome;

    @OneToMany(mappedBy = "categoria", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<SubcategoriaEntity> subcategorias;

    public CategoriaEntity(CategoriaDTO categoriaDTO) {
        this.nome = categoriaDTO.getNome();
    }

}
