package br.com.priscyladepaula.desafioitau.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "tb_subcategoria", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"nome", "id_categoria"})
})
public class SubcategoriaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id_subcategoria")
    private Long id;

    @Column(nullable = false, unique = true)
    private String nome;

    @ManyToOne
    @JoinColumn(name = "id_categoria", nullable = false)
    private CategoriaEntity categoria;

    @OneToMany(mappedBy = "subcategoria", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LancamentoEntity> lancamentos;

}
