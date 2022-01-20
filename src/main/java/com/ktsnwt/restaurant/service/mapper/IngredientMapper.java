package com.ktsnwt.restaurant.service.mapper;

import com.ktsnwt.restaurant.domain.Ingredient;
import com.ktsnwt.restaurant.service.dto.IngredientDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Ingredient} and its DTO {@link IngredientDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface IngredientMapper extends EntityMapper<IngredientDTO, Ingredient> {
    @Named("nameSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    Set<IngredientDTO> toDtoNameSet(Set<Ingredient> ingredient);
}
