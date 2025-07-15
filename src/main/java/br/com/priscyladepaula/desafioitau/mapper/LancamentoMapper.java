package br.com.priscyladepaula.desafioitau.mapper;

import br.com.priscyladepaula.desafioitau.domain.LancamentoEntity;
import br.com.priscyladepaula.desafioitau.dto.LancamentoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LancamentoMapper {

    @Mapping(source = "subcategoria.id", target = "idSubcategoria")
    LancamentoDTO toDto(LancamentoEntity lancamentoEntity);

    @Mapping(target = "subcategoria", ignore = true)
    LancamentoEntity toEntity(LancamentoDTO dto);

    List<LancamentoEntity> toEntityList(List<LancamentoDTO> dtos);
    List<LancamentoDTO> toDtoList(List<LancamentoEntity> entities);
}
