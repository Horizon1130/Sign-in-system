package com.myway.platform.controller.v1.mapping.resources;

import com.myway.platform.controller.BaseController;
import com.myway.platform.mysql.authority.entity.DictionaryInfo;
import com.myway.platform.mysql.authority.service.IDictionaryInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
@RequestMapping("/resources/dictionary")
public class DictionaryMapping extends BaseController {

    private static final String path = "resources/dictionary/";

    @Autowired
    IDictionaryInfoService dictionaryInfoServiceImpl;

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
    public ModelAndView modify(String dictionaryId) {
        ModelAndView view = new ModelAndView(String.format("%s%s", path, "modify"));
        DictionaryInfo dictionary = dictionaryInfoServiceImpl.getById(dictionaryId);
        view.addObject("dictionary", dictionary);
        return view;
    }
}
