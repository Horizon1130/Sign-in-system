package com.myway.platform.controller.v1.restful.resources;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myway.platform.api.ReturnResult;
import com.myway.platform.controller.BaseController;
import com.myway.platform.mysql.authority.entity.SysUserBasicInfo;
import com.myway.platform.mysql.authority.entity.SysUserInfo;
import com.myway.platform.mysql.authority.service.ISysUserBasicInfoService;
import com.myway.platform.mysql.authority.service.ISysUserInfoService;
import com.myway.platform.utils.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1/resources/staff")
public class StaffController extends BaseController {

    @Autowired
    ISysUserInfoService sysUserInfoServiceImpl;
    @Autowired
    ISysUserBasicInfoService sysUserBasicInfoServiceImpl;

    @PostMapping("/list")
    public ReturnResult list(long current, long size, SysUserBasicInfo recrd) {
        ReturnResult result = new ReturnResult();
        IPage<SysUserBasicInfo> page = new Page(current, size);

        QueryWrapper<SysUserBasicInfo> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<SysUserBasicInfo> lambda = queryWrapper.lambda();
        if (!StringUtils.isEmpty(recrd.getNice())) {
            lambda.eq(SysUserBasicInfo::getNice, recrd.getNice());
        }
        if (!StringUtils.isEmpty(recrd.getPhone())) {
            lambda.eq(SysUserBasicInfo::getPhone, recrd.getPhone());
        }
        if (!StringUtils.isEmpty(recrd.getEmail())) {
            lambda.eq(SysUserBasicInfo::getEmail, recrd.getEmail());
        }

        lambda.eq(SysUserBasicInfo::getType, 0).orderByDesc(SysUserBasicInfo::getLastUpdateTime);

        IPage<SysUserBasicInfo> iPage = sysUserBasicInfoServiceImpl.page(page, queryWrapper);

        List<JSONObject> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(iPage.getRecords())) {
            for (SysUserBasicInfo sysUserBasicInfo : iPage.getRecords()) {
                JSONObject item = new JSONObject();
                item.put("sysUserId", sysUserBasicInfo.getSysUserId());
                item.put("nice", sysUserBasicInfo.getNice());
                item.put("phone", sysUserBasicInfo.getPhone());
                item.put("email", sysUserBasicInfo.getEmail());
                item.put("createTime", sysUserBasicInfo.getCreateTime());
                item.put("face", sysUserBasicInfo.getFace());
                item.put("address", sysUserBasicInfo.getAddress());

                SysUserInfo sysUserInfo = sysUserInfoServiceImpl.getById(sysUserBasicInfo.getSysUserId());
                if (sysUserInfo != null) {
                    item.put("account", sysUserInfo.getAccount());
                }

                list.add(item);
            }
        }

        result.setListView(list, iPage.getTotal());
        return result;
    }

    @SneakyThrows
    @PostMapping("/save")
    public ReturnResult save(@RequestParam(required = false) MultipartFile file, String account, SysUserBasicInfo record) {
        ReturnResult result = new ReturnResult();

        if (file != null) {
            FileUtil util = new FileUtil(file, filePath, String.valueOf(System.currentTimeMillis()));
            record.setFace(util.saveFile());
        }
        String password = RandomUtil.randomString(10);

        Datetime datetime = new Datetime();

        String sysUserId = UUIDUtils.getEncryUUID();
        SysUserInfo user = new SysUserInfo();

        user.setAccount(account);
        //user.setPassword(MD5Util.MD5Encode(password, "utf-8"));
        user.setPassword("e10adc3949ba59abbe56e057f20f883e");
        user.setSysUserId(sysUserId);
        user.setStatus(1);
        user.setCreateTime(datetime.getFullDatetime());
        user.setCreateDate(datetime.getDate());
        user.setLastUpdateTime(datetime.getFullDatetime());
        user.setLastUpdateDate(datetime.getDate());

        record.setCode(account);
        record.setType(0);
        record.setSysUserId(sysUserId);
        record.setCreateDate(datetime.getDate());
        record.setCreateTime(datetime.getFullDatetime());
        record.setLastUpdateTime(datetime.getFullDatetime());
        record.setLastUpdateDate(datetime.getDate());

        sysUserInfoServiceImpl.saveOrUpdate(user);
        sysUserBasicInfoServiceImpl.saveOrUpdate(record);

        result.setMessage(String.format("新增成功，学生密码：%s", "123456"));
        return result;
    }

    @SneakyThrows
    @PostMapping("/modify")
    public ReturnResult modify(@RequestParam(required = false) MultipartFile file, SysUserBasicInfo record) {
        ReturnResult result = new ReturnResult();

        if (file != null) {
            FileUtil util = new FileUtil(file, filePath, String.valueOf(System.currentTimeMillis()));
            record.setFace(util.saveFile());
        }

        Datetime datetime = new Datetime();

        record.setLastUpdateTime(datetime.getFullDatetime());
        record.setLastUpdateDate(datetime.getDate());

        sysUserBasicInfoServiceImpl.updateById(record);
        result.setMessage("更新完成");
        return result;
    }

    @DeleteMapping("/delete")
    public ReturnResult delete(@RequestParam List<String> userIds) {
        ReturnResult result = new ReturnResult();
        sysUserInfoServiceImpl.removeByIds(userIds);
        sysUserBasicInfoServiceImpl.removeByIds(userIds);
        result.setMessage("删除成功");
        return result;
    }
}
