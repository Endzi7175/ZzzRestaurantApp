package com.ktsnwt.restaurant.service.mapper;

import com.ktsnwt.restaurant.domain.MenuItem;
import com.ktsnwt.restaurant.service.dto.MenuItemDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MenuItem} and its DTO {@link MenuItemDTO}.
 */
@Mapper(componentModel = "spring", uses = { PictureMapper.class, IngredientMapper.class, MenuMapper.class })
public interface MenuItemMapper extends EntityMapper<MenuItemDTO, MenuItem> {
    @Mapping(target = "picture", source = "picture", qualifiedByName = "id")
    @Mapping(target = "ingredients", source = "ingredients", qualifiedByName = "nameSet")
    @Mapping(target = "menu", source = "menu", qualifiedByName = "id")
    MenuItemDTO toDto(MenuItem s);

    @Mapping(target = "removeIngredient", ignore = true)
    MenuItem toEntity(MenuItemDTO menuItemDTO);
}
