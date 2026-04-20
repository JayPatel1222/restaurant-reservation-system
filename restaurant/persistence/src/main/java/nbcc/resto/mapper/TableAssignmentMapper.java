package nbcc.resto.mapper;

import nbcc.resto.dto.TableAssignment;
import nbcc.resto.entity.DiningTableEntity;
import nbcc.resto.entity.TableAssignmentEntity;
import org.springframework.stereotype.Component;

@Component
public class TableAssignmentMapper implements EntityMapper<TableAssignment, TableAssignmentEntity> {

    private final SeatingMapper seatingMapper;
    private final DiningTableMapper tableMapper;

    public TableAssignmentMapper(SeatingMapper seatingMapper, DiningTableMapper tableMapper) {
        this.seatingMapper = seatingMapper;
        this.tableMapper = tableMapper;
    }


    @Override
    public TableAssignment toDTO(TableAssignmentEntity entity) {

        if(entity == null) {
            return null;
        }

        return new TableAssignment()
                .setId(entity.getId())
                .setSeating(seatingMapper.toDTO(entity.getSeating()))
                .setDiningTable(tableMapper.toDTO(entity.getDiningTable()))
                .setAssociatedDate(entity.getAssociatedDate());
    }

    @Override
    public TableAssignmentEntity toEntity(TableAssignment dto) {
        if (dto == null) {
            return null;
        }

        return new TableAssignmentEntity()
                .setId(dto.getId())
                .setSeating(seatingMapper.toEntity(dto.getSeating()))
                .setDiningTable(tableMapper.toEntity(dto.getDiningTable()))
                .setAssociatedDate(dto.getAssociatedDate());
    }
}
