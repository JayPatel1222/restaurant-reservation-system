package nbcc.resto.repository;


import nbcc.resto.dto.ReservationStatus;
import nbcc.resto.entity.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReservationJPARepository extends JpaRepository<ReservationEntity, Long>
{
    List<ReservationEntity> findBySeatingPlatterEventIdOrderBySeatingStartDateAsc(Long eventId);
    List<ReservationEntity> findByStatusOrderBySeatingStartDateAsc(ReservationStatus status);
    List<ReservationEntity> findBySeatingPlatterEventIdAndStatusOrderBySeatingStartDateAsc(Long eventId, ReservationStatus status);
    List<ReservationEntity> findAllByOrderBySeatingStartDateAsc();
    Optional<ReservationEntity> findByReservationId(String reservationId);
    List<ReservationEntity> findAllBySeatingId(Long seatingId);
    boolean existsBySeatingId(Long seatingId);
    boolean existsByAssignedTableId(Long assignedTableId);
}
