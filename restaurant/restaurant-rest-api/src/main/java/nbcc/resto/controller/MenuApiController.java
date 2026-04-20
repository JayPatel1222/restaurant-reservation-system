package nbcc.resto.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import nbcc.common.result.Result;
import nbcc.common.result.ValidatedResult;
import nbcc.resto.dto.Menu;
import nbcc.resto.service.MenuService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

import static nbcc.resto.controller.api.result.ResultHandler.handleResult;

@Tag(name = "Menu API", description = "Menu Related Information")
@RestController
@RequestMapping("/api/menu")
public class MenuApiController {

    public final MenuService menuService;

    public MenuApiController(MenuService menuService) {
        this.menuService = menuService;
    }

    @Operation(summary = "Get all Menus")
    @GetMapping
    public ResponseEntity<Result<Collection<Menu>>> getAll(){

        var menus = menuService.getAll();
        return handleResult(menus, HttpStatus.OK);
    }

    @Operation(summary = "Get Menu By Id")
    @GetMapping("/{id}")
    public ResponseEntity<ValidatedResult<Menu>> get(@PathVariable Long id){

        var menu = menuService.get(id);
        return handleResult(menu,HttpStatus.OK,HttpStatus.NOT_FOUND);
    }
}
