package nbcc.resto.mapper;

import nbcc.resto.dto.Seating;
import nbcc.resto.entity.SeatingEntity;
import org.springframework.stereotype.Component;

@Component
public class SeatingMapper implements EntityMapper<Seating, SeatingEntity> {

    private final UrbanPlatterEventMapper eventMapper;

    public SeatingMapper(UrbanPlatterEventMapper eventMapper) {
        this.eventMapper = eventMapper;
    }

    @Override
    public Seating toDTO(SeatingEntity entity) {
        if(entity == null){
            return null;
        }

        return new Seating()
                .setId(entity.getId())
                .setName(entity.getName())
                .setStartDate(entity.getStartDate())
                .setArchived(entity.getArchived())
                .setDuration(entity.getDuration())
                .setVersion(entity.getVersion())
                .setPlatterEvent(eventMapper.toDTO(entity.getPlatterEvent()));
    }

    @Override
    public SeatingEntity toEntity(Seating dto) {
        if(dto == null){
            return null;
        }

        return new SeatingEntity()
                .setId(dto.getId())
                .setName(dto.getName())
                .setStartDate(dto.getStartDate())
                .setDuration(dto.getDuration())
                .setArchived(dto.getArchived())
                .setVersion(dto.getVersion())
                .setPlatterEvent(eventMapper.toEntity(dto.getPlatterEvent()));
    }
}
