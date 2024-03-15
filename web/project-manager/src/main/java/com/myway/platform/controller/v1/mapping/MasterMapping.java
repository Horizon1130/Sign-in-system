package com.myway.platform.controller.v1.mapping;

import com.myway.platform.controller.BaseController;
import com.myway.platform.mysql.authority.entity.SysUserInfo;
import com.myway.platform.mysql.authority.service.ISysUserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
public class MasterMapping extends BaseController {

    @Autowired
    ISysUserInfoService sysUserInfoServiceImpl;

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("title", systemTitle);
        model.addAttribute("copyright", systemCopyRight);
        return "login";
    }

    @GetMapping("/master")
    public ModelAndView master() {
        ModelAndView view = new ModelAndView("master");

        view.addObject("title", systemTitle);
        view.addObject("copyright", systemCopyRight);
        return view;
    }

    @GetMapping("/modify/password")
    public ModelAndView toModifyPassword() {
        ModelAndView view = new ModelAndView("personal/modify_password");
        SysUserInfo sessionSysUser = getSessionSysUser();
        view.addObject("account", sessionSysUser.getAccount());
        return view;
    }

    @GetMapping("/modify/user")
    public ModelAndView toModifyUser() {
        ModelAndView view = new ModelAndView("personal/modify_user");
        return view;
    }
}
