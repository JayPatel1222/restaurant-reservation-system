package nbcc.resto.mapper;

import nbcc.resto.dto.Reservation;
import nbcc.resto.dto.Seating;
import nbcc.resto.entity.ReservationEntity;
import nbcc.resto.entity.SeatingEntity;
import org.springframework.stereotype.Component;

@Component
public class ReservationMapper implements EntityMapper<Reservation, ReservationEntity> {

    private final SeatingMapper seatingMapper;
    private final DiningTableMapper diningTableMapper;

    public ReservationMapper(SeatingMapper seatingMapper, DiningTableMapper diningTableMapper) {
        this.seatingMapper = seatingMapper;
        this.diningTableMapper = diningTableMapper;
    }

    @Override
    public Reservation toDTO(ReservationEntity entity) {
        if(entity == null){
            return null;
        }

        return new Reservation()
                .setId(entity.getId())
                .setFirstName(entity.getFirstName())
                .setLastName(entity.getLastName())
                .setEmail(entity.getEmail())
                .setReservationId(entity.getReservationId())
                .setSeating(seatingMapper.toDTO(entity.getSeating()))
                .setVersion(entity.getVersion())
                .setGroupSize(entity.getGroupSize())
                .setStatus(entity.getStatus())
                .setAssignedTable(diningTableMapper.toDTO(entity.getAssignedTable()));
    }

    @Override
    public ReservationEntity toEntity(Reservation dto) {
        if(dto == null){
            return null;
        }

        return new ReservationEntity()
                .setId(dto.getId())
                .setReservationId(dto.getReservationId())
                .setFirstName(dto.getFirstName())
                .setLastName(dto.getLastName())
                .setEmail(dto.getEmail())
                .setGroupSize(dto.getGroupSize())
                .setStatus(dto.getStatus())
                .setVersion(dto.getVersion())
                .setAssignedTable(diningTableMapper.toEntity(dto.getAssignedTable()))
                .setSeating(seatingMapper.toEntity(dto.getSeating()));
    }
}
