package nbcc.resto.controller;

import nbcc.resto.dto.MenuItem;
import nbcc.resto.service.MenuItemService;
import nbcc.resto.service.MenuService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static nbcc.common.validation.ModelErrorConverter.addErrorsToBindingResults;

@Controller
@RequestMapping("/menuItem")
@PreAuthorize("isAuthenticated()")
public class MenuItemController {
    private final MenuItemService menuItemService;
    private final MenuService menuService;

    public MenuItemController(MenuItemService menuItemService, MenuService menuService) {
        this.menuItemService = menuItemService;
        this.menuService = menuService;
    }

    @GetMapping("/add/{menuId}")
    public String add(@PathVariable Long menuId, Model model){
        var menuItem = new MenuItem();
        var menu = menuService.get(menuId);

        menuItem.setMenuId(menuId);
        model.addAttribute("menuItem",menuItem);
        model.addAttribute("menu",menu.getValue());
        return "menuItem/create";
    }

    @PostMapping("/add/{menuId}")
    public String add(@PathVariable Long menuId ,
                      @ModelAttribute("menuItem") MenuItem item,
                      BindingResult br,
                      Model model,
                      RedirectAttributes redirectAttributes){
        var menuResult = menuService.get(menuId);

        if(menuResult.isError() || menuResult.isEmpty()){
            model.addAttribute("message",
                    "There was a problem trying to retrieve menu you are trying to add a Item to");
            return "error/errorPage";
        }
        item.setMenuId(menuResult.getValue().getId());
        var menuItemResult = menuItemService.create(item);
        if(menuItemResult.isError()){
            return "error/errorPage";
        }

        if(menuItemResult.isInvalid()){
            addErrorsToBindingResults(br,menuItemResult);
            var menu = menuService.get(menuId);
            model.addAttribute("menu",menu.getValue());
            return "menuItem/create";
        }
        redirectAttributes.addAttribute("id",menuId);
        return "redirect:/menu/edit/{id}";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id,Model model){
        var result = menuItemService.get(id);

        if(result.isError()){
            return "error/errorPage";
        }
        var menu = menuService.get(result.getValue().getMenuId());
        model.addAttribute("menu",menu.getValue());
        model.addAttribute("menuItem",result.getValue());
        return "menuItem/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@ModelAttribute("menuItem") MenuItem menuItem, Model model, BindingResult br, RedirectAttributes redirectAttributes){
        var result = menuItemService.update(menuItem);
        if(result.isError()){
            return "error/errorPage";
        }
        if(result.isInvalid()){
            addErrorsToBindingResults(br,result);
            var menu = menuService.get(result.getValue().getMenuId());
            model.addAttribute("menu",menu.getValue());
            return "menuItem/edit";
        }
        redirectAttributes.addAttribute("id",menuItem.getMenuId());
        return "redirect:/menu/edit/{id}";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id,Model model){
        var result = menuItemService.get(id);

        if(result.isError()){
            return "error/errorPage";
        }
        var menu = menuService.get(result.getValue().getMenuId());
        model.addAttribute("menu",menu.getValue());
        model.addAttribute("menuItem",result.getValue());
        return "menuItem/delete";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id,RedirectAttributes redirectAttributes){
        var menuItem = menuItemService.get(id);
        var result = menuItemService.delete(id);

        if(result.isError()){
            return "error/errorPage";
        }
        redirectAttributes.addAttribute("id",menuItem.getValue().getMenuId());
        return "redirect:/menu/edit/{id}";
    }

}
