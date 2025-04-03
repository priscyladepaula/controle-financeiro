package br.com.priscyladepaula.desafioitau.infrastructure;

import br.com.priscyladepaula.desafioitau.domain.LancamentoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface LancamentoRepository extends JpaRepository<LancamentoEntity, Long> {

    List<LancamentoEntity> findBySubcategoriaId(Long idSubcategoria);

    List<LancamentoEntity> findByDataBetween(LocalDate dataInicial, LocalDate dataFinal);

    List<LancamentoEntity> findBySubcategoriaIdAndDataBetween(Long idSubcategoria, LocalDate dataInicio, LocalDate dataFim);

    boolean existsBySubcategoriaId(Long idSubcategoria);

}
