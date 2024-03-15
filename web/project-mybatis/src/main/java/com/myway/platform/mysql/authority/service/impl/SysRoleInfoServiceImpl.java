package com.myway.platform.mysql.authority.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myway.platform.mysql.authority.entity.SysRoleInfo;
import com.myway.platform.mysql.authority.entity.SysUserInfo;
import com.myway.platform.mysql.authority.mapper.SysRoleInfoDao;
import com.myway.platform.mysql.authority.service.ISysRoleInfoService;
import com.myway.platform.mysql.authority.service.ISysUserInfoService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 系统用户角色表 服务实现类
 * </p>
 *
 */
@Service
public class SysRoleInfoServiceImpl extends ServiceImpl<SysRoleInfoDao, SysRoleInfo> implements ISysRoleInfoService {

    @Resource
    ISysUserInfoService sysUserInfoServiceImpl;

    @Override
    public String getRoleNameById(String roleId) {
        SysRoleInfo roleInfo = this.getById(roleId);
        if (roleInfo != null) {
            return roleInfo.getTitle();
        }
        return null;
    }
}
