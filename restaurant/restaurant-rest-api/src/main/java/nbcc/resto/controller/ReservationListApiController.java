package nbcc.resto.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import nbcc.common.result.Result;
import nbcc.common.result.ValidatedResult;
import nbcc.resto.dto.Menu;
import nbcc.resto.dto.Reservation;
import nbcc.resto.service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

import static nbcc.resto.controller.api.result.ResultHandler.handleResult;

@Tag(name = "Reservations API", description = "Reservation Related Information")
@RestController
@RequestMapping("/api/reservationList")
public class ReservationListApiController {
    public final ReservationService reservationService;

    public ReservationListApiController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @Operation(summary = "Get all Reservations")
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<Result<Collection<Reservation>>> getAll() {

        var reservations = reservationService.getAll();
        return handleResult(reservations, HttpStatus.OK);
    }

    @Operation(summary = "Get Reservation By Id")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ResponseEntity<ValidatedResult<Reservation>> get(@PathVariable Long id){

        var reservation = reservationService.get(id);
        return handleResult(reservation,HttpStatus.OK,HttpStatus.NOT_FOUND);
    }
}
