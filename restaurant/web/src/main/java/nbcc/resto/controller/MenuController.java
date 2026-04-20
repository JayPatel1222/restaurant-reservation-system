package nbcc.resto.controller;

import nbcc.resto.dto.Menu;
import nbcc.resto.dto.MenuItem;
import nbcc.resto.service.MenuItemService;
import nbcc.resto.service.MenuService;
import nbcc.resto.viewmodel.MenuListViewModel;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

import static nbcc.common.validation.ModelErrorConverter.addErrorsToBindingResults;

@Controller
@PreAuthorize("isAuthenticated()")
@RequestMapping("/menu")
public class MenuController {

    private final MenuService menuService;
    private final MenuItemService menuItemService;

    public MenuController(MenuService menuService, MenuItemService menuItemService) {
        this.menuService = menuService;
        this.menuItemService = menuItemService;
    }

    @GetMapping
    public String getAll(Model model){
        var results = menuService.getAll();

        if(results.isError()){
            return "error/errorPage";
        }
        var viewModel = new MenuListViewModel(results.getValue());
        model.addAttribute("viewModel",viewModel);
        return "menu/list";
    }

    @GetMapping("/create")
    public String create(Model model){
        model.addAttribute("menu",new Menu());
        return "menu/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("menu") Menu menu, BindingResult br){
        var result = menuService.create(menu);

        if(result.isError()){
            return "error/errorPage";
        }

        if(result.isInvalid()){
            addErrorsToBindingResults(br,result);
            return "menu/create";
        }
        return "redirect:/menu";
    }

    @GetMapping("/detail/{id}")
    public String details(@PathVariable Long id,Model model){
        var result = menuService.get(id);

        if(result.isError()){
            return "error/errorPage";
        }

        model.addAttribute("menu",result.getValue());
        return "menu/details";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id,Model model){
        var result = menuService.get(id);

        if(result.isError()){
            return "error/errorPage";
        }

        if(result.getValue().getItems() != null){
            model.addAttribute("menuItems",result.getValue().getItems());
        }
        model.addAttribute("menu",result.getValue());
        return "menu/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@ModelAttribute("menu") Menu menu, BindingResult br,Model model){
        var result = menuService.update(menu);

        if(result.isError()){
            return "error/errorPage";
        }

        if(result.isInvalid()){
            var menuItems = menuItemService.getAll(menu.getId());
            if(menuItems != null){
                model.addAttribute("menuItems",menuItems.getValue());
            }
            addErrorsToBindingResults(br,result);
            return "menu/edit";
        }
        return "redirect:/menu";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id,Model model){
        var result = menuService.get(id);

        if(result.isError()){
            return "error/errorPage";
        }
        model.addAttribute("menu",result.getValue());
        return "menu/delete";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        var result = menuService.delete(id);

        if(result.isError()){
            return "error/errorPage";
        }
        return "redirect:/menu";
    }

    @GetMapping("/search")
    public String search(@RequestParam String name, Model model) {
        var searchedResults = menuService.search(name);

        Collection<Menu> menus = searchedResults.getValue();
        if (menus.isEmpty()) {
            var viewModel = new MenuListViewModel(searchedResults.getValue());
            viewModel.setNameToSearch(name);
            viewModel.setShowResultNotFound(true);
            model.addAttribute("viewModel", viewModel);
            return "menu/list";
        }
        var viewModel = new MenuListViewModel(searchedResults.getValue());
        viewModel.setNameToSearch(name);
        model.addAttribute("viewModel", viewModel);
        return "menu/list";
    }

}
