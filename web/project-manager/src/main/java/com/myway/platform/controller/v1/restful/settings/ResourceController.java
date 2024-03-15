package com.myway.platform.controller.v1.restful.settings;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.myway.platform.api.ReturnResult;
import com.myway.platform.mysql.authority.entity.SysResources;
import com.myway.platform.mysql.authority.service.ISysResourcesService;
import com.myway.platform.type.ResourceType;
import com.myway.platform.utils.Datetime;
import com.myway.platform.utils.UUIDUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/v1/settings/resource")
public class ResourceController {

    @Autowired
    ISysResourcesService sysResourcesServiceImpl;

    @PostMapping("/list")
    public ReturnResult list() {
        ReturnResult result = new ReturnResult();
        List<JSONObject> resources = sysResourcesServiceImpl.getResourcesAll();
        result.setData(resources);
        return result;
    }

    @PostMapping("/save")
    public ReturnResult save(SysResources record) {
        ReturnResult result = new ReturnResult();

        Datetime datetime = new Datetime();
        String resourceId = UUIDUtils.getEncryUUID();
        record.setResourceId(resourceId);
        record.setCreateTime(datetime.getFullDatetime());
        record.setLastUpdateTime(datetime.getFullDatetime());

        sysResourcesServiceImpl.saveOrUpdate(record);
        result.setMessage("新增成功");
        return result;
    }

    @PostMapping("/modify")
    public ReturnResult modify(SysResources record) {
        ReturnResult result = new ReturnResult();

        Datetime datetime = new Datetime();
        record.setLastUpdateTime(datetime.getFullDatetime());
        sysResourcesServiceImpl.saveOrUpdate(record);
        result.setMessage("更新成功");
        return result;
    }

    @DeleteMapping("/delete")
    public ReturnResult delete(String ids) {
        ReturnResult result = new ReturnResult();
        List<String> resourceIds = JSON.parseArray(ids, String.class);
        sysResourcesServiceImpl.removeByIds(resourceIds);
        result.setMessage("删除成功");
        return result;
    }

    @PostMapping("/catalog/list")
    public ReturnResult catalogList() {
        ReturnResult result = new ReturnResult();

        QueryWrapper<SysResources> query = new QueryWrapper<>();
        query.lambda().select(SysResources::getTitle,SysResources::getResourceId).eq(SysResources::getClassify, ResourceType.CATALOG.toString());
        List<Map<String, Object>> list = sysResourcesServiceImpl.listMaps(query);

        result.set("list", list);
        return result;
    }

    @PostMapping("/menu/list")
    public ReturnResult menuList() {
        ReturnResult result = new ReturnResult();

        QueryWrapper<SysResources> query = new QueryWrapper<>();
        query.lambda().select(SysResources::getTitle,SysResources::getResourceId).eq(SysResources::getClassify, ResourceType.MENU.toString());
        List<Map<String, Object>> list = sysResourcesServiceImpl.listMaps(query);

        result.set("list", list);
        return result;
    }
}
