package br.com.priscyladepaula.desafioitau.infrastructure;

import br.com.priscyladepaula.desafioitau.domain.SubcategoriaEntity;
import ch.qos.logback.classic.util.LogbackMDCAdapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubcategoriaRepository extends JpaRepository<SubcategoriaEntity, Long> {

    boolean existsByNomeAndCategoriaId(String nome, Long idCategoria);

    boolean existsByIdAndLancamentosIsNotEmpty(Long idLancamento);

    Optional<SubcategoriaEntity> findByNomeIgnoreCase(String nome);
}
