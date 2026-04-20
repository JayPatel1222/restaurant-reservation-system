package nbcc.resto.repository;

import nbcc.resto.entity.DiningTableEntity;
import nbcc.resto.entity.TableAssignmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TableAssignmentJPARepository extends JpaRepository<TableAssignmentEntity, Long> {
    List<TableAssignmentEntity> findAllByDiningTable_Id(Long diningTableId);
    List<TableAssignmentEntity> findAllBySeating_Id(Long seatingId);
    boolean existsByDiningTable_Id(Long diningTableId);

}
