package com.myway.platform.controller.v1.mapping.settings;

import com.myway.platform.mysql.authority.entity.SysRoleInfo;
import com.myway.platform.mysql.authority.service.ISysRoleInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
@RequestMapping("/settings/role")
public class RoleMapping {

    private static final String path = "settings/role/";

    @Autowired
    ISysRoleInfoService sysRoleInfoServiceImpl;

    @GetMapping("/list")
    public ModelAndView list() {
        ModelAndView view = new ModelAndView(String.format("%s%s", path, "list"));
        view.addObject("libs", path);
        return view;
    }

    @GetMapping("/add")
    public ModelAndView add() {
        ModelAndView view = new ModelAndView(String.format("%s%s", path, "add"));
        return view;
    }

    @GetMapping("/modify")
    public ModelAndView modify(String roleId) {
        ModelAndView view = new ModelAndView(String.format("%s%s", path, "add"));
        SysRoleInfo role = sysRoleInfoServiceImpl.getById(roleId);
        view.addObject("role", role);
        return view;
    }
}
