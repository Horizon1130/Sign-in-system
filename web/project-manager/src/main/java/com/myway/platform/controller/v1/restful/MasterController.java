package com.myway.platform.controller.v1.restful;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.myway.platform.api.ReturnResult;
import com.myway.platform.controller.BaseController;
import com.myway.platform.mysql.authority.entity.SysUserBasicInfo;
import com.myway.platform.mysql.authority.entity.SysUserInfo;
import com.myway.platform.mysql.authority.service.ISysUserBasicInfoService;
import com.myway.platform.mysql.authority.service.ISysUserInfoService;
import com.myway.platform.utils.FileUtil;
import com.myway.platform.utils.MD5Util;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/v1/")
public class MasterController extends BaseController {

    @Autowired
    ISysUserInfoService sysUserInfoServiceImpl;
    @Autowired
    ISysUserBasicInfoService sysUserBasicInfoServiceImpl;

    @GetMapping("modify/user")
    public ReturnResult getUserInfo(String userId) {
        ReturnResult result = new ReturnResult();
        SysUserInfo sessionSysUser = getSessionSysUser();
        if (StringUtils.isEmpty(userId)) {
            userId = sessionSysUser.getSysUserId();
        }
        SysUserBasicInfo sysUserBasicInfo = sysUserBasicInfoServiceImpl.getById(userId);
        SysUserInfo sysUserInfo = sysUserInfoServiceImpl.getById(sysUserBasicInfo.getSysUserId());

        JSONObject item = new JSONObject();
        item.put("sysUserId", sysUserBasicInfo.getSysUserId());
        item.put("nice", sysUserBasicInfo.getNice());
        item.put("phone", sysUserBasicInfo.getPhone());
        item.put("email", sysUserBasicInfo.getEmail());
        item.put("address", sysUserBasicInfo.getAddress());
        item.put("face", sysUserBasicInfo.getFace());
        item.put("account", sysUserInfo.getAccount());
        item.put("roles", sysUserInfo.getRoles());
        item.put("status", sysUserInfo.getStatus());
        item.put("courseId", sysUserBasicInfo.getCourseId());

        result.setData(item);
        return result;
    }

    @SneakyThrows
    @PostMapping("modify/user")
    public ReturnResult doModifyUser(SysUserBasicInfo record, @RequestParam(required = false) MultipartFile file) {
        ReturnResult result = new ReturnResult();
        SysUserInfo sessionSysUser = getSessionSysUser();

        if (file != null) {
            FileUtil util = new FileUtil(file, filePath, String.valueOf(System.currentTimeMillis()));
            record.setFace(util.saveFile());
        }

        record.setSysUserId(sessionSysUser.getSysUserId());
        result.set("nice", record.getNice());
        result.set("face", record.getFace());

        boolean b = sysUserBasicInfoServiceImpl.updateById(record);
        if (b) {
            result.setMessage("更新完成");
        } else {
            result.setErrorMsg("更新失败");
            result.rollback();
        }
        return result;
    }

    @PostMapping("modify/password")
    public ReturnResult doModifyPassword(String oldPwd, String newPwd) {
        ReturnResult result = new ReturnResult();
        SysUserInfo sessionSysUser = getSessionSysUser();

        oldPwd = MD5Util.MD5Encode(oldPwd, "utf-8");
        QueryWrapper<SysUserInfo> query = new QueryWrapper<>();
        query.lambda().eq(SysUserInfo::getSysUserId, sessionSysUser.getSysUserId()).eq(SysUserInfo::getPassword, oldPwd);
        SysUserInfo sysUserInfo = sysUserInfoServiceImpl.getOne(query);
        if (sysUserInfo != null) {
            newPwd = MD5Util.MD5Encode(newPwd, "utf-8");
            UpdateWrapper<SysUserInfo> record = new UpdateWrapper<>();
            record.lambda().eq(SysUserInfo::getSysUserId, sessionSysUser.getSysUserId()).set(SysUserInfo::getPassword, newPwd);
            sysUserInfoServiceImpl.update(record);
            result.setMessage("更新成功");
        } else {
            result.setErrorMsg("账号原密码输入不正确");
            result.rollback();
        }
        return result;
    }

}
