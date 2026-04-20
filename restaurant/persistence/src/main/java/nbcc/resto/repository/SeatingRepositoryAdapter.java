package nbcc.resto.repository;

import nbcc.common.exception.ConcurrencyException;
import nbcc.resto.dto.Seating;
import nbcc.resto.mapper.SeatingMapper;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class SeatingRepositoryAdapter implements SeatingRepository {

    private final SeatingMapper seatingMapper;
    private final SeatingJPARepository seatingJPARepository;
    private final ReservationJPARepository reservationJPARepository;

    public SeatingRepositoryAdapter(SeatingMapper seatingMapper, SeatingJPARepository seatingJPARepository, ReservationJPARepository reservationJPARepository) {
        this.seatingMapper = seatingMapper;
        this.seatingJPARepository = seatingJPARepository;
        this.reservationJPARepository = reservationJPARepository;
    }

    @Override
    public List<Seating> getAll() {
        var seatings = seatingJPARepository.findAllByArchivedIsFalse();
        return seatingMapper.toDTO(seatings);
    }

    @Override
    public List<Seating> getAll(Long eventId) {
       var seatings = seatingJPARepository.findByPlatterEventIdAndArchivedIsFalse(eventId);
       return seatingMapper.toDTO(seatings);
    }

    @Override
    public Optional<Seating> get(Long id) {
        var entity = seatingJPARepository.findByIdAndArchivedIsFalse(id);
        return seatingMapper.toDTO(entity);
    }

    @Override
    public Seating create(Seating seating) {
        var entity = seatingMapper.toEntity(seating);
        entity = seatingJPARepository.save(entity);
        return seatingMapper.toDTO(entity);

    }

    @Override
    public boolean exists(String name) {
        return seatingJPARepository.existsByNameIgnoreCase(name);
    }

    @Override
    public void delete(Long id) {
        seatingJPARepository.deleteById(id);
    }

    @Override
    public Seating update(Seating seating) throws ConcurrencyException {
        try {
            var entity = seatingMapper.toEntity(seating);
            entity = seatingJPARepository.save(entity);
            return seatingMapper.toDTO(entity);
        } catch (ConcurrencyFailureException e) {
            throw new ConcurrencyException(e);
        }
    }

    @Override
    public boolean existReservationForSeating(Long seatingId) {
        return  reservationJPARepository.existsBySeatingId(seatingId);
    }
}
