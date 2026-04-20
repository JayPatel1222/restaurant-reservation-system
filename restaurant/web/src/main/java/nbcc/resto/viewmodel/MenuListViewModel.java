package nbcc.resto.viewmodel;

import nbcc.resto.dto.Menu;

import java.util.Collection;

public class MenuListViewModel {
    private final Collection<Menu> menus ;
    private boolean showResultNotFound;
    private String nameToSearch;
    public MenuListViewModel(Collection<Menu> menus) {
        this.menus = menus;

    }

    public boolean isShowResultNotFound() {
        return showResultNotFound;
    }

    public MenuListViewModel setShowResultNotFound(boolean showResultNotFound) {
        this.showResultNotFound = showResultNotFound;
        return this;
    }

    public Collection<Menu> getMenus() {
        return menus;
    }

    public boolean isShowNoMenusFound() {
        return menus.isEmpty() && !showResultNotFound;
    }

    public String getNameToSearch() {
        return nameToSearch;
    }

    public MenuListViewModel setNameToSearch(String nameToSearch) {
        this.nameToSearch = nameToSearch;
        return this;
    }
}
