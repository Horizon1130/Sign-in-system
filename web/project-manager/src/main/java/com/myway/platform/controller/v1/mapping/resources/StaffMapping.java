package com.myway.platform.controller.v1.mapping.resources;

import com.myway.platform.mysql.authority.entity.SysUserBasicInfo;
import com.myway.platform.mysql.authority.service.ISysUserBasicInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
@RequestMapping("/resources/staff")
public class StaffMapping {

    private static final String path = "resources/staff/";

    @Autowired
    ISysUserBasicInfoService sysUserBasicInfoServiceImpl;

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
    public ModelAndView modify(String userId) {
        ModelAndView view = new ModelAndView(String.format("%s%s", path, "modify"));

        SysUserBasicInfo sysUserBasicInfo = sysUserBasicInfoServiceImpl.getById(userId);
        view.addObject("record", sysUserBasicInfo);
        return view;
    }
}
