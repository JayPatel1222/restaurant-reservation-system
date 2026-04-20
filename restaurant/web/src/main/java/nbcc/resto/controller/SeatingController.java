package nbcc.resto.controller;

import jakarta.servlet.http.HttpServletRequest;
import nbcc.resto.dto.DiningTable;
import nbcc.resto.dto.Seating;
import nbcc.resto.service.DiningTableService;
import nbcc.resto.service.SeatingService;
import nbcc.resto.service.UrbanPlatterEventService;
import nbcc.resto.viewmodel.ReservationListViewModel;
import nbcc.resto.viewmodel.SeatingEditViewModel;
import nbcc.resto.viewmodel.SeatingListViewModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static nbcc.common.validation.ModelErrorConverter.addErrorsToBindingResults;

@Controller
@PreAuthorize("isAuthenticated()")
@RequestMapping
public class SeatingController {

    private final Logger logger = LoggerFactory.getLogger(SeatingController.class);

    private final SeatingService seatingService;
    private final UrbanPlatterEventService eventService;


    public SeatingController(SeatingService seatingService, UrbanPlatterEventService eventService, DiningTableService tableService) {
        this.seatingService = seatingService;
        this.eventService = eventService;
    }

    @GetMapping("/seating")
    public String seating(Model model) {
        var result = seatingService.getAll();

        if (result.isError()) {
            model.addAttribute("message", "Error retrieving seatings");
            return "error/errorPage";
        }

        var viewModel = new SeatingListViewModel(result.getValue());
        model.addAttribute("viewModel", viewModel);
        return "seating/list";
    }

    @GetMapping("/event/{eventId}/seating/create")
    public String create(@PathVariable Long eventId, Model model) {

        logger.debug("Attempting to load new seating for creation with eventId {}" , eventId);

        var eventResult = eventService.get(eventId);

        if (eventResult.isError()) {
            return "error/errorPage";
        }

        if (eventResult.isEmpty()) {
            model.addAttribute("message", "The Event you are trying to add a Seating was not found");
            return "error/errorPage";
        }

        var platterEvent = eventResult.getValue();
        var seating = new Seating(platterEvent);
        seating.setDuration(platterEvent.getMinutes());

        model.addAttribute("event", platterEvent);
        model.addAttribute("seating", seating);

        return "seating/create";
    }

    @PostMapping("/event/{eventId}/seating/create")
    public String create(@PathVariable Long eventId, @ModelAttribute("seating") Seating seating,
                         BindingResult br, Model model, RedirectAttributes redirectAttributes) {

        logger.debug("Attempting to create seating for event with id {}" , eventId);

        var eventResult = eventService.get(eventId);
        if (eventResult.isError() || eventResult.isEmpty()) {
            model.addAttribute("message", "There was a problem trying to retrieve the Event you are trying to add a Seating to.");
            return "error/errorPage";
        }

        seating.setPlatterEvent(eventResult.getValue());
        var seatingResult = seatingService.create(seating);

        if (seatingResult.isError()) {
            model.addAttribute("message", "There was a problem trying to create Seating");
            return "error/errorPage";
        }

        if (seatingResult.isInvalid()) {
            addErrorsToBindingResults(br, seatingResult);
            model.addAttribute("event",eventResult.getValue());
            return "seating/create";
        }

        redirectAttributes.addAttribute("eventId", eventId);
        return "redirect:/event/edit/{eventId}";
    }

    @GetMapping("/seating/edit/{id}")
    public String editSelectEvent(@PathVariable Long id, Model model) {

        logger.debug("Loading events to edit Seating with id {}" , id);

        var result = seatingService.get(id);

        if (result.isError() || result.isEmpty()) {
            model.addAttribute("message", "The seating you are trying to edit was not found");
            return "error/errorPage";
        }
        var eventResult = eventService.getAll();

        if (eventResult.isError() ||  eventResult.isEmpty()) {
            model.addAttribute("message", "Something went wrong while retrieving events.");
            return "error/errorPage";
        }

        var viewModel = new SeatingEditViewModel(eventResult.getValue());
        model.addAttribute("viewModel", viewModel);
        model.addAttribute("seating", result.getValue());
        return "seating/eventList";
    }

    @GetMapping("/seating/edit/{id}/{eventId}")
    public String edit(@PathVariable("id") Long id, @PathVariable("eventId") Long eventId, Model model) {

        logger.debug("Loading information for Seating with id {}" , id);

        var result = seatingService.get(id);

        if (result.isError() || result.isEmpty()) {
            model.addAttribute("message", "The seating you are trying to edit was not found");
            return "error/errorPage";
        }
        var resultEvent = eventService.get(eventId);

        if(resultEvent.isError()  || resultEvent.isEmpty()){
            model.addAttribute("message", "Error retrieving event.");
            return "error/errorPage";
        }

        model.addAttribute("event", resultEvent.getValue());
        model.addAttribute("seating", result.getValue());
        return "seating/edit";
    }

    @PostMapping("/seating/edit/{id}/{eventId}")
    public String edit(@PathVariable Long eventId, @PathVariable Long id,
                       @ModelAttribute("seating") Seating seating,  BindingResult br, Model model) {

        logger.debug("Attempting to update Seating with id {}" , id);

        var resultEvent = eventService.get(eventId);

        if(resultEvent.isError()  || resultEvent.isEmpty()){
            model.addAttribute("message", "Error retrieving event.");
            return "error/errorPage";
        }

        seating.setPlatterEvent(resultEvent.getValue());

        var result = seatingService.update(seating);

        if (result.isError() || result.isEmpty()) {
            model.addAttribute("message", "Something went wrong while updating seating.");
            return "error/errorPage";
        }

        if (result.isInvalid()) {
            addErrorsToBindingResults(br, result);

            model.addAttribute("seating", seating);
            model.addAttribute("event", resultEvent.getValue());

            return "seating/edit";
        }
        return "redirect:/seating";
    }

    @GetMapping("/seating/delete/{id}")
    public String delete(@PathVariable long id, Model model) {

        logger.debug("Loading information to delete Seating with id {}" , id);

        var result = seatingService.get(id);

        if (result.isError()) {
            return "error/errorPage";
        }

        if (result.isEmpty()) {
            model.addAttribute("message", "The seating you are trying to delete was not found");
            return "error/errorPage";
        }

        var seating = result.getValue();
        model.addAttribute("event", seating.getPlatterEvent());
        model.addAttribute("seating", seating);
        return "seating/delete";
    }

    @PostMapping("/seating/delete/{id}")
    public String delete(@PathVariable long id) {

        logger.debug("Attempting to delete Seating with id {}" , id);

        var result  = seatingService.delete(id);

        if (result.isError()) {
            return "error/errorPage";
        }

        return "redirect:/seating";
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String exceptionHandler(Model model, Exception ex, HttpServletRequest request){
        logger.error("Unexpected Exception on uri {}: on method {} ", request.getRequestURI() , request.getMethod(), ex);
        model.addAttribute("message", "Unexpected Error Occurred");
        return "error/errorPage";
    }

}
