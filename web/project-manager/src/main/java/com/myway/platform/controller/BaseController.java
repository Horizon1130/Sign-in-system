package com.myway.platform.controller;

import com.myway.platform.handler.exception.NotLoginException;
import com.myway.platform.mysql.authority.entity.SysUserInfo;
import com.myway.platform.mysql.authority.service.ISysUserInfoService;
import lombok.SneakyThrows;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class BaseController {

    @Value("${myway.system.title}")
    protected String systemTitle;
    @Value("${myway.system.copyright}")
    protected String systemCopyRight;
    @Value("${myway.system.domain}")
    protected String doMain;

    protected String filePath = "images/";

    @Autowired
    @Qualifier("sysUserInfoServiceImpl")
    protected ISysUserInfoService sysUserInfoService;

    public HttpServletRequest request() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request;
    }

    /**
     * 获取系统用户会话信息
     *
     * @return
     */
    @SneakyThrows
    protected SysUserInfo getSessionSysUser() {
        SysUserInfo principal = (SysUserInfo) SecurityUtils.getSubject().getPrincipal();
        if (principal == null) {
            throw new NotLoginException();
        }
        return principal;
    }

    protected List<String> getSessionSysUserRoles() {
        List<String> roles = new ArrayList<>();
        SysUserInfo sessionSysUser = getSessionSysUser();
        SysUserInfo sysUserBasicInfo = sysUserInfoService.getById(sessionSysUser.getSysUserId());

        String roleId = sysUserBasicInfo.getRoles();

        if (!StringUtils.isEmpty(roleId)) {
            roles.add(roleId);
        }
        return roles;
    }

}
