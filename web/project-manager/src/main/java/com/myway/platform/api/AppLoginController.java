package com.myway.platform.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.myway.platform.controller.BaseController;
import com.myway.platform.mysql.authority.entity.SysUserBasicInfo;
import com.myway.platform.mysql.authority.entity.SysUserInfo;
import com.myway.platform.mysql.authority.service.ISysUserBasicInfoService;
import com.myway.platform.mysql.authority.service.ISysUserInfoService;
import com.myway.platform.utils.Datetime;
import com.myway.platform.utils.MD5Util;
import com.myway.platform.utils.RandomUtil;
import com.myway.platform.utils.UUIDUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/")
public class AppLoginController extends BaseController {

    @Autowired
    ISysUserInfoService sysUserInfoServiceImpl;
    @Autowired
    ISysUserBasicInfoService sysUserBasicInfoServiceImpl;

    @PostMapping("login")
    public ReturnResult login(String account, String password, String token) {
        ReturnResult result = new ReturnResult();

        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(account, MD5Util.MD5Encode(password, "utf-8"));
        subject.login(usernamePasswordToken);
        String accessToken = SecurityUtils.getSubject().getSession().getId().toString();
        result.set("token", accessToken);

        SysUserInfo sessionSysUser = getSessionSysUser();

        if (sessionSysUser != null ){
            SysUserBasicInfo sysUserBasicInfo = sysUserBasicInfoServiceImpl.getById(sessionSysUser.getSysUserId());
            result.set("type", sysUserBasicInfo.getType());

            if (!StringUtils.isEmpty(token)) {
                SysUserBasicInfo record = new SysUserBasicInfo();
                record.setSysUserId(sessionSysUser.getSysUserId());
                record.setToken(token);

                sysUserBasicInfoServiceImpl.updateById(record);
            }
        }
        return result;
    }

    @PostMapping("register")
    public ReturnResult register(String account, String password) {
        ReturnResult result = new ReturnResult();

        QueryWrapper<SysUserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().select(SysUserInfo::getSysUserId).eq(SysUserInfo::getAccount, account);
        int count = sysUserInfoServiceImpl.count(queryWrapper);

        if (count > 0) {
            result.setErrorMsg("账号已被注册");
            result.rollback();
            return result;
        }

        SysUserBasicInfo record = new SysUserBasicInfo();
        SysUserInfo user = new SysUserInfo();

        Datetime datetime = new Datetime();
        String sysUserId = UUIDUtils.getEncryUUID();

        user.setAccount(account);
        user.setPassword(MD5Util.MD5Encode(password, "utf-8"));
        user.setSysUserId(sysUserId);
        user.setStatus(1);
        user.setCreateTime(datetime.getFullDatetime());
        user.setCreateDate(datetime.getDate());
        user.setLastUpdateTime(datetime.getFullDatetime());
        user.setLastUpdateDate(datetime.getDate());

        record.setType(0);
        record.setSysUserId(sysUserId);
        record.setCreateDate(datetime.getDate());
        record.setCreateTime(datetime.getFullDatetime());
        record.setLastUpdateTime(datetime.getFullDatetime());
        record.setLastUpdateDate(datetime.getDate());

        sysUserInfoServiceImpl.saveOrUpdate(user);
        sysUserBasicInfoServiceImpl.saveOrUpdate(record);

        result.setMessage("注册成功");
        return result;
    }

    @PostMapping("logout")
    public ReturnResult logout() {
        ReturnResult result = new ReturnResult();
        SecurityUtils.getSubject().logout();
        return result;
    }
}
