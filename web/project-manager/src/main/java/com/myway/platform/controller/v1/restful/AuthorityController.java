package com.myway.platform.controller.v1.restful;

import com.alibaba.fastjson.JSONObject;
import com.myway.platform.api.ReturnResult;
import com.myway.platform.controller.BaseController;
import com.myway.platform.mysql.authority.entity.SysUserInfo;
import com.myway.platform.mysql.authority.service.ISysRoleAuthorityService;
import com.myway.platform.type.ResourceType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/v1/authority/")
public class AuthorityController extends BaseController {

    @Autowired
    ISysRoleAuthorityService sysRoleAuthorityServiceImpl;

    @PostMapping("menus")
    public ReturnResult getAuthorityMenuList() {
        ReturnResult result = new ReturnResult();
        SysUserInfo sessionSysUser = getSessionSysUser();
        String roles = sessionSysUser.getRoles();

        if (!StringUtils.isEmpty(roles)) {
            List<String> roleIdList = new ArrayList<>();
            roleIdList.add(roles);

            List<JSONObject> resourcesByRoles = sysRoleAuthorityServiceImpl.getResourceByRoles(roleIdList);

            List<JSONObject> list = new ArrayList<>();

            resourcesByRoles.stream().filter(item -> ResourceType.CATALOG.toString().equals(item.getString("classify"))).collect(Collectors.toList()).forEach(item -> {
                list.add(item);
            });

            result.set("menus", list);
        }
        return result;
    }

    @PostMapping("{resourceId}/buttons")
    public ReturnResult getAuthorityButtonList(@PathVariable String resourceId) {
        ReturnResult result = new ReturnResult();
        List<JSONObject> buttons = sysRoleAuthorityServiceImpl.getResourceButtonsByResourceIdAndRoleIds(resourceId, getSessionSysUserRoles());
        result.set("buttons", buttons);
        return result;
    }
}
