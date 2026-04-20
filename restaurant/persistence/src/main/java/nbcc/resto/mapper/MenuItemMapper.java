package nbcc.resto.mapper;

import nbcc.resto.dto.MenuItem;
import nbcc.resto.entity.MenuItemEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MenuItemMapper implements EntityMapper<MenuItem, MenuItemEntity>{

    @Override
    public MenuItem toDTO(MenuItemEntity entity) {
        return new MenuItem()
                .setId(entity.getId())
                .setName(entity.getName())
                .setDescription(entity.getDescription())
                .setVersion(entity.getVersion())
                .setMenuId(entity.getMenuId());
    }

    @Override
    public MenuItemEntity toEntity(MenuItem dto) {
        return new MenuItemEntity()
                .setId(dto.getId())
                .setName(dto.getName())
                .setDescription(dto.getDescription())
                .setVersion(dto.getVersion())
                .setMenuId(dto.getMenuId());
    }

    public List<MenuItemEntity> toEntity(List<MenuItem> itemsDto){
        return itemsDto.stream().map(this::toEntity).toList();
    }
}
