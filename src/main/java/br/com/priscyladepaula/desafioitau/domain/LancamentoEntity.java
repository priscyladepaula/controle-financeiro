package br.com.priscyladepaula.desafioitau.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "tb_lancamento")
public class LancamentoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_lancamento")
    private Long id;

    @Column(nullable = false)
    private BigDecimal valor;

    @Column(nullable = false)
    private LocalDate data = LocalDate.now();

    @ManyToOne
    @JoinColumn(name = "id_subcategoria", nullable = false)
    private SubcategoriaEntity subcategoria;

    private String comentario;

}
