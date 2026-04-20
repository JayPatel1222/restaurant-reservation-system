package nbcc.resto.controller;

import jakarta.servlet.http.HttpServletRequest;
import nbcc.resto.dto.ReservationStatus;
import nbcc.resto.dto.UrbanPlatterEvent;
import nbcc.resto.repository.UrbanPlatterEventRepository;
import nbcc.resto.service.MenuService;
import nbcc.resto.service.ReservationService;
import nbcc.resto.service.SeatingService;
import nbcc.resto.service.UrbanPlatterEventService;
import nbcc.resto.viewmodel.ReservationListViewModel;
import nbcc.resto.viewmodel.SeatingListViewModel;
import nbcc.resto.viewmodel.UrbanPlatterEventListViewModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static nbcc.common.validation.ModelErrorConverter.addErrorsToBindingResults;
@Controller
@PreAuthorize("isAuthenticated()")
@RequestMapping({"/event"})
public class EventController {

    private final UrbanPlatterEventService eventService;
    private final SeatingService seatingService;
    private final MenuService menuService;
    private final ReservationService reservationService;
    private final Logger logger = LoggerFactory.getLogger(EventController.class);

    public EventController(UrbanPlatterEventService eventService, SeatingService seatingService, MenuService menuService, ReservationService reservationService) {
        this.eventService = eventService;
        this.seatingService = seatingService;
        this.menuService = menuService;
        this.reservationService = reservationService;
    }

    @GetMapping
    public String getAll(Model model){
        var results = eventService.getAll();

        if(results.isError()){
            model.addAttribute("message","Error retrieving Events");
            return "error/errorPage";
        }


        var viewModel = new UrbanPlatterEventListViewModel(results.getValue());
        model.addAttribute("viewModel",viewModel);
        return "event/list";
    }

    @GetMapping("/{eventId}")
    public String details(@PathVariable Long eventId,Model model){
        var result = eventService.get(eventId);

        if (result.isError()) {
            return "error/errorPage";
        }

        if (result.isEmpty()) {
            model.addAttribute("message", "The Event you are trying to view was not found");
            return "error/errorPage";
        }

        var reservationsResult = reservationService.filter(eventId, ReservationStatus.APPROVED);

        if (reservationsResult.isError() ||  reservationsResult.isEmpty()) {
            model.addAttribute("message", "Reservations not found");
        }

        var viewModel = new ReservationListViewModel(false, reservationsResult.getValue());
        viewModel.setTitle("Approved Reservations");
        viewModel.setHideStatus(true);
        model.addAttribute("viewModel", viewModel);

        model.addAttribute("event",result.getValue());
        return "event/details";
    }

    @GetMapping("/create")
    public String create(Model model){
        var result = menuService.getAll();

        model.addAttribute("menus",result.getValue());
        model.addAttribute("event", new UrbanPlatterEvent());
        return "event/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("event") UrbanPlatterEvent event, BindingResult br, Model model){

        var result = eventService.create(setMenu(event));

        if(result.isError()){
            return "error/errorPage";
        }

        if(result.isInvalid()){
            addErrorsToBindingResults(br,result);
            setMenuAgain(event, model);
            model.addAttribute("event", event);

            return "event/create";
        }

        return "redirect:/event";
    }

    @GetMapping("/edit/{eventId}")
    public String edit(@PathVariable Long eventId, Model model){
        var result = eventService.get(eventId);

        if(result.isError()){
            return "error/errorPage";
        }
        var menus = menuService.getAll();

        model.addAttribute("menus",menus.getValue());

        var seatingResult = seatingService.getAll(eventId);
        var viewModel = new SeatingListViewModel(seatingResult.getValue(), eventId, false);
        model.addAttribute("event", result.getValue());
        model.addAttribute("viewModel", viewModel);
        return "event/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@ModelAttribute("event") UrbanPlatterEvent event,BindingResult br,Model model){
        var result = eventService.update(setMenu(event));

        if (result.isError()) {
            return "error/errorPage";
        }

        if (result.isInvalid()) {
            var seatingResult = seatingService.getAll(event.getId());
            var viewModel = new SeatingListViewModel(seatingResult.getValue(), event.getId(), false);
            model.addAttribute("viewModel", viewModel);
            setMenuAgain(event, model);
            addErrorsToBindingResults(br, result);
            return "event/edit";
        }

        return "redirect:/event";
    }


    @GetMapping("/delete/{eventId}")
    public String delete(@PathVariable Long eventId, Model model) {
        var result = eventService.get(eventId);

        if(result.isError()){
            return "error/errorPage";
        }

        var seatingResult = seatingService.getAll(result.getValue().getId());
        var viewModel = new SeatingListViewModel(seatingResult.getValue(), result.getValue().getId(), false);
        model.addAttribute("viewModel", viewModel);
        model.addAttribute("event",result.getValue());

        return "event/delete";
    }

    @PostMapping("/delete/{eventId}")
    public String delete(@PathVariable Long eventId){
        var result = eventService.delete(eventId);

        if (result.isError()) {
            return "error/errorPage";
        }
        return "redirect:/event";
    }

    @GetMapping("/search")
    public String search(@RequestParam(required = false) String eventName,
                         @RequestParam(required = false) LocalDateTime startDate,
                         @RequestParam(required = false) LocalDateTime endDate,
                         Model model){
        var results = eventService.search(eventName,startDate,endDate);
        var viewModel = new UrbanPlatterEventListViewModel(results.getValue());
        if(eventName.isBlank() && startDate == null && endDate == null){
            viewModel.setShowBackButton(true);
            viewModel.setTextFlag(false);
            model.addAttribute("viewModel",viewModel);
            model.addAttribute("searchErrors","Please select any one criteria to search");

            return "event/list";
        }

        if(results.isError()){
            return "error/errorPage";
        }

        if(results.hasValue()){
            viewModel.setShowBackButton(true);
            viewModel.setEventName(eventName);
            viewModel.setStartDate(startDate);
            viewModel.setEndDate(endDate);
            viewModel.setTextFlag(false);
            model.addAttribute("viewModel",viewModel);

        }

        return "event/list";
    }

    @GetMapping("/filter")
    public String filter(@RequestParam(required = false) LocalDateTime startDate,
                         @RequestParam(required = false) LocalDateTime endDate,
                         Model model){
        var results = eventService.filter(startDate,endDate);

        if(results.isError()){
            return "error/errorPage";
        }

        var viewModel = new UrbanPlatterEventListViewModel(results.getValue());
        if(startDate == null && endDate == null){
            model.addAttribute("filterErrors","Please select any one filters to search");
        }
        viewModel.setShowBackButton(true);
        viewModel.setFilterStartDate(startDate);
        viewModel.setFilterEndDate(endDate);
        viewModel.setTextFlag(true);
        model.addAttribute("viewModel",viewModel);


        return "event/list";
    }

    private UrbanPlatterEvent setMenu(UrbanPlatterEvent event){
        if(event.getMenu() != null && event.getMenu().getId() != null){
            return event.setMenu(menuService.get(event.getMenu().getId()).getValue());
        }
        return event.setMenu(null);
    }
    private void setMenuAgain(@ModelAttribute("event") UrbanPlatterEvent event, Model model) {
        if(event.getMenu() != null && event.getMenu().getId() != null){
            var menu = menuService.get(event.getMenu().getId());
            event.setMenu(menu.getValue());
        }

        var menus = menuService.getAll();
        model.addAttribute("menus", menus.getValue());
    }


    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String exceptionHandler(Model model, Exception ex, HttpServletRequest request){
        logger.error("Unexpected Exception on uri {}: on method {} ", request.getRequestURI() , request.getMethod(), ex);
        model.addAttribute("message", "Unexpected Error Occurred");
        return "error/errorPage";
    }

}
