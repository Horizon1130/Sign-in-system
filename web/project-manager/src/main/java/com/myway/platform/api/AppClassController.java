package com.myway.platform.api;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myway.platform.controller.BaseController;
import com.myway.platform.mysql.authority.entity.StudentClass;
import com.myway.platform.mysql.authority.service.IStudentClassService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/class/")
public class AppClassController extends BaseController {

    @Autowired
    IStudentClassService studentClassServiceImpl;

    @PostMapping("list")
    public ReturnResult list() {
        ReturnResult result = new ReturnResult();

        List<StudentClass> records = studentClassServiceImpl.list();

        result.set("list", records);
        return result;
    }
}
