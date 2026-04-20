package nbcc.resto.repository;

import nbcc.common.exception.ConcurrencyException;
import nbcc.resto.dto.DiningTable;
import nbcc.resto.mapper.DiningTableMapper;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class DiningTableRepositoryAdapter implements DiningTableRepository {

    private final DiningTableMapper diningTableMapper;
    private final DiningTableJPARepository diningTableJPARepository;
    private final ReservationJPARepository reservationJPARepository;
    private final TableAssignmentJPARepository tableAssignmentJPARepository;

    public DiningTableRepositoryAdapter(DiningTableMapper diningTableMapper, DiningTableJPARepository diningTableJPARepository, ReservationJPARepository reservationJPARepository, TableAssignmentJPARepository tableAssignmentJPARepository) {
        this.diningTableMapper = diningTableMapper;
        this.diningTableJPARepository = diningTableJPARepository;
        this.reservationJPARepository = reservationJPARepository;
        this.tableAssignmentJPARepository = tableAssignmentJPARepository;
    }


    @Override
    public List<DiningTable> getAll() {
        var entities = diningTableJPARepository.findAllByArchivedIsFalse();
        return diningTableMapper.toDTO(entities);
    }

    @Override
    public Optional<DiningTable> get(Long id) {
        var entity = diningTableJPARepository.findById(id);
        return diningTableMapper.toDTO(entity);

    }

    @Override
    public DiningTable create(DiningTable diningTable) {
        var entity = diningTableMapper.toEntity(diningTable);
        entity = diningTableJPARepository.save(entity);
        return diningTableMapper.toDTO(entity);
    }

    @Override
    public DiningTable update(DiningTable diningTable) throws ConcurrencyException {
        try {
            var entity = diningTableMapper.toEntity(diningTable);
            entity = diningTableJPARepository.save(entity);
            return diningTableMapper.toDTO(entity);
        } catch (ConcurrencyFailureException e) {
            throw new ConcurrencyException(e);
        }

    }

    @Override
    public void delete(Long id) {
        diningTableJPARepository.deleteById(id);
    }

    @Override
    public boolean exists(String name) {
        return diningTableJPARepository.existsByNameIgnoreCase(name);
    }

    @Override
    public boolean existReservationForTable(Long id) {
        return reservationJPARepository.existsByAssignedTableId(id);
    }

    @Override
    public boolean existsSeatingForTable(Long id) {
        return tableAssignmentJPARepository.existsByDiningTable_Id(id);
    }


}
