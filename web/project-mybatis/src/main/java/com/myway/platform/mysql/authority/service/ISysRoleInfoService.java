package com.myway.platform.mysql.authority.service;

import com.myway.platform.mysql.authority.entity.SysRoleInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 系统用户角色表 服务类
 * </p>
 *
 */
public interface ISysRoleInfoService extends IService<SysRoleInfo> {

    String getRoleNameById(String roleId);

}
