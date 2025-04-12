package br.com.priscyladepaula.desafioitau.mapper;

import br.com.priscyladepaula.desafioitau.domain.SubcategoriaEntity;
import br.com.priscyladepaula.desafioitau.dto.SubcategoriaDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = {CategoriaMapper.class})
public interface SubcategoriaMapper {

    SubcategoriaMapper INSTANCE = Mappers.getMapper(SubcategoriaMapper.class);

    @Mapping(source = "categoria.id", target = "idCategoria")
    SubcategoriaDTO toDto(SubcategoriaEntity subcategoria);

    @Mapping(source = "idCategoria", target = "categoria.id")
    SubcategoriaEntity toEntity(SubcategoriaDTO subcategoriaDTO);

    List<SubcategoriaDTO> toDtoList(List<SubcategoriaEntity> subcategorias);
    List<SubcategoriaEntity> toEntityList(List<SubcategoriaDTO> dtos);
}
