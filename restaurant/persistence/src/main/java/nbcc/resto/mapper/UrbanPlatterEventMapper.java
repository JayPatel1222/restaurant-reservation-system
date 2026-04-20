package nbcc.resto.mapper;

import nbcc.resto.dto.UrbanPlatterEvent;
import nbcc.resto.entity.UrbanPlatterEventEntity;
import org.springframework.stereotype.Component;

@Component
public class UrbanPlatterEventMapper implements EntityMapper<UrbanPlatterEvent, UrbanPlatterEventEntity> {
    private final MenuMapper menuMapper;

    public UrbanPlatterEventMapper(MenuMapper menuMapper) {
        this.menuMapper = menuMapper;
    }


    @Override
    public UrbanPlatterEvent toDTO(UrbanPlatterEventEntity entity) {
        if(entity == null){
            return null;
        }

        return new UrbanPlatterEvent()
                .setId(entity.getId())
                .setName(entity.getName())
                .setDescription(entity.getDescription())
                .setStartDate(entity.getStartDate())
                .setEndDate(entity.getEndDate())
                .setMinutes(entity.getMinutes())
                .setPrice(entity.getPrice())
                .setActive(entity.isActive())
                .setArchived(entity.isArchived())
                .setVersion(entity.getVersion())
                .setCreatedAt(entity.getCreatedAt())
                .setUpdatedAt(entity.getUpdatedAt())
                .setMenu(menuMapper.toDTO(entity.getMenu()));
    }

    @Override
    public UrbanPlatterEventEntity toEntity(UrbanPlatterEvent dto) {

        if(dto == null){
            return null;
        }

        return new UrbanPlatterEventEntity()
                .setId(dto.getId())
                .setName(dto.getName())
                .setDescription(dto.getDescription())
                .setStartDate(dto.getStartDate())
                .setEndDate(dto.getEndDate())
                .setMinutes(dto.getMinutes())
                .setArchived(dto.isArchived())
                .setVersion(dto.getVersion())
                .setPrice(dto.getPrice())
                .setActive(dto.isActive())
                .setMenu(menuMapper.toEntity(dto.getMenu()));
    }
}
