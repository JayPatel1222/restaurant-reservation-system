package nbcc.resto.repository;

import nbcc.common.exception.ConcurrencyException;
import nbcc.resto.dto.Reservation;
import nbcc.resto.dto.ReservationStatus;
import nbcc.resto.entity.ReservationEntity;
import nbcc.resto.mapper.ReservationMapper;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ReservationRepositoryAdapter implements ReservationRepository {

    private final ReservationMapper reservationMapper;
    private final ReservationJPARepository reservationJPARepository;

    public ReservationRepositoryAdapter(ReservationMapper reservationMapper, ReservationJPARepository reservationJPARepository) {
        this.reservationMapper = reservationMapper;
        this.reservationJPARepository = reservationJPARepository;
    }

    @Override
    public Reservation create(Reservation reservation) {
        var entity = reservationMapper.toEntity(reservation);
        entity = reservationJPARepository.save(entity);
        return reservationMapper.toDTO(entity);
    }

    @Override
    public List<Reservation> getAll() {
        var entities = reservationJPARepository.findAllByOrderBySeatingStartDateAsc();
        return reservationMapper.toDTO(entities);
    }

    @Override
    public List<Reservation> getAll(Long eventId) {
        var entities = reservationJPARepository.findBySeatingPlatterEventIdOrderBySeatingStartDateAsc(eventId);
        return reservationMapper.toDTO(entities);
    }

    @Override
    public Optional<Reservation> get(Long id) {
        var entity = reservationJPARepository.findById(id);
        return reservationMapper.toDTO(entity);
    }

    @Override
    public Optional<Reservation> getByUUID(String reservationId) {
        var entity =  reservationJPARepository.findByReservationId(reservationId);
        return reservationMapper.toDTO(entity);
    }

    @Override
    public List<Reservation> getApproved(Long seatingId) {
        var entities = reservationJPARepository.findAllBySeatingId(seatingId);
        var filteredEntities = entities.stream().filter( r -> r.getStatus().equals(ReservationStatus.APPROVED)).toList();
        return reservationMapper.toDTO(filteredEntities);
    }

    @Override
    public Reservation update(Reservation reservation) throws ConcurrencyException {
        try {
            var entity = reservationMapper.toEntity(reservation);
            entity = reservationJPARepository.save(entity);
            return reservationMapper.toDTO(entity);
        } catch (ConcurrencyFailureException e) {
            throw new ConcurrencyException(e);
        }
    }

    @Override
    public List<Reservation> filter(Long eventId, ReservationStatus status) {

        List<ReservationEntity> entities;

        if (eventId != null && eventId > 0 && status != null) {
            entities = reservationJPARepository.findBySeatingPlatterEventIdAndStatusOrderBySeatingStartDateAsc(eventId, status);
        } else if (eventId != null && eventId > 0) {
            entities = reservationJPARepository.findBySeatingPlatterEventIdOrderBySeatingStartDateAsc(eventId);
        } else if (status != null) {
            entities = reservationJPARepository.findByStatusOrderBySeatingStartDateAsc(status);
        } else {
            entities = List.of();
        }

        return reservationMapper.toDTO(entities);
    }
}
