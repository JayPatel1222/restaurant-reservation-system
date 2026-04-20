package nbcc.resto.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import nbcc.common.result.ValidatedResult;
import nbcc.common.result.ValidationResults;
import nbcc.resto.controller.api.dto.RequestReservation;
import nbcc.resto.controller.api.dto.ResponseReservation;
import nbcc.resto.controller.api.mapper.ReservationApiMapper;
import nbcc.resto.service.ReservationService;
import nbcc.resto.service.SeatingService;
import nbcc.resto.service.UrbanPlatterEventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static nbcc.resto.controller.api.result.ResultHandler.handleResult;

@Tag(name = "Reservations API", description = "Reservation API to request a reservation")
@RestController
@RequestMapping("/api/reservation")
public class ReservationApiController {

    public final ReservationService reservationService;
    public final UrbanPlatterEventService eventService;
    public final SeatingService  seatingService;
    public final ReservationApiMapper reservationApiMapper;

    public ReservationApiController(ReservationService reservationService, UrbanPlatterEventService eventService, SeatingService seatingService, ReservationApiMapper requestReservationMapper) {
        this.reservationService = reservationService;
        this.eventService = eventService;
        this.seatingService = seatingService;
        this.reservationApiMapper = requestReservationMapper;
    }

    @Operation(summary = "Request a reservation")
    @PostMapping
    public ResponseEntity<ValidatedResult<ResponseReservation>> requestReservation(@RequestBody RequestReservation reservation) {

        var reservationDTO = reservationApiMapper.toDTO(reservation);

        var resultEvent = eventService.get(reservation.getEventId());
        var resultSeating = seatingService.get(reservation.getSeatingId());

        if (resultEvent.hasValue() && resultSeating.hasValue()) {
            var seating = resultSeating.getValue();
            var platterEvent = resultEvent.getValue();

            seating.setPlatterEvent(platterEvent);
            reservationDTO.setSeating(seating);
        }

        var result = reservationService.create(reservationDTO);

        ValidatedResult<ResponseReservation> responseResult;

        if (result.isSuccessful()) {
            var response = reservationApiMapper.toResponse(result.getValue());
            responseResult = ValidationResults.success(response);
        } else if (result.isInvalid()) {
            responseResult = ValidationResults.invalid(null, result.getValidationErrors());
        } else {
            responseResult = ValidationResults.error();
        }

        return handleResult(responseResult, HttpStatus.CREATED);
    }
}
