package com.myway.platform.controller.v1.restful.resources;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myway.platform.api.ReturnResult;
import com.myway.platform.controller.BaseController;
import com.myway.platform.mysql.authority.entity.DictionaryInfo;
import com.myway.platform.mysql.authority.service.IDictionaryInfoService;
import com.myway.platform.utils.Datetime;
import com.myway.platform.utils.UUIDUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/v1/resources/dictionary/")
public class DictionaryController extends BaseController {

    @Autowired
    IDictionaryInfoService dictionaryInfoServiceImpl;

    @PostMapping("list")
    public ReturnResult list(long current, long size, DictionaryInfo record) {
        ReturnResult result = new ReturnResult();
        IPage page = new Page(current, size);

        IPage records = dictionaryInfoServiceImpl.getDictionaryListByPage(page, record);
        result.setListView(records.getRecords(), records.getTotal());
        return result;
    }

    @PostMapping("save")
    public ReturnResult save(DictionaryInfo record) {
        ReturnResult result = new ReturnResult();
        Datetime datetime = new Datetime();
        String dictionaryId = UUIDUtils.getEncryUUID();
        record.setCreateDate(datetime.getDate());
        record.setCreateTime(datetime.getFullDatetime());
        record.setDictionaryId(dictionaryId);
        dictionaryInfoServiceImpl.saveOrUpdateDictionary(record);
        result.setMessage("添加完成");
        return result;
    }

    @PostMapping("modify")
    public ReturnResult modify(DictionaryInfo record) {
        ReturnResult result = new ReturnResult();
        Datetime datetime = new Datetime();
        record.setCreateDate(datetime.getDate());
        record.setCreateTime(datetime.getFullDatetime());
        dictionaryInfoServiceImpl.saveOrUpdateDictionary(record);
        result.setMessage("更新完成");
        return result;
    }

    @DeleteMapping("delete")
    public ReturnResult delete(@RequestParam List<String> ids) {
        ReturnResult result = new ReturnResult();
        dictionaryInfoServiceImpl.removeByIds(ids);
        result.setMessage("删除成功");
        return result;
    }

    @PostMapping("tree")
    public ReturnResult tree() {
        ReturnResult result = new ReturnResult();
        QueryWrapper<DictionaryInfo> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<DictionaryInfo> lambda = queryWrapper.lambda();

        lambda.orderByAsc(DictionaryInfo::getClassify, DictionaryInfo::getLevel);
        List<DictionaryInfo> list = dictionaryInfoServiceImpl.list(queryWrapper);

        List<JSONObject> data = new ArrayList<>();

        list.stream().filter(item -> item.getLevel().equals(1))
                .collect(Collectors.toList()).forEach(item -> {
            JSONObject obj = new JSONObject();
            obj.put("value", item.getDictionaryId());
            obj.put("name", item.getTitle());
            List<JSONObject> children = ergodic(list, item.getDictionaryId());
            obj.put("children", children);
            data.add(obj);
        });
        result.set("list", data);
        return result;
    }

    @PostMapping("matching")
    public ReturnResult matching(String classify) {
        ReturnResult result = new ReturnResult();

        List<Map<String, Object>> list = dictionaryInfoServiceImpl.getDictionaryByClassify(classify);

        List<JSONObject> data = new ArrayList<>();

        list.stream().filter(item -> item.get("level").equals(1))
                .collect(Collectors.toList()).forEach(item -> {
            JSONObject obj = new JSONObject();
            obj.put("value", item.get("dictionary_id"));
            obj.put("name", item.get("title"));
            List<JSONObject> children = ergodicMap(list, item.get("dictionary_id").toString());
            obj.put("children", children);
            data.add(obj);
        });
        result.set("list", data);
        return result;
    }

    @PostMapping("all")
    public ReturnResult all(String classify) {
        ReturnResult result = new ReturnResult();
        List<Map<String, Object>> list = dictionaryInfoServiceImpl.getDictionaryByClassify(classify);
        result.set("list", list);
        return result;
    }

    protected List<JSONObject> ergodic(List<DictionaryInfo> list, String parentId) {
        List<JSONObject> result = new ArrayList<>();
        list.stream().filter(item -> !StringUtils.isEmpty(parentId) && parentId.equals(item.getParentId()))
                .collect(Collectors.toList()).forEach(item -> {
            JSONObject obj = new JSONObject();
            obj.put("value", item.getDictionaryId());
            obj.put("name", item.getTitle());
            List<JSONObject> children = ergodic(list, item.getDictionaryId());
            obj.put("children", children);
            result.add(obj);
        });
        return result;
    }

    protected List<JSONObject> ergodicMap(List<Map<String, Object>> list, String parentId) {
        List<JSONObject> result = new ArrayList<>();
        list.stream().filter(item -> !StringUtils.isEmpty(parentId) && !StringUtils.isEmpty(item.get("parent_id")) && parentId.equals(item.get("parent_id")))
                .collect(Collectors.toList()).forEach(item -> {
            JSONObject obj = new JSONObject();
            obj.put("value", item.get("dictionary_id"));
            obj.put("name", item.get("title"));
            List<JSONObject> children = ergodicMap(list, item.get("dictionary_id").toString());
            obj.put("children", children);
            result.add(obj);
        });
        return result;
    }
}
