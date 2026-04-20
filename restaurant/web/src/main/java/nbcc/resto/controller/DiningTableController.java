package nbcc.resto.controller;

import jakarta.servlet.http.HttpServletRequest;
import nbcc.resto.dto.DiningTable;
import nbcc.resto.service.DiningTableService;
import nbcc.resto.viewmodel.DiningTableListViewModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import static nbcc.common.validation.ModelErrorConverter.addErrorsToBindingResults;

@Controller
@PreAuthorize("isAuthenticated()")
@RequestMapping({"/diningTable"})
public class DiningTableController {

    private final DiningTableService diningTableService;

    private final Logger logger = LoggerFactory.getLogger(DiningTableController.class);

    public DiningTableController(DiningTableService diningTableService) {
        this.diningTableService = diningTableService;
    }

    @GetMapping
    public String getAll(Model model) {
        var result = diningTableService.getAll();

        if (result.isError()){
            model.addAttribute("message", "Error retrieving tables");
            return "error/errorPage";
        }

        DiningTableListViewModel viewModel = new DiningTableListViewModel(result.getValue());
        model.addAttribute("viewModel", viewModel);
        return "diningtable/list";

    }


    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("diningTable", new DiningTable());
        return "diningtable/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("diningTable") DiningTable diningTable, BindingResult br) {

        var result = diningTableService.create(diningTable);

        if (result.isError()) {
            return "error/errorPage";
        }

        if (result.isInvalid()) {
            addErrorsToBindingResults(br, result);
            return "diningtable/create";
        }
        return "redirect:/diningTable";
    }

    @GetMapping("/edit/{tableId}")
    public String edit(@PathVariable Long tableId, Model model) {
        var result = diningTableService.get(tableId);

        if (result.isError()) {
            return "error/errorPage";
        }

        if (result.isEmpty()) {
            model.addAttribute("message", "The table you are trying to edit was not found");
            return "error/errorPage";
        }

        model.addAttribute("diningTable", result.getValue());
        return "diningtable/edit";
    }

    @PostMapping("/edit/{tableId}")
    public String edit(@ModelAttribute("diningTable") DiningTable diningTable, BindingResult br, Model model) {

        var result = diningTableService.update(diningTable);

        if (result.isError()) {
            return "error/errorPage";
        }

        if (result.isInvalid()) {
            addErrorsToBindingResults(br, result);
            model.addAttribute("diningTable", diningTable);
            return "diningtable/edit";
        }

        return "redirect:/diningTable";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable long id, Model model) {
        var result = diningTableService.get(id);

        if (result.isError()) {
            return "error/errorPage";
        }

        if (result.isEmpty()) {
            model.addAttribute("message", "The table you are trying to delete was not found");
            return "error/errorPage";
        }

        model.addAttribute("diningTable", result.getValue());
        return "diningtable/delete";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable long id) {
        var result  = diningTableService.delete(id);

        if (result.isError()) {
            return "error/errorPage";
        }

        return "redirect:/diningTable";
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String exceptionHandler(Model model, Exception ex, HttpServletRequest request) {
        logger.error("Unexpected Exception on uri {}: on method {} ", request.getRequestURI(), request.getMethod(), ex);
        model.addAttribute("message", "Unexpected Error Occurred");
        return "error/errorPage";
    }
}
