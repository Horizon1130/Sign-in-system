package com.myway.platform.mysql.authority.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.myway.platform.mysql.authority.entity.SysResources;
import com.myway.platform.mysql.authority.entity.SysRoleAuthority;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 系统用户权限表 服务类
 * </p>
 *
 */
public interface ISysRoleAuthorityService extends IService<SysRoleAuthority> {

    /**
     * 获取角色资源权限标识列表
     * @param roleId
     * @return
     */
    Set<String> getResourcePermsListByRoleId(String roleId);

    /**
     * 获取
     * @param roles
     * @return
     */
    Set<SysResources> getResourcesByRoles(List<String> roles);

    Set<SysResources> getResourcesByRole(String role);

    List<JSONObject> getResourceByRoles(List<String> roles);

    Set<SysResources> getResourceButtonsByResourceIdAndRoleId(String resourceId, String roleId);

    List<JSONObject> getResourceButtonsByResourceIdAndRoleIds(String resourceId, List<String> roleId);

    boolean saveAuthority(String roleId, List<String> resources);

}
