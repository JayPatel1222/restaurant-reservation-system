package nbcc.resto.repository;

import nbcc.resto.entity.SeatingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SeatingJPARepository extends JpaRepository<SeatingEntity, Long> {
    List<SeatingEntity> findByPlatterEventIdAndArchivedIsFalse(Long eventId);
    boolean existsByNameIgnoreCase(String name);
    List<SeatingEntity> findAllByArchivedIsFalse();
    Optional<SeatingEntity> findByIdAndArchivedIsFalse(Long id);
}
