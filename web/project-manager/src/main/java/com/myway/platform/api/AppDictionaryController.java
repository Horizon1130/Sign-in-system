package com.myway.platform.api;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.myway.platform.controller.BaseController;
import com.myway.platform.mysql.authority.entity.DictionaryInfo;
import com.myway.platform.mysql.authority.service.IDictionaryInfoService;
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
@RequestMapping("/api/dictionary/")
public class AppDictionaryController extends BaseController {

    @Autowired
    IDictionaryInfoService dictionaryInfoServiceImpl;

    @PostMapping("list")
    public ReturnResult list(String classify, Integer level, String parentId) {
        ReturnResult result = new ReturnResult();

        QueryWrapper<DictionaryInfo> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<DictionaryInfo> lambda = queryWrapper.lambda();

        if (!StringUtils.isEmpty(classify)) {
            lambda.eq(DictionaryInfo::getClassify, classify);
        }
        if (!StringUtils.isEmpty(level)) {
            lambda.eq(DictionaryInfo::getLevel, level);
        }
        if (!StringUtils.isEmpty(parentId)) {
            lambda.eq(DictionaryInfo::getParentId, parentId);
        }

        lambda.eq(DictionaryInfo::getStatus, 1).orderByDesc(DictionaryInfo::getCreateTime);

        List<DictionaryInfo> records = dictionaryInfoServiceImpl.list(queryWrapper);

        List<JSONObject> list = new ArrayList<>();

        if (!CollectionUtils.isEmpty(records)) {
            for (DictionaryInfo dictionaryInfo : records) {
                JSONObject item = new JSONObject();
                item.put("classifyId", dictionaryInfo.getDictionaryId());
                item.put("title", dictionaryInfo.getTitle());
                item.put("icon", doMain + dictionaryInfo.getIcon());
                list.add(item);
            }
        }
        result.set("list", list);
        return result;
    }
}
