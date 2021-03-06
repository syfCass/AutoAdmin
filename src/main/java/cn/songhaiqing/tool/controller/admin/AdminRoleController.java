package cn.songhaiqing.tool.controller.admin;

import cn.songhaiqing.tool.base.BaseQuery;
import cn.songhaiqing.tool.base.BaseResponse;
import cn.songhaiqing.tool.constants.AdminErrorMsg;
import cn.songhaiqing.tool.base.BaseController;
import cn.songhaiqing.tool.model.MenuViewModel;
import cn.songhaiqing.tool.model.SysRoleViewModel;
import cn.songhaiqing.tool.service.MenuService;
import cn.songhaiqing.tool.service.SysRoleService;
import cn.songhaiqing.tool.base.BaseResponseList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RestController
@RequestMapping("/admin/role")
public class AdminRoleController extends BaseController {

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private MenuService menuService;

    @RequestMapping(value = "/roleView")
    public ModelAndView roleView() {
        return new ModelAndView("/admin/role");
    }

    @RequestMapping(value = "/getRolePage")
    public BaseResponseList getRolePage(BaseQuery query) {
        return sysRoleService.getSysRole(query);
    }

    @RequestMapping(value = "/addRoleView")
    public ModelAndView addRoleView() {
        ModelAndView modelAndView = new ModelAndView("/admin/roleAdd");
        List<MenuViewModel> menuViewModels = menuService.getAllMenuByRole(null);
        modelAndView.addObject("menuViewModels", menuViewModels);
        return modelAndView;
    }

    @RequestMapping(value = "/editRoleView")
    public ModelAndView editRoleView(HttpServletRequest request, @RequestParam Long id) {
        SysRoleViewModel sysRole = sysRoleService.getSysRoleDetail(id);
        ModelAndView modelAndView = new ModelAndView("/admin/roleEdit");
        List<MenuViewModel> menuViewModels = menuService.getAllMenuByRole(id);
        modelAndView.addObject("sysRole", sysRole);
        modelAndView.addObject("menuViewModels", menuViewModels);
        return modelAndView;
    }

    @RequestMapping(value = "/addRole", method = RequestMethod.POST)
    public BaseResponse addSysRole(SysRoleViewModel model, @RequestParam Long[] menuIds) {
        if (menuIds == null || menuIds.length == 0) {
            return fail(AdminErrorMsg.PLEASE_CHOOSE_MENU);
        }
        sysRoleService.insert(model, menuIds);
        return success();
    }

    @RequestMapping(value = "/editRole", method = RequestMethod.POST)
    public BaseResponse editSysRole(SysRoleViewModel model, @RequestParam Long[] menuIds) {
        if (menuIds == null || menuIds.length == 0) {
            return fail(AdminErrorMsg.PLEASE_CHOOSE_MENU);
        }
        sysRoleService.update(model, menuIds);
        return success();
    }
}
