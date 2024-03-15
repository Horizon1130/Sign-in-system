package com.myway.platform.controller.v1.mapping.authority;

import com.myway.platform.mysql.authority.entity.SysResources;
import com.myway.platform.mysql.authority.service.ISysResourcesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
@RequestMapping("/authority/resources")
public class ResourcesMapping {

    private static final String path = "authority/resources/";

    @Autowired
    ISysResourcesService sysResourcesServiceImpl;

    @GetMapping("/setting")
    public ModelAndView setting() {
        ModelAndView view = new ModelAndView(String.format("%s%s", path, "setting"));
        view.addObject("libs", path);
        return view;
    }

    @GetMapping("/add")
    public ModelAndView add() {
        ModelAndView view = new ModelAndView(String.format("%s%s", path, "add"));
        return view;
    }

    @GetMapping("/modify")
    public ModelAndView modify(String resourceId) {
        ModelAndView view = new ModelAndView(String.format("%s%s", path, "add"));
        SysResources sysMenu = sysResourcesServiceImpl.getById(resourceId);
        view.addObject("buttonDetail", sysMenu);
        return view;
    }
}
