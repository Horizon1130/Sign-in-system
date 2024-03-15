package com.myway.platform.controller.v1.mapping.resources;

import com.myway.platform.controller.BaseController;
import com.myway.platform.mysql.authority.entity.StudentClass;
import com.myway.platform.mysql.authority.service.IStudentClassService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
@RequestMapping("/resources/sclass")
public class ClassMapping extends BaseController {

    private static final String path = "resources/sclass/";

    @Autowired
    IStudentClassService studentClassServiceImpl;

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
    public ModelAndView modify(Integer id) {
        ModelAndView view = new ModelAndView(String.format("%s%s", path, "modify"));
        StudentClass record = studentClassServiceImpl.getById(id);
        view.addObject("record", record);
        return view;
    }
}
