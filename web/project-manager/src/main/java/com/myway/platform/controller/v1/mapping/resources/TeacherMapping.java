package com.myway.platform.controller.v1.mapping.resources;

import com.myway.platform.controller.BaseController;
import com.myway.platform.mysql.authority.entity.SysUserBasicInfo;
import com.myway.platform.mysql.authority.entity.SysUserInfo;
import com.myway.platform.mysql.authority.service.ISysUserBasicInfoService;
import com.myway.platform.mysql.authority.service.ISysUserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
@RequestMapping("/resources/teacher")
public class TeacherMapping extends BaseController {

    private static final String path = "resources/teacher/";

    @Autowired
    ISysUserInfoService sysUserInfoServiceImpl;
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
    public ModelAndView modify(String id) {
        ModelAndView view = new ModelAndView(String.format("%s%s", path, "modify"));
        SysUserInfo sysUserInfo = sysUserInfoServiceImpl.getById(id);

        SysUserBasicInfo sysUserBasicInfo = sysUserBasicInfoServiceImpl.getById(id);
        view.addObject("sysUser", sysUserInfo);
        view.addObject("sysUserBasic", sysUserBasicInfo);
        return view;
    }
}
