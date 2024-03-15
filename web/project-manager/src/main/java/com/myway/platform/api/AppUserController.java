package com.myway.platform.api;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.myway.platform.controller.BaseController;
import com.myway.platform.mysql.authority.entity.SysUserBasicInfo;
import com.myway.platform.mysql.authority.entity.SysUserInfo;
import com.myway.platform.mysql.authority.service.ISysUserBasicInfoService;
import com.myway.platform.mysql.authority.service.ISysUserInfoService;
import com.myway.platform.utils.Datetime;
import com.myway.platform.utils.FileUtil;
import com.myway.platform.utils.MD5Util;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/user/")
public class AppUserController extends BaseController {

    @Autowired
    ISysUserInfoService sysUserInfoServiceImpl;
    @Autowired
    ISysUserBasicInfoService sysUserBasicInfoServiceImpl;

    @PostMapping("update/password")
    public ReturnResult updatePwd(String oldPwd, String newPwd) {
        ReturnResult result = new ReturnResult();

        String sysUserId = getSessionSysUser().getSysUserId();

        QueryWrapper<SysUserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().select(SysUserInfo::getSysUserId).eq(SysUserInfo::getSysUserId, sysUserId)
                .eq(SysUserInfo::getPassword, MD5Util.MD5Encode(oldPwd, "utf-8"));
        int count = sysUserInfoServiceImpl.count(queryWrapper);
        if (count < 1) {
            result.setErrorMsg("原密码不正确");
            result.rollback();
            return result;
        }
        SysUserInfo record = new SysUserInfo();
        record.setSysUserId(sysUserId);
        record.setPassword(MD5Util.MD5Encode(newPwd, "utf-8"));
        sysUserInfoServiceImpl.updateById(record);
        result.setMessage("修改成功");
        return result;
    }

    @SneakyThrows
    @PostMapping("update/detail")
    public ReturnResult updateDetail(@RequestParam(required = false, value = "image") MultipartFile image, SysUserBasicInfo record) {
        ReturnResult result = new ReturnResult();

        if (image != null) {
            FileUtil util = new FileUtil(image, filePath, String.valueOf(System.currentTimeMillis()));
            record.setFace(util.saveFile());
        }
        record.setSysUserId(getSessionSysUser().getSysUserId());
        sysUserBasicInfoServiceImpl.updateById(record);
        result.setMessage("保存完成");
        return result;
    }

    @PostMapping("detail")
    public ReturnResult detail() {
        ReturnResult result = new ReturnResult();
        String sysUserId = getSessionSysUser().getSysUserId();

        SysUserInfo sysUserInfo = sysUserInfoServiceImpl.getById(sysUserId);
        SysUserBasicInfo sysUserBasicInfo = sysUserBasicInfoServiceImpl.getById(getSessionSysUser().getSysUserId());
        if (sysUserBasicInfo != null && sysUserInfo != null) {
            JSONObject item = new JSONObject();
            item.put("nice", !StringUtils.isEmpty(sysUserBasicInfo.getNice()) ? sysUserBasicInfo.getNice(): "昵称：未完善");
            item.put("account", "账号：" + sysUserInfo.getAccount());
            item.put("face", doMain + sysUserBasicInfo.getFace());
            result.setData(item);
        }
        return result;
    }

    @PostMapping("detail/view")
    public ReturnResult detailView() {
        ReturnResult result = new ReturnResult();
        String sysUserId = getSessionSysUser().getSysUserId();

        SysUserInfo sysUserInfo = sysUserInfoServiceImpl.getById(sysUserId);
        SysUserBasicInfo sysUserBasicInfo = sysUserBasicInfoServiceImpl.getById(getSessionSysUser().getSysUserId());
        if (sysUserBasicInfo != null && sysUserInfo != null) {
            JSONObject item = new JSONObject();
            item.put("nice", !StringUtils.isEmpty(sysUserBasicInfo.getNice()) ? sysUserBasicInfo.getNice(): "昵称：未完善");
            item.put("face", doMain + sysUserBasicInfo.getFace());
            item.put("sex", sysUserBasicInfo.getSex());
            if (sysUserBasicInfo.getBirthday() != null ) {
                item.put("birthday", new Datetime(sysUserBasicInfo.getBirthday()).toString("yyyy-MM-dd"));
            }
            item.put("phone", sysUserBasicInfo.getPhone());
            item.put("email", sysUserBasicInfo.getEmail());
            item.put("account", sysUserInfo.getAccount());

            result.setData(item);
        }
        return result;
    }

    @PostMapping("logout")
    public ReturnResult logout() {
        ReturnResult result = new ReturnResult();
        SecurityUtils.getSubject().logout();
        return result;
    }
}
