package com.myway.platform.controller.v1.restful.resources;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myway.platform.api.ReturnResult;
import com.myway.platform.controller.BaseController;
import com.myway.platform.mysql.authority.entity.StudentClass;
import com.myway.platform.mysql.authority.service.IStudentClassService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1/resources/sclass/")
public class ClassController extends BaseController {

    @Autowired
    IStudentClassService studentClassServiceImpl;

    @PostMapping("list")
    public ReturnResult list(long current, long size, StudentClass record) {
        ReturnResult result = new ReturnResult();

        Page<StudentClass> page = new Page<>(current, size);
        QueryWrapper<StudentClass> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<StudentClass> lambda = queryWrapper.lambda();
        if (!StringUtils.isEmpty(record.getTitle())) {
            lambda.like(StudentClass::getTitle, record.getTitle());
        }
        lambda.orderByDesc(StudentClass::getId);

        IPage<StudentClass> records = studentClassServiceImpl.page(page, queryWrapper);

        List<JSONObject> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(records.getRecords())) {
            for (StudentClass courseInfo : records.getRecords()) {
                JSONObject item = new JSONObject();
                item.put("id", courseInfo.getId());
                item.put("title", courseInfo.getTitle());
                item.put("content", courseInfo.getContent());
                list.add(item);
            }
        }

        result.setListView(list, records.getTotal());
        return result;
    }

    @PostMapping("save")
    public ReturnResult save(StudentClass record) {
        ReturnResult result = new ReturnResult();

        studentClassServiceImpl.saveOrUpdate(record);
        result.setMessage("添加完成");
        return result;
    }

    @PostMapping("modify")
    public ReturnResult modify(StudentClass record) {
        ReturnResult result = new ReturnResult();

        studentClassServiceImpl.saveOrUpdate(record);
        result.setMessage("更新完成");
        return result;
    }

    @DeleteMapping("delete")
    public ReturnResult delete(@RequestParam List<Integer> ids) {
        ReturnResult result = new ReturnResult();
        studentClassServiceImpl.removeByIds(ids);
        result.setMessage("删除成功");
        return result;
    }
}
