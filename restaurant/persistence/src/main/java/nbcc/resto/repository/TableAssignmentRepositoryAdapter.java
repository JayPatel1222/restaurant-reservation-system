package nbcc.resto.repository;

import nbcc.resto.dto.DiningTable;
import nbcc.resto.dto.TableAssignment;
import nbcc.resto.mapper.TableAssignmentMapper;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public class TableAssignmentRepositoryAdapter implements TableAssignmentRepository {

    private final TableAssignmentMapper assignmentMapper;
    private final TableAssignmentJPARepository assignmentJPARepository;

    public TableAssignmentRepositoryAdapter(TableAssignmentMapper assignmentMapper, TableAssignmentJPARepository assignmentJPARepository) {
        this.assignmentMapper = assignmentMapper;
        this.assignmentJPARepository = assignmentJPARepository;
    }

    @Override
    public TableAssignment assignTable(TableAssignment tableAssignment) {
        var entity = assignmentMapper.toEntity(tableAssignment);
        entity = assignmentJPARepository.save(entity);
        return assignmentMapper.toDTO(entity);
    }

    @Override
    public Collection<TableAssignment> getByTableId(Long tableId) {
        var entities =  assignmentJPARepository.findAllByDiningTable_Id(tableId);
        return assignmentMapper.toDTO(entities);
    }

    @Override
    public Collection<TableAssignment> getBySeatingId(Long seatingId) {
        var entities =  assignmentJPARepository.findAllBySeating_Id(seatingId);
        return assignmentMapper.toDTO(entities);
    }

}