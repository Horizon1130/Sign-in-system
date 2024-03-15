package com.myway.platform.controller.v1.mapping.resources;

import com.myway.platform.controller.BaseController;
import com.myway.platform.mysql.authority.entity.NewsInfo;
import com.myway.platform.mysql.authority.service.INewsInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
@RequestMapping("/resources/news")
public class NewsMapping extends BaseController {

    private static final String path = "resources/news/";

    @Autowired
    INewsInfoService newsInfoServiceImpl;

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
    public ModelAndView modify(String newsId) {
        ModelAndView view = new ModelAndView(String.format("%s%s", path, "modify"));
        NewsInfo news = newsInfoServiceImpl.getById(newsId);
        view.addObject("news", news);
        return view;
    }
}
