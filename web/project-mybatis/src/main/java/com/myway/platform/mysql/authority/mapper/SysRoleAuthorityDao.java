package com.myway.platform.mysql.authority.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.myway.platform.mysql.authority.entity.SysResources;
import com.myway.platform.mysql.authority.entity.SysRoleAuthority;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

/**
 * <p>
 * 系统用户权限表 Mapper 接口
 * </p>
 *
 */
public interface SysRoleAuthorityDao extends BaseMapper<SysRoleAuthority> {

    Set<SysResources> getResourcesByRole(@Param("role") String role);

    Set<SysResources> getResourceButtonsByResourceIdAndRoleId(@Param("resourceId") String resourceId, @Param("roleId") String roleId);
}
