package nbcc.resto.repository;

import nbcc.resto.dto.UrbanPlatterEvent;
import nbcc.resto.entity.UrbanPlatterEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface UrbanPlatterEventJPARepository extends JpaRepository<UrbanPlatterEventEntity, Long> {
    boolean existsByNameIgnoreCase(String name);
    List<UrbanPlatterEventEntity> findByNameContainingIgnoreCase(String name);
    List<UrbanPlatterEventEntity> findByStartDate(LocalDateTime startDate);
    List<UrbanPlatterEventEntity> findByEndDate(LocalDateTime endDate);
    List<UrbanPlatterEventEntity> findByNameContainingIgnoreCaseAndStartDate(String name, LocalDateTime startDate);
    List<UrbanPlatterEventEntity> findByNameContainingIgnoreCaseAndEndDate(String name, LocalDateTime endDate);
    List<UrbanPlatterEventEntity> findByStartDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<UrbanPlatterEventEntity> findByNameContainingIgnoreCaseAndStartDateAndEndDate(String name,LocalDateTime startDate,LocalDateTime endDate);
    List<UrbanPlatterEventEntity> findByStartDateAfter(LocalDateTime startDate);
    List<UrbanPlatterEventEntity> findByEndDateBefore(LocalDateTime endDate);
    @Query("SELECT e FROM UrbanPlatterEventEntity e WHERE e.startDate BETWEEN :startDate AND :endDate")
    List<UrbanPlatterEventEntity> findByDateBetween(LocalDateTime startDate, LocalDateTime endDate);

}
