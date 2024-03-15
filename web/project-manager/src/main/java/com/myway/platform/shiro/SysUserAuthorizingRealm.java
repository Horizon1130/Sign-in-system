package com.myway.platform.shiro;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.myway.platform.mysql.authority.entity.SysUserInfo;
import com.myway.platform.mysql.authority.service.ISysRoleAuthorityService;
import com.myway.platform.mysql.authority.service.ISysUserInfoService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Set;

@Configuration
public class SysUserAuthorizingRealm extends AuthorizingRealm {

    @Resource
    @Lazy
    ISysUserInfoService sysUserInfoServiceImpl;

    @Resource
    @Lazy
    ISysRoleAuthorityService sysRoleAuthorityServiceImpl;

    /**
     * 授权(验证权限时调用)
     * 获取用户权限集合
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo authorInfo = new SimpleAuthorizationInfo();

        SysUserInfo sysUserInfo = (SysUserInfo) principalCollection.getPrimaryPrincipal();
        if (sysUserInfo == null) {
            throw new UnknownAccountException("账号不存在");
        }

        String roleId = sysUserInfo.getRoles();

        if(!StringUtils.isEmpty(roleId)){
            Set<String> resourcePermsListByRoleId = sysRoleAuthorityServiceImpl.getResourcePermsListByRoleId(roleId);
            authorInfo.setStringPermissions(resourcePermsListByRoleId);
        }
        return authorInfo;
    }

    /**
     * 认证(登录时调用)
     * 验证用户登录
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;

        String account = token.getUsername();

        QueryWrapper<SysUserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysUserInfo::getAccount, account);
        SysUserInfo userInfo = sysUserInfoServiceImpl.getOne(queryWrapper);

        if (userInfo == null) {
            throw new UnknownAccountException();//未知的账号
        } else if (userInfo.getStatus().equals(0)) {
            throw new DisabledAccountException();//禁用的账号
        }
        AuthenticationInfo authc = new SimpleAuthenticationInfo(userInfo, userInfo.getPassword(), getName());
        clearCachedAuthorizationInfo(authc.getPrincipals());//清空之前的授权信息
        return authc;
    }

}
