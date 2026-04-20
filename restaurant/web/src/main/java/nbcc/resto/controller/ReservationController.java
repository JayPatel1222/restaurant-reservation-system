package nbcc.resto.controller;

import jakarta.servlet.http.HttpServletRequest;
import nbcc.common.service.LoginService;
import nbcc.resto.dto.DiningTable;
import nbcc.resto.dto.Reservation;
import nbcc.resto.dto.ReservationStatus;
import nbcc.resto.dto.TableAssignment;
import nbcc.resto.service.*;
import nbcc.resto.viewmodel.DiningTableListViewModel;
import nbcc.resto.viewmodel.ReservationListViewModel;
import nbcc.resto.viewmodel.SeatingListViewModel;
import nbcc.resto.viewmodel.TableAssignmentViewModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static nbcc.common.validation.ModelErrorConverter.addErrorsToBindingResults;

@Controller
@PreAuthorize("isAuthenticated()")
@RequestMapping("/reservation")
public class ReservationController {

    private static final Logger logger = LoggerFactory.getLogger(ReservationController.class);

    private final SeatingService seatingService;
    private final UrbanPlatterEventService eventService;
    private final ReservationService reservationService;
    private final LoginService loginService;
    private final DiningTableService tableService;

    public ReservationController(SeatingService seatingService, UrbanPlatterEventService eventService, ReservationService reservationService, LoginService loginService, DiningTableService tableService) {
        this.seatingService = seatingService;
        this.eventService = eventService;
        this.reservationService = reservationService;
        this.loginService = loginService;
        this.tableService = tableService;
    }

    @PreAuthorize("permitAll()")
    @GetMapping
    public String getEvents(Model model)
    {
        var result = eventService.getAll();

        if (result.isError()) {
            model.addAttribute("message", "Error retrieving events");
            return "error/errorPage";
        }

        var viewModel = new ReservationListViewModel(result.getValue(), loginService.isLoggedIn());
        model.addAttribute("viewModel", viewModel);
        return "reservation/eventList";
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/{eventId}")
    public String getSeatings(@PathVariable("eventId") Long eventId, Model model)
    {
        var result = eventService.get(eventId);

        if(result.isError()  || result.isEmpty()){
            model.addAttribute("message", "Error retrieving events.");
            return "error/errorPage";
        }

        var seatingResult = seatingService.getAll(eventId);

        if(seatingResult.isError()  || seatingResult.isEmpty()){
            model.addAttribute("message", "Error retrieving seatings.");
            return "error/errorPage";
        }

        var viewModel = new SeatingListViewModel(seatingResult.getValue(), eventId, false, true);
        var platterEvent = result.getValue();

        model.addAttribute("event", platterEvent);
        model.addAttribute("menu",platterEvent.getMenu());
        model.addAttribute("viewModel", viewModel);

        return "reservation/seatingList";
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/{eventId}/book/{seatingId}")
    public String create(@PathVariable("eventId") Long eventId, @PathVariable("seatingId") Long seatingId, Model model)
    {
        var result = seatingService.get(seatingId);

        if(result.isError()  || result.isEmpty()){
            model.addAttribute("message", "Error retrieving seating.");
            return "error/errorPage";
        }

        var resultEvent = eventService.get(eventId);

        model.addAttribute("seating", result.getValue());
        model.addAttribute("event", resultEvent.getValue());
        model.addAttribute("menu", resultEvent.getValue().getMenu());
        model.addAttribute("reservation", new Reservation());
        return "reservation/create";
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/{eventId}/book/{seatingId}")
    public String create(@PathVariable Long eventId, @PathVariable Long seatingId,
                         @ModelAttribute("reservation") Reservation reservation,
                         BindingResult br, Model model,
                         RedirectAttributes redirectAttributes) {

        logger.debug("Attempting to create reservation for event with id {}" , eventId);

        var eventResult = eventService.get(eventId);
        var seatingResult = seatingService.get(seatingId);
        if (eventResult.isError() || eventResult.isEmpty() || seatingResult.isError() || seatingResult.isEmpty() ) {
            model.addAttribute("message", "There was a problem trying to retrieve the Event you are trying to book for.");
            return "error/errorPage";
        }

        reservation.setSeating(seatingResult.getValue());
        var result = reservationService.create(reservation);

        if (result.isError()) {
            model.addAttribute("message", "There was a problem trying to book.");
            return "error/errorPage";
        }

        if (result.isInvalid()) {
            addErrorsToBindingResults(br, result);

            // 🌟 get Menu for this Event/Seating
            model.addAttribute("seating", seatingResult.getValue());
            model.addAttribute("event",eventResult.getValue());
            model.addAttribute("menu", eventResult.getValue().getMenu());
            model.addAttribute("reservation", reservation);
            return "reservation/create";
        }

        String uuid = result.getValue().getReservationId();
        redirectAttributes.addAttribute("eventId", eventId);
        redirectAttributes.addAttribute("seatingId", seatingId);
        return "redirect:/reservation/confirmation/" + uuid;
    }


    @GetMapping("/list")
    public String getAll(Model model) {
        return getAllReservations(model);
    }

    @GetMapping("/list/event/{eventId}")
    public String getByEvent(@PathVariable Long eventId, Model model) {

        var resultEvent = eventService.get(eventId);

        if (resultEvent.isError()  || resultEvent.isEmpty()){
            return "error/errorPage";
        }

        var platterEvent = resultEvent.getValue();
        var result = reservationService.getAll(platterEvent.getId());

        if (result.isError()) {
            return "error/errorPage";
        }

        if (result.isEmpty()) {
            model.addAttribute("message", "No reservations found.");
            return "error/errorPage";
        }

        var viewModel = new ReservationListViewModel(false, result.getValue());
        model.addAttribute("viewModel", viewModel);

        return "reservation/listByEvent";

    }

    @GetMapping("/details/{requestId}")
    public String details(@PathVariable Long requestId, Model model){
        var result = reservationService.get(requestId);

        if (result.isError() || result.isEmpty()) {
            model.addAttribute("message", "The reservation request you are trying to view was not found.");
            return "error/errorPage";
        }

        model.addAttribute("reservation", result.getValue());
        return "reservation/details";
    }

    @GetMapping("/filter")
    public String filter(@RequestParam(required = false) Long eventId,
                         @RequestParam(required = false) ReservationStatus status,
                         Model model){

        if (eventId == null && status == null) {
            model.addAttribute("filterMessage", "Please select a Event or a Status to filter.");
            return getAllReservations(model);
        }

        var result = reservationService.filter(eventId, status);

        if (result.isError() || result.isEmpty()) {
            model.addAttribute("message", "Error retrieving reservations.");
            return "error/errorPage";
        }

        var resultEvents =  eventService.getAll();
        var viewModel = new ReservationListViewModel(resultEvents.getValue(), false, result.getValue(), true);
        viewModel.setShowBackButton(true);
        viewModel.setFilterByEvent(eventId);
        viewModel.setFilterByStatus(status);

        model.addAttribute("viewModel", viewModel);
        return "reservation/list";  // check
    }

    public String getAllReservations(Model model) {

        var result = reservationService.getAll();

        if (result.isError()) {
            model.addAttribute("message", "Error retrieving reservations.");
            return "error/errorPage";
        }

        var  eventsResult = eventService.getAll();
        if (eventsResult.isError()) {
            model.addAttribute("message", "Error retrieving events");
            return "error/errorPage";
        }

        var viewModel = new ReservationListViewModel(eventsResult.getValue(), true,
                result.getValue(), true);
        model.addAttribute("viewModel", viewModel);

        return "reservation/list";
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/confirmation/{uuid}")
    public String confirmationDetails(@PathVariable UUID uuid, Model model) {

        model.addAttribute("confirmation", "We are pleased to inform you that your reservation request has been submitted.");
        model.addAttribute("reservationId", uuid.toString());
        return "reservation/booked";
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/booking/{uuid}")
    public String getByUUID(@PathVariable String uuid, Model model){

        var result = reservationService.getByUUID(uuid);

        if (result.isError() || result.isEmpty()) {
            model.addAttribute("message", "The reservation request you are trying to view was not found.");
            return "error/errorPage";
        }

        if (result.isInvalid()) {
            model.addAttribute("message", uuid + " is not a valid reservation ID.");
            return "error/errorPage";
        }

        model.addAttribute("reservation", result.getValue());
        return "reservation/bookingDetails";
    }

    @GetMapping("/details/{requestId}/assignTable")
    public String assignTable(@PathVariable Long requestId, Model model){

        var result = reservationService.get(requestId);

        if (result.isError() || result.isEmpty()) {
            return "error/errorPage";
        }

        var reservation = result.getValue();
        var seating = reservation.getSeating();

        var tablesResult = reservationService.getAvailableTables(seating.getId());

        var viewModel = new DiningTableListViewModel(tablesResult.getValue());
        model.addAttribute("viewModel", viewModel);
        model.addAttribute("reservation", reservation);

        return "reservation/approve";
    }


    @PostMapping("/{requestId}/approve/{tableId}")
    public String approve(@PathVariable Long requestId, @PathVariable Long tableId, Model model){

        var result = reservationService.get(requestId);

        if (result.isError() || result.isEmpty()) {
            return "error/errorPage";
        }

        var tableResult = tableService.get(tableId);

        if (tableResult.isError() || tableResult.isEmpty()) {
            return "error/errorPage";
        }

        var reservation = result.getValue();
        var table = tableResult.getValue();

        reservation.setAssignedTable(table);
        reservation.setStatus(ReservationStatus.APPROVED);

        var resultUpdate = reservationService.update(reservation);

        if (resultUpdate.isError()) {
            return "error/errorPage";
        }

        if (resultUpdate.isInvalid()) {

            var seating = reservation.getSeating();
            var tablesResult = reservationService.getAvailableTables(seating.getId());

            var viewModel = new DiningTableListViewModel(tablesResult.getValue());

            model.addAttribute("errors", resultUpdate.getValidationErrors());
            model.addAttribute("viewModel", viewModel);
            model.addAttribute("reservation", reservation);

            return "reservation/approve";
        }

        return "redirect:/reservation/details/" + reservation.getId();
    }

    @PostMapping("/{requestId}/deny")
public String deny(@PathVariable Long requestId, Model model){

    var result = reservationService.get(requestId);

    if (result.isError() || result.isEmpty()) {
        return "error/errorPage";
    }

    var reservation = result.getValue();
    reservation.setStatus(ReservationStatus.DENIED);

    var resultUpdate = reservationService.update(reservation);

    if (resultUpdate.isError()) {
        return "error/errorPage";
    }

    return "redirect:/reservation/details/" + reservation.getId();
}


    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String exceptionHandler(Model model, Exception ex, HttpServletRequest request){
        logger.error("Unexpected Exception on uri {}: on method {} ", request.getRequestURI() , request.getMethod(), ex);
        model.addAttribute("message", "Unexpected Error Occurred");
        return "error/errorPage";
    }



}
