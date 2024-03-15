package com.myway.platform.controller.v1.restful.settings;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myway.platform.api.ReturnResult;
import com.myway.platform.mysql.authority.entity.SysRoleInfo;
import com.myway.platform.mysql.authority.service.ISysRoleInfoService;
import com.myway.platform.utils.UUIDUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1/settings/role/")
public class RoleController {

    @Autowired
    ISysRoleInfoService sysRoleInfoServiceImpl;

    @PostMapping("list")
    public ReturnResult list(long current, long size, SysRoleInfo record) {
        ReturnResult result = new ReturnResult();
        Page<SysRoleInfo> pageRecord = new Page<>(current, size);

        QueryWrapper<SysRoleInfo> query = new QueryWrapper<>();
        LambdaQueryWrapper<SysRoleInfo> lambda = query.lambda();
        if (!StringUtils.isEmpty(record.getCode())) {
            lambda.eq(SysRoleInfo::getCode, record.getCode());
        }
        if (!StringUtils.isEmpty(record.getTitle())) {
            lambda.like(SysRoleInfo::getTitle, record.getTitle());
        }
        if (!StringUtils.isEmpty(record.getStatus())) {
            lambda.eq(SysRoleInfo::getStatus, record.getStatus());
        }

        pageRecord.setDesc("title");
        IPage<SysRoleInfo> page = sysRoleInfoServiceImpl.page(pageRecord, query);

        result.setListView(page.getRecords(), page.getTotal());
        return result;
    }

    @PostMapping("save")
    public ReturnResult save(SysRoleInfo record) {
        ReturnResult result = new ReturnResult();
        String roleId = UUIDUtils.getEncryUUID();
        record.setRoleId(roleId);
        sysRoleInfoServiceImpl.saveOrUpdate(record);
        result.setMessage("新增成功");
        return result;
    }

    @PostMapping("modify")
    public ReturnResult modify(SysRoleInfo record) {
        ReturnResult result = new ReturnResult();
        sysRoleInfoServiceImpl.saveOrUpdate(record);
        result.setMessage("更新成功");
        return result;
    }

    @DeleteMapping("delete")
    public ReturnResult delete(String ids) {
        ReturnResult result = new ReturnResult();
        List<String> resourceIds = JSON.parseArray(ids, String.class);
        sysRoleInfoServiceImpl.removeByIds(resourceIds);
        result.setMessage("删除成功");
        return result;
    }

    @PostMapping("all")
    public ReturnResult list() {
        ReturnResult result = new ReturnResult();

        QueryWrapper<SysRoleInfo> query = new QueryWrapper<>();
        query.lambda().eq(SysRoleInfo::getStatus, 1);

        List<SysRoleInfo> list = sysRoleInfoServiceImpl.list(query);
        result.setData(list);
        return result;
    }
}
