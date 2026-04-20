package nbcc.resto.repository;

import nbcc.common.exception.ConcurrencyException;
import nbcc.resto.dto.UrbanPlatterEvent;
import nbcc.resto.entity.UrbanPlatterEventEntity;
import nbcc.resto.mapper.UrbanPlatterEventMapper;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public class UrbanPlatterEventRepositoryAdaptor implements UrbanPlatterEventRepository {

    private final UrbanPlatterEventMapper urbanPlatterEventMapper;
    private final UrbanPlatterEventJPARepository urbanPlatterEventJPARepository;

    public UrbanPlatterEventRepositoryAdaptor(UrbanPlatterEventMapper urbanPlatterEventMapper, UrbanPlatterEventJPARepository urbanPlatterEventJPARepository) {
        this.urbanPlatterEventMapper = urbanPlatterEventMapper;
        this.urbanPlatterEventJPARepository = urbanPlatterEventJPARepository;
    }

    @Override
    public List<UrbanPlatterEvent> getAll() {
        var entities = updateIfEventIsOld(urbanPlatterEventJPARepository.findAll());
        return urbanPlatterEventMapper.toDTO(entities);
    }

    @Override
    public Optional<UrbanPlatterEvent> get(Long id) {
        var entity = urbanPlatterEventJPARepository.findById(id);
        return urbanPlatterEventMapper.toDTO(entity);
    }

    @Override
    public UrbanPlatterEvent create(UrbanPlatterEvent event) {
        var entity = urbanPlatterEventMapper.toEntity(event);
        entity = urbanPlatterEventJPARepository.save(entity);
        return urbanPlatterEventMapper.toDTO(entity);
    }

    @Override
    public UrbanPlatterEvent update(UrbanPlatterEvent event) throws ConcurrencyException {
        try {
            var entity = urbanPlatterEventMapper.toEntity(event);
            entity = urbanPlatterEventJPARepository.save(entity);
            return urbanPlatterEventMapper.toDTO(entity);
        } catch (ConcurrencyFailureException e) {
            throw new ConcurrencyException(e);
        }
    }

    @Override
    public void delete(Long id) {
        urbanPlatterEventJPARepository.deleteById(id);
    }

    @Override
    public List<UrbanPlatterEvent> search(String name, LocalDateTime startDate, LocalDateTime endDate) {

        List<UrbanPlatterEventEntity> results;
        boolean hasName = name != null && !name.isBlank();

        if ( hasName && startDate != null && endDate != null) {
            results = urbanPlatterEventJPARepository.
                    findByNameContainingIgnoreCaseAndStartDateAndEndDate(
                            name, startDate, endDate
                    );

        } else if (hasName && startDate != null){
            results = urbanPlatterEventJPARepository.findByNameContainingIgnoreCaseAndStartDate(name,startDate);
        }
        else if(hasName && endDate != null){
            results = urbanPlatterEventJPARepository.findByNameContainingIgnoreCaseAndEndDate(name,endDate);
        }
        else if (startDate != null && endDate != null) {
            results = urbanPlatterEventJPARepository.findByStartDateBetween(startDate, endDate);

        } else if (hasName) {
            results = urbanPlatterEventJPARepository.findByNameContainingIgnoreCase(name);

        } else if (startDate != null) {
            results = urbanPlatterEventJPARepository.findByStartDate(startDate);

        } else if (endDate != null) {
            results = urbanPlatterEventJPARepository.findByEndDate(endDate);

        } else {
            results = List.of();
        }

        return urbanPlatterEventMapper.toDTO(results);
    }

    @Override
    public List<UrbanPlatterEvent> filter(LocalDateTime startDate, LocalDateTime endDate) {

        if(startDate != null && endDate != null){
            var events = urbanPlatterEventJPARepository.findByDateBetween(startDate,endDate);
            return urbanPlatterEventMapper.toDTO(events);
        }
        if(startDate != null){
            var events = urbanPlatterEventJPARepository.findByStartDateAfter(startDate);
            return urbanPlatterEventMapper.toDTO(events);
        }
        if(endDate != null){
            var events = urbanPlatterEventJPARepository.findByEndDateBefore(endDate);
            return urbanPlatterEventMapper.toDTO(events);
        }

        return List.of();
    }

    @Override
    public boolean exists(String name) {
        return urbanPlatterEventJPARepository.existsByNameIgnoreCase(name);
    }

    private Collection<UrbanPlatterEventEntity> updateIfEventIsOld(Collection<UrbanPlatterEventEntity> events){

        events.forEach( event ->{
            var isOld = event.getEndDate().isBefore(LocalDateTime.now());
            if(isOld){
                event.setArchived(true);
                urbanPlatterEventJPARepository.save(event);
            }
        });
        return events;
    }
}
