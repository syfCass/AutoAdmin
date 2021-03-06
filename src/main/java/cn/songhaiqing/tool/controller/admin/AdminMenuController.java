package cn.songhaiqing.tool.controller.admin;

import cn.songhaiqing.tool.base.BaseController;
import cn.songhaiqing.tool.base.BaseQuery;
import cn.songhaiqing.tool.base.BaseResponse;
import cn.songhaiqing.tool.base.BaseResponseList;
import cn.songhaiqing.tool.entity.Menu;
import cn.songhaiqing.tool.model.MenuViewModel;
import cn.songhaiqing.tool.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import java.util.List;


@RestController
@RequestMapping("/admin/menu")
public class AdminMenuController extends BaseController {

    @Autowired
    private MenuService menuService;

    @RequestMapping(value = "/menuView")
    public ModelAndView menuView() {
        return new ModelAndView("/admin/menu");
    }

    @RequestMapping(value = "/addMenuView")
    public ModelAndView addMenuView() {
        List<MenuViewModel> menus = menuService.getParentMenu();
        ModelAndView modelAndView = new ModelAndView("/admin/menuAdd");
        modelAndView.addObject("menus", menus);
        return modelAndView;
    }

    @RequestMapping(value = "/editMenuView")
    public ModelAndView editMenuView(@RequestParam Long id) {
        Menu menu = menuService.getMenu(id);
        List<MenuViewModel> menus = menuService.getParentMenu();
        ModelAndView model = new ModelAndView("/admin/menuEdit");
        model.addObject("menu", menu);
        model.addObject("menus", menus);
        return model;
    }

    @RequestMapping(value = "/getMenuPage")
    public BaseResponseList getMenu(BaseQuery query) {
        return menuService.getMenuPage(query);
    }

    @RequestMapping(value = "/addMenu", method = RequestMethod.POST)
    public BaseResponse addMenu(MenuViewModel model) {
        menuService.addMenu(model);
        return success();
    }

    @RequestMapping(value = "/editMenu", method = RequestMethod.POST)
    public BaseResponse editMenu(MenuViewModel model) {
        menuService.editMenu(model);
        return success();
    }

    @RequestMapping(value = "/deleteMenu", method = RequestMethod.POST)
    public BaseResponse deleteMenu(@RequestParam Long[] ids) {
        menuService.deleteMenu(ids);
        return success();
    }
}
