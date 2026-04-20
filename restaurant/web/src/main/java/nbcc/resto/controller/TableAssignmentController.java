package nbcc.resto.controller;

import jakarta.servlet.http.HttpServletRequest;
import nbcc.resto.service.DiningTableService;
import nbcc.resto.service.SeatingService;
import nbcc.resto.service.TableAssignmentService;
import nbcc.resto.viewmodel.DiningTableListViewModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import static nbcc.common.validation.ModelErrorConverter.addErrorsToBindingResults;

@Controller
@PreAuthorize("isAuthenticated()")
public class TableAssignmentController {

    private final Logger logger = LoggerFactory.getLogger(TableAssignmentController.class);

    private final SeatingService seatingService;
    private final DiningTableService tableService;
    private final TableAssignmentService assignmentService;
    private final DiningTableService diningTableService;


    public TableAssignmentController(SeatingService seatingService, DiningTableService tableService, TableAssignmentService assignmentService, DiningTableService diningTableService) {
        this.seatingService = seatingService;
        this.tableService = tableService;
        this.assignmentService = assignmentService;
        this.diningTableService = diningTableService;
    }

    @GetMapping("/seating/{seatingId}/assignTable")
    public String assignTable(@PathVariable Long seatingId, Model model) {

        var seatingResult = seatingService.get(seatingId);

        if (seatingResult.isError() || seatingResult.isEmpty()) {
            return "error/errorPage";
        }

        var result = diningTableService.getAll();

        if (result.isError() || result.isEmpty()) {
            model.addAttribute("message", "Error retrieving tables");
            return "error/errorPage";
        }

        var viewModel = new DiningTableListViewModel(result.getValue());
        model.addAttribute("viewModel", viewModel);
        model.addAttribute("seating", seatingResult.getValue());

        return "tableassignment/assign"; // modify
    }

    @PostMapping("/seating/{seatingId}/assignTable/{tableId}")
    public String assignTable(@PathVariable Long seatingId, @PathVariable Long tableId,  Model model){

        var diningTable = diningTableService.get(tableId);
        var seating = seatingService.get(seatingId);

        if (diningTable.isError() || seating.isError() || seating.isEmpty() || diningTable.isEmpty()) {
            return "error/errorPage";
        }

        var result = assignmentService.assignTable(diningTable.getValue().getId(), seating.getValue().getId());

        if (result.isError() || result.isEmpty()) {
            return "error/errorPage";
        }

        if (result.isInvalid()) {
            model.addAttribute("errors", result.getValidationErrors());

            var diningTables = diningTableService.getAll();
            if (diningTables.hasValue()) {
                var viewModel = new DiningTableListViewModel(diningTables.getValue());
                model.addAttribute("viewModel", viewModel);
                model.addAttribute("seating", seating.getValue());
            }

            return "tableassignment/assign";
        }

        return "redirect:/event";   //modify
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String exceptionHandler(Model model, Exception ex, HttpServletRequest request){
        logger.error("Unexpected Exception on uri {}: on method {} ", request.getRequestURI() , request.getMethod(), ex);
        model.addAttribute("message", "Unexpected Error Occurred");
        return "error/errorPage";
    }


}
