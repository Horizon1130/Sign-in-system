package com.myway.platform.controller.v1.restful.authority;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.myway.platform.api.ReturnResult;
import com.myway.platform.controller.BaseController;
import com.myway.platform.mysql.authority.entity.SysResources;
import com.myway.platform.mysql.authority.entity.SysRoleAuthority;
import com.myway.platform.mysql.authority.entity.SysUserInfo;
import com.myway.platform.mysql.authority.service.ISysResourcesService;
import com.myway.platform.mysql.authority.service.ISysRoleAuthorityService;
import com.myway.platform.type.ResourceType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/v1/authority/resources/")
public class ResourcesSettings extends BaseController {

    @Autowired
    ISysResourcesService sysResourcesServiceImpl;
    @Autowired
    ISysRoleAuthorityService sysRoleAuthorityServiceImpl;

    @PostMapping("list")
    public ReturnResult getResources() {
        ReturnResult result = new ReturnResult();
        SysUserInfo sessionSysUser = getSessionSysUser();

        QueryWrapper<SysResources> query = new QueryWrapper<>();
        query.lambda().eq(SysResources::getStatus, 1);
        List<SysResources> list = sysResourcesServiceImpl.list(query);

        List<JSONObject> resources = new ArrayList<>();

        list.stream().sorted((x,y) ->{
            return x.getSort().compareTo(y.getSort());
        }).filter(new Predicate<SysResources>() {
            @Override
            public boolean test(SysResources resources) {
                return resources.getClassify().equals(ResourceType.CATALOG.toString());
            }
        }).collect(Collectors.toList()).forEach(catalog -> {

            JSONObject itemCatalog = new JSONObject();
            itemCatalog.put("title", catalog.getTitle());
            itemCatalog.put("resourceId", catalog.getResourceId());

            List<JSONObject> menuArray = new ArrayList<>();

            list.stream().sorted((x,y) ->{
                return x.getSort().compareTo(y.getSort());
            }).filter(new Predicate<SysResources>() {
                @Override
                public boolean test(SysResources resource) {
                    return resource.getClassify().equals(ResourceType.MENU.toString()) && !StringUtils.isEmpty(resource.getParentId()) && catalog.getResourceId().equals(resource.getParentId());
                }
            }).collect(Collectors.toList()).forEach(menu -> {           //菜单

                JSONObject itemMenu = new JSONObject();
                itemMenu.put("title", menu.getTitle());
                itemMenu.put("resourceId", menu.getResourceId());

                List<JSONObject> buttonArray = new ArrayList<>();

                list.stream().sorted((x,y) ->{
                    return x.getSort().compareTo(y.getSort());
                }).filter(new Predicate<SysResources>() {
                    @Override
                    public boolean test(SysResources resource) {
                        return resource.getClassify().equals(ResourceType.BUTTON.toString()) && !StringUtils.isEmpty(resource.getParentId()) && menu.getResourceId().equals(resource.getParentId());
                    }
                }).collect(Collectors.toList()).forEach(button -> {     //按钮

                    JSONObject itemButton = new JSONObject();
                    String style = button.getStyle().replace("layui-btn-sm", "layui-btn-xs");
                    itemButton.put("style", style);
                    itemButton.put("icon", button.getIcon());
                    itemButton.put("title", button.getTitle());
                    itemButton.put("resourceId", button.getResourceId());

                    buttonArray.add(itemButton);
                });
                itemMenu.put("nodes", buttonArray);
                menuArray.add(itemMenu);
            });
            itemCatalog.put("nodes", menuArray);
            resources.add(itemCatalog);
        });
        result.setData(resources);
        return result;
    }

    @GetMapping("view")
    public ReturnResult view(String roleId) {
        ReturnResult result = new ReturnResult();
        QueryWrapper<SysRoleAuthority> query = new QueryWrapper<>();
        query.lambda().eq(SysRoleAuthority::getRoleId, roleId);
        List<SysRoleAuthority> list = sysRoleAuthorityServiceImpl.list(query);
        result.setData(list);
        return result;
    }

    @PostMapping("save")
    public ReturnResult save(String roleId,@RequestParam List<String> resources) {
        ReturnResult result = new ReturnResult();

        boolean b = sysRoleAuthorityServiceImpl.saveAuthority(roleId, resources);
        if (b){
            result.setMessage("配置完成");
        }else{
            result.setMessage("配置异常");
            result.rollback();
        }
        return result;
    }
}
