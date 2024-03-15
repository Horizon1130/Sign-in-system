package com.myway.platform.mysql.authority.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myway.platform.mysql.authority.entity.SysResources;
import com.myway.platform.mysql.authority.entity.SysRoleAuthority;
import com.myway.platform.mysql.authority.mapper.SysRoleAuthorityDao;
import com.myway.platform.mysql.authority.service.ISysResourcesService;
import com.myway.platform.mysql.authority.service.ISysRoleAuthorityService;
import com.myway.platform.type.ResourceType;
import com.myway.platform.utils.UUIDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * <p>
 * 系统用户权限表 服务实现类
 * </p>
 *
 */
@Service
public class SysRoleAuthorityServiceImpl extends ServiceImpl<SysRoleAuthorityDao, SysRoleAuthority> implements ISysRoleAuthorityService {

    @Autowired
    ISysResourcesService sysResourcesServiceImpl;

    @Cacheable(value = "resources_role_perms", key = "#roleId")
    @Override
    public Set<String> getResourcePermsListByRoleId(String roleId) {
        QueryWrapper<SysRoleAuthority> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysRoleAuthority::getRoleId, roleId);
        List<SysRoleAuthority> sysRoleAuthorities = getBaseMapper().selectList(queryWrapper);
        Set<String> resourceIdArray = new HashSet<>();
        sysRoleAuthorities.forEach(sysRoleAuthority -> {
            resourceIdArray.add(sysRoleAuthority.getResourceId());
        });
        Set<String> permsArray = new HashSet<>();
        if (!CollectionUtils.isEmpty(resourceIdArray)) {
            QueryWrapper<SysResources> resourcesQueryWrapper = new QueryWrapper<>();
            resourcesQueryWrapper.lambda().in(SysResources::getResourceId, resourceIdArray);
            List<SysResources> resources = sysResourcesServiceImpl.list(resourcesQueryWrapper);

            resources.forEach(sysResources -> {
                if (!StringUtils.isEmpty(sysResources.getPerms())) {
                    permsArray.add(sysResources.getPerms());
                }
            });
        }
        return permsArray;
    }

    @Override
    public Set<SysResources> getResourcesByRole(String role) {
        Set<SysResources> resourcesByRoleId = getBaseMapper().getResourcesByRole(role);
        return resourcesByRoleId;
    }

    @Override
    public Set<SysResources> getResourcesByRoles(List<String> roles) {
        Set<SysResources> resources = new HashSet<>();
        if (!CollectionUtils.isEmpty(roles)) {
            roles.forEach(roleId -> {
                Set<SysResources> resourcesByRole = getResourcesByRole(roleId);
                resources.addAll(resourcesByRole);
            });
        }
        return resources;
    }

    @Cacheable(value = "resources_role", key = "#roles[0]")
    @Override
    public List<JSONObject> getResourceByRoles(List<String> roles) {
        List<JSONObject> list = new ArrayList<>();
        Set<SysResources> resourcesByRoles = getResourcesByRoles(roles);
        if (!CollectionUtils.isEmpty(resourcesByRoles)) {
            //遍历目录
            resourcesByRoles.stream().filter(new Predicate<SysResources>() {
                @Override
                public boolean test(SysResources resources) {
                    return resources.getClassify().equals(ResourceType.CATALOG.toString());
                }
            }).collect(Collectors.toList()).forEach(item -> {
                JSONObject first = new JSONObject();
                first.put("resourceId", item.getResourceId());
                first.put("href", "");
                first.put("sort", item.getSort());
                first.put("title", item.getTitle());
                first.put("spread", true);
                first.put("classify", ResourceType.CATALOG.toString());
                first.put("group", item.getGrouping());

                List<JSONObject> childrens = new ArrayList<>();

                resourcesByRoles.stream().sorted((x,y) -> {
                    return x.getSort().compareTo(y.getSort());
                }).filter(new Predicate<SysResources>() {
                    @Override
                    public boolean test(SysResources resources) {
                        return resources.getParentId().equals(item.getResourceId()) && resources.getClassify().equals(ResourceType.MENU.toString());
                    }
                }).collect(Collectors.toList()).forEach(c -> {
                    JSONObject children = new JSONObject();
                    children.put("resourceId", c.getResourceId());
                    children.put("classify", ResourceType.MENU.toString());
                    children.put("href", c.getUrl());
                    children.put("sort", c.getSort());
                    children.put("title", c.getTitle());
                    children.put("spread", false);
                    childrens.add(children);
                });
                first.put("children", childrens);

                list.add(first);
            });
        }
        return list;
    }

    @Override
    public Set<SysResources> getResourceButtonsByResourceIdAndRoleId(String resourceId, String roleId) {
        return getBaseMapper().getResourceButtonsByResourceIdAndRoleId(resourceId, roleId);
    }

    @Cacheable(value = "resource_buttons_role", key = "#resourceId + '_' + #roleIds[0]", unless = "#result == null")
    @Override
    public List<JSONObject> getResourceButtonsByResourceIdAndRoleIds(String resourceId, List<String> roleIds) {
        Set<SysResources> buttons = new HashSet<>();
        roleIds.forEach(roleId -> {
            Set<SysResources> resourceButtonsByResourceIdAndRoleId = getResourceButtonsByResourceIdAndRoleId(resourceId, roleId);
            buttons.addAll(resourceButtonsByResourceIdAndRoleId);
        });
        Set<SysResources> sortResources = new TreeSet<>((o1, o2) -> o1.getSort().compareTo(o2.getSort()));
        sortResources.addAll(buttons);
        List<JSONObject> list = new ArrayList<>();
        sortResources.forEach(item -> {
            JSONObject button = new JSONObject();
            button.put("resourceId", item.getResourceId());
            button.put("title", item.getTitle());
            button.put("url", item.getUrl());
            button.put("style", item.getStyle());
            button.put("parentId", item.getParentId());
            button.put("classify", item.getClassify());
            button.put("sort", item.getSort());
            button.put("icon", item.getIcon());
            list.add(button);
        });
        return list;
    }

    @Caching(evict = {
            @CacheEvict(value = "resources_role", key = "#roleId", allEntries = true, beforeInvocation = false),
            @CacheEvict(value = "resources_role_perms", key = "#roleId", allEntries = true, beforeInvocation = false),
            @CacheEvict(value = "resource_buttons_role", key = "#resources[0] + '_' + #roleId", allEntries = true, beforeInvocation = false)
    })
    @Override
    public boolean saveAuthority(String roleId, List<String> resources) {
        QueryWrapper<SysRoleAuthority> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysRoleAuthority::getRoleId, roleId);
        this.remove(queryWrapper);

        List<SysRoleAuthority> entitys = new ArrayList<>();

        if (!CollectionUtils.isEmpty(resources)) {
            resources.forEach(resourceId -> {
                SysRoleAuthority authority = new SysRoleAuthority();
                String authorityId = UUIDUtils.getEncryUUID();
                authority.setResourceId(resourceId);
                authority.setRoleId(roleId);
                authority.setAuthorityId(authorityId);

                entitys.add(authority);
            });
            this.saveBatch(entitys);
            return true;
        }else if(CollectionUtils.isEmpty(resources)){
            return true;
        }
        return false;
    }

}
