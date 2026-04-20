package nbcc.resto.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import nbcc.common.result.Result;
import nbcc.common.result.ValidatedResult;
import nbcc.resto.dto.EventDetails;
import nbcc.resto.dto.UrbanPlatterEvent;
import nbcc.resto.service.MenuService;
import nbcc.resto.service.SeatingService;
import nbcc.resto.service.UrbanPlatterEventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

import static nbcc.resto.controller.api.result.ResultHandler.handleResult;

@Tag(name = "Events API", description = "Event Related Information")
@RestController
@RequestMapping("/api/event")
public class EventApiController {
    public final UrbanPlatterEventService eventService;
    public final SeatingService seatingService;
    public final MenuService menuService;

    public EventApiController(UrbanPlatterEventService eventService, SeatingService seatingService, MenuService menuService) {
        this.eventService = eventService;
        this.seatingService = seatingService;
        this.menuService = menuService;
    }

    @Operation(summary = "Get all Events")
    @GetMapping
    public ResponseEntity<Result<Collection<EventDetails>>> getAll() {

        var events = eventService.getAllEventDetails();
        return handleResult(events, HttpStatus.OK);
    }

    @Operation(summary = "Get Event By Id")
    @GetMapping("/{id}")
    public ResponseEntity<ValidatedResult<EventDetails>> get(@PathVariable Long id){
        var eventDetails = eventService.getDetailByEventId(id);
        if(eventDetails.getValue().getEvent() == null){
            return handleResult(eventDetails, HttpStatus.NOT_FOUND);
        }

        return handleResult(eventDetails, HttpStatus.OK);
    }
}
