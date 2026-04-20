package nbcc.resto.mapper;

import nbcc.resto.dto.Menu;
import nbcc.resto.entity.MenuEntity;
import org.springframework.stereotype.Component;

@Component
public class MenuMapper implements EntityMapper<Menu,MenuEntity>{

    private final MenuItemMapper menuItemMapper;

    public MenuMapper(MenuItemMapper menuItemMapper) {
        this.menuItemMapper = menuItemMapper;
    }

    @Override
    public Menu toDTO(MenuEntity entity) {
        if(entity == null){
            return null;
        }
        if(entity.getItems() != null){
            var items = menuItemMapper.toDTO(entity.getItems());
            return new Menu()
                    .setId(entity.getId())
                    .setName(entity.getName())
                    .setDescription(entity.getDescription())
                    .setCreatedAt(entity.getCreatedAt())
                    .setVersion(entity.getVersion())
                    .setItems(items);
        }
        return new Menu()
                .setId(entity.getId())
                .setName(entity.getName())
                .setDescription(entity.getDescription())
                .setCreatedAt(entity.getCreatedAt())
                .setVersion(entity.getVersion());
    }

    @Override
    public MenuEntity toEntity(Menu menuDto) {
        if(menuDto == null){
            return null;
        }
        if(menuDto.getItems() != null){
            var items = menuItemMapper.toEntity(menuDto.getItems());
            return new MenuEntity()
                    .setId(menuDto.getId())
                    .setName(menuDto.getName())
                    .setDescription(menuDto.getDescription())
                    .setCreatedAt(menuDto.getCreatedAt())
                    .setVersion(menuDto.getVersion())
                    .setItems(items);
        }
        return new MenuEntity()
                .setId(menuDto.getId())
                .setName(menuDto.getName())
                .setDescription(menuDto.getDescription())
                .setCreatedAt(menuDto.getCreatedAt())
                .setVersion(menuDto.getVersion());
    }
}
