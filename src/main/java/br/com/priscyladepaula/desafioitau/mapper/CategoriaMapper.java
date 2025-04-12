package br.com.priscyladepaula.desafioitau.mapper;

import br.com.priscyladepaula.desafioitau.domain.CategoriaEntity;
import br.com.priscyladepaula.desafioitau.dto.CategoriaDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoriaMapper {

    CategoriaMapper INSTANCE = Mappers.getMapper(CategoriaMapper.class);

    CategoriaDTO toDto(CategoriaEntity categoria);

    CategoriaEntity toEntity(CategoriaDTO categoriaDTO);

    List<CategoriaDTO> toDtoList(List<CategoriaEntity> categorias);
    List<CategoriaEntity> toEntityList(List<CategoriaDTO> dtos);
}
