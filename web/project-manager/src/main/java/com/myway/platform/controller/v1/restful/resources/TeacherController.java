package com.myway.platform.controller.v1.restful.resources;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myway.platform.api.ReturnResult;
import com.myway.platform.controller.BaseController;
import com.myway.platform.mysql.authority.entity.DictionaryInfo;
import com.myway.platform.mysql.authority.entity.SysUserBasicInfo;
import com.myway.platform.mysql.authority.entity.SysUserInfo;
import com.myway.platform.mysql.authority.service.IDictionaryInfoService;
import com.myway.platform.mysql.authority.service.ISysUserBasicInfoService;
import com.myway.platform.mysql.authority.service.ISysUserInfoService;
import com.myway.platform.utils.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1/resources/teacher")
public class TeacherController extends BaseController {

    @Autowired
    ISysUserInfoService sysUserInfoServiceImpl;
    @Autowired
    ISysUserBasicInfoService sysUserBasicInfoServiceImpl;
    @Autowired
    IDictionaryInfoService dictionaryInfoServiceImpl;

    @PostMapping("/list")
    public ReturnResult list(long current, long size, SysUserBasicInfo record) {
        ReturnResult result = new ReturnResult();
        Page<SysUserBasicInfo> page = new Page<>(current, size);
        QueryWrapper<SysUserBasicInfo> queryWrapper = new QueryWrapper<>();

        LambdaQueryWrapper<SysUserBasicInfo> lambda = queryWrapper.lambda();
        if (!StringUtils.isEmpty(record.getNice())) {
            lambda.like(SysUserBasicInfo::getNice, record.getNice());
        }
        if (!StringUtils.isEmpty(record.getCode())) {
            lambda.eq(SysUserBasicInfo::getCode, record.getCode());
        }
        lambda.eq(SysUserBasicInfo::getType, 1);
        lambda.orderByDesc(SysUserBasicInfo::getCreateTime);

        IPage<SysUserBasicInfo> resources = sysUserBasicInfoServiceImpl.page(page, queryWrapper);
        JSONArray list = new JSONArray();

        for (SysUserBasicInfo menu : resources.getRecords()) {
            JSONObject object = new JSONObject();
            object.put("id", menu.getSysUserId());
            object.put("nice", menu.getNice());
            object.put("phone", menu.getPhone());
            object.put("code", menu.getCode());
            object.put("email", menu.getEmail());
            object.put("createTime", menu.getCreateTime());

            DictionaryInfo dictionaryInfo = dictionaryInfoServiceImpl.getById(menu.getPositionId());
            if (dictionaryInfo != null) {
                object.put("position", dictionaryInfo.getTitle());
            }

            list.add(object);
        }

        result.setListView(list, resources.getTotal());
        return result;
    }

    @SneakyThrows
    @PostMapping("/save")
    public ReturnResult save(@RequestParam(required = false) MultipartFile file, SysUserBasicInfo record, SysUserInfo user) {
        ReturnResult result = new ReturnResult();

        String fileName = "";
        if (file != null) {
            FileUtil util = new FileUtil(file, filePath, String.valueOf(System.currentTimeMillis()));
            fileName = util.saveFile();
        }

        if (!"".equals(fileName) && fileName != null) {
            record.setFace(fileName);
        }
        String password = RandomUtil.randomString(10);

        Datetime datetime = new Datetime();
        String sysUserId = UUIDUtils.getEncryUUID();

        user.setAccount(record.getCode());
        //user.setPassword(MD5Util.MD5Encode(password, "utf-8"));
        user.setPassword("e10adc3949ba59abbe56e057f20f883e");
        user.setSysUserId(sysUserId);
        user.setStatus(1);
        user.setCreateTime(datetime.getFullDatetime());
        user.setCreateDate(datetime.getDate());
        user.setLastUpdateTime(datetime.getFullDatetime());
        user.setLastUpdateDate(datetime.getDate());

        record.setType(1);
        record.setSysUserId(sysUserId);
        record.setCreateDate(datetime.getDate());
        record.setCreateTime(datetime.getFullDatetime());
        record.setLastUpdateTime(datetime.getFullDatetime());
        record.setLastUpdateDate(datetime.getDate());

        sysUserInfoServiceImpl.saveOrUpdate(user);
        sysUserBasicInfoServiceImpl.saveOrUpdate(record);
        result.setMessage(String.format("新增成功，教师密码：%s", "123456"));
        return result;
    }

    @SneakyThrows
    @PostMapping("/modify")
    public ReturnResult modify(@RequestParam(required = false) MultipartFile file, SysUserBasicInfo record, SysUserInfo user) {
        ReturnResult result = new ReturnResult();

        String fileName = "";
        if (file != null) {
            FileUtil util = new FileUtil(file, filePath, String.valueOf(System.currentTimeMillis()));
            fileName = util.saveFile();
        }

        if (!"".equals(fileName) && fileName != null) {
            record.setFace(fileName);
        }
        String password = RandomUtil.randomString(10);

        Datetime datetime = new Datetime();

        user.setAccount(record.getCode());
        //user.setPassword(MD5Util.MD5Encode(password, "utf-8"));
        user.setStatus(1);
        user.setCreateTime(datetime.getFullDatetime());
        user.setCreateDate(datetime.getDate());
        user.setLastUpdateTime(datetime.getFullDatetime());
        user.setLastUpdateDate(datetime.getDate());

        record.setCreateDate(datetime.getDate());
        record.setCreateTime(datetime.getFullDatetime());
        record.setLastUpdateTime(datetime.getFullDatetime());
        record.setLastUpdateDate(datetime.getDate());

        sysUserInfoServiceImpl.saveOrUpdate(user);
        sysUserBasicInfoServiceImpl.saveOrUpdate(record);
        result.setMessage("新增成功");
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
