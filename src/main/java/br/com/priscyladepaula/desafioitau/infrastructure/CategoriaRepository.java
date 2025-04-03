package br.com.priscyladepaula.desafioitau.infrastructure;

import br.com.priscyladepaula.desafioitau.domain.CategoriaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<CategoriaEntity, Long> {

    List<CategoriaEntity> findByNomeIgnoreCase(String nome);

    boolean existsByNome(String nome);

    boolean existsById(Long id);
}
