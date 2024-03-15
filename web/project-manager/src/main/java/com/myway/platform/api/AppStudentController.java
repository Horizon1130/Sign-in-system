package com.myway.platform.api;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myway.platform.controller.BaseController;
import com.myway.platform.mysql.authority.entity.SysUserBasicInfo;
import com.myway.platform.mysql.authority.service.ISysUserBasicInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/student/")
public class AppStudentController extends BaseController {

    @Autowired
    ISysUserBasicInfoService sysUserBasicInfoServiceImpl;

    @PostMapping("list")
    public ReturnResult list(Integer page) {
        ReturnResult result = new ReturnResult();

        Page<SysUserBasicInfo> iPage = new Page<>(page, 20);

        QueryWrapper<SysUserBasicInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysUserBasicInfo::getType, 0).orderByDesc(SysUserBasicInfo::getCreateTime);

        IPage<SysUserBasicInfo> records = sysUserBasicInfoServiceImpl.page(iPage, queryWrapper);

        List<JSONObject> list = new ArrayList<>();

        if (!CollectionUtils.isEmpty(records.getRecords())) {
            for (SysUserBasicInfo sysUserBasicInfo : records.getRecords()) {
                JSONObject item = new JSONObject();
                item.put("sysUserId", sysUserBasicInfo.getSysUserId());
                item.put("nice", sysUserBasicInfo.getNice());
                item.put("face", doMain + sysUserBasicInfo.getFace());
                list.add(item);
            }
        }
        result.set("list", list);
        return result;
    }

}