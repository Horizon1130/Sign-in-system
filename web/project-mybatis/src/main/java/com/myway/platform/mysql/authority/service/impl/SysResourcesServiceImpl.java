package com.myway.platform.mysql.authority.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myway.platform.mysql.authority.entity.SysResources;
import com.myway.platform.mysql.authority.mapper.SysResourcesDao;
import com.myway.platform.mysql.authority.service.ISysResourcesService;
import com.myway.platform.type.ResourceType;
import com.myway.platform.utils.Datetime;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 系统资源表 服务实现类
 * </p>
 *
 */
@Service
public class SysResourcesServiceImpl extends ServiceImpl<SysResourcesDao, SysResources> implements ISysResourcesService {

    @Override
    public List<JSONObject> getResourcesAll() {
        QueryWrapper<SysResources> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().orderByAsc(SysResources::getSort);

        List<SysResources> list = list(queryWrapper);

        //目录
        List<SysResources> catalogs = list.stream().filter(catalog -> ResourceType.CATALOG.toString().equals(catalog.getClassify())).collect(Collectors.toList());
        //菜单
        List<SysResources> menus = list.stream().filter(menu -> ResourceType.MENU.toString().equals(menu.getClassify())).collect(Collectors.toList());
        //按钮
        List<SysResources> buttons = list.stream().filter(menu -> ResourceType.BUTTON.toString().equals(menu.getClassify())).collect(Collectors.toList());

        List<JSONObject> resources = new ArrayList<>();
        catalogs.forEach(catalogItem -> {
            JSONObject catalog = new JSONObject();
            catalog.put("id", catalogItem.getResourceId());
            catalog.put("name", catalogItem.getTitle());
            catalog.put("classify", catalogItem.getClassify());
            catalog.put("sort", catalogItem.getSort());
            catalog.put("visible", catalogItem.getVisible());
            catalog.put("status", catalogItem.getStatus());
            catalog.put("lastUpdateTime", new Datetime(catalogItem.getLastUpdateTime()).toString("yyyy-MM-dd HH:mm"));

            List<JSONObject> menuChildren = new ArrayList<>();
            menus.stream().filter(menu -> !StringUtils.isEmpty(menu.getParentId()) && menu.getParentId().equals(catalogItem.getResourceId()))
                    .collect(Collectors.toList()).forEach(menuItem -> {
                JSONObject menu = new JSONObject();
                menu.put("id", menuItem.getResourceId());
                menu.put("name", menuItem.getTitle());
                menu.put("url", menuItem.getUrl());
                menu.put("perms", menuItem.getPerms());
                menu.put("icon", menuItem.getIcon());
                menu.put("classify", menuItem.getClassify());
                menu.put("sort", menuItem.getSort());
                menu.put("visible", menuItem.getVisible());
                menu.put("status", menuItem.getStatus());
                menu.put("lastUpdateTime", new Datetime(menuItem.getLastUpdateTime()).toString("yyyy-MM-dd HH:mm"));
                List<JSONObject> buttonChildren = new ArrayList<>();

                buttons.stream().filter(button -> !StringUtils.isEmpty(button.getParentId()) && button.getParentId().equals(menuItem.getResourceId()))
                        .collect(Collectors.toList()).forEach(buttonItem -> {
                    JSONObject button = new JSONObject();
                    button.put("id", buttonItem.getResourceId());
                    button.put("name", buttonItem.getTitle());
                    button.put("url", buttonItem.getUrl());
                    button.put("perms", buttonItem.getPerms());
                    button.put("icon", buttonItem.getIcon());
                    button.put("style", buttonItem.getStyle());
                    button.put("classify", buttonItem.getClassify());
                    button.put("sort", buttonItem.getSort());
                    button.put("visible", buttonItem.getVisible());
                    button.put("status", buttonItem.getStatus());
                    button.put("lastUpdateTime", new Datetime(buttonItem.getLastUpdateTime()).toString("yyyy-MM-dd HH:mm"));
                    buttonChildren.add(button);
                });

                if (!CollectionUtils.isEmpty(buttonChildren)) {
                    menu.put("children", buttonChildren);
                }
                menuChildren.add(menu);
            });
            if (!CollectionUtils.isEmpty(menuChildren)) {
                catalog.put("children", menuChildren);
            }

            resources.add(catalog);
        });

        return resources;
    }
}
