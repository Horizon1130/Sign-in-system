package com.myway.platform.api;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myway.platform.controller.BaseController;
import com.myway.platform.mysql.authority.entity.*;
import com.myway.platform.mysql.authority.service.*;
import com.myway.platform.utils.Datetime;
import com.myway.platform.utils.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/course/")
public class AppCourseController extends BaseController {

    @Autowired
    ICourseInfoService courseInfoServiceImpl;
    @Autowired
    ISysUserBasicInfoService sysUserBasicInfoServiceImpl;
    @Autowired
    IDictionaryInfoService dictionaryInfoServiceImpl;
    @Autowired
    ICourseSignService courseSignServiceImpl;

    @PostMapping("list")
    public ReturnResult list(Integer page) {
        ReturnResult result = new ReturnResult();

        String sysUserId = getSessionSysUser().getSysUserId();
        SysUserBasicInfo sysUserBasic = sysUserBasicInfoServiceImpl.getById(sysUserId);

        Page<CourseInfo> iPage = new Page<>(page, 20);

        QueryWrapper<CourseInfo> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<CourseInfo> lambda = queryWrapper.lambda();
        if (sysUserBasic.getType().equals(0)) {
            lambda.eq(CourseInfo::getClassId, sysUserBasic.getPositionId());
        } else {
            lambda.eq(CourseInfo::getSysUserId, sysUserId);
        }
        lambda.orderByAsc(CourseInfo::getEndTime);

        IPage<CourseInfo> records = courseInfoServiceImpl.page(iPage, queryWrapper);

        List<JSONObject> list = new ArrayList<>();

        if (!CollectionUtils.isEmpty(records.getRecords())) {
            for (CourseInfo courseInfo : records.getRecords()) {
                JSONObject item = new JSONObject();
                item.put("id", courseInfo.getId());
                item.put("title", courseInfo.getTitle());
                item.put("address", "签到地址：" + courseInfo.getAddress() + "范围" + courseInfo.getRangeVal() + "公里内");
                item.put("lat", courseInfo.getLat());
                item.put("lng", courseInfo.getLng());
                item.put("range", courseInfo.getRangeVal());
                item.put("time", "截止时间：" + new Datetime(courseInfo.getEndTime()).toString("yyyy-MM-dd"));
                item.put("statusVal", "未签到");
                item.put("status", 0);

                QueryWrapper<CourseSign> courseSignQueryWrapper = new QueryWrapper<>();
                if (sysUserBasic.getType().equals(1)) {
                    item.put("isTeacher", true);
                    courseSignQueryWrapper.lambda().eq(CourseSign::getCourseId, courseInfo.getId());
                    int count = courseSignServiceImpl.count(courseSignQueryWrapper);
                    item.put("statusVal", "签到" + count + "人");
                } else {
                    courseSignQueryWrapper.lambda().eq(CourseSign::getSysUserId, sysUserId).eq(CourseSign::getCourseId, courseInfo.getId());
                    CourseSign one = courseSignServiceImpl.getOne(courseSignQueryWrapper);

                    if (one != null) {
                        item.put("statusVal", one.getNormal().equals(0) ? "外勤签到" : "正常打卡");
                        item.put("status", 1);
                        item.put("signId", one.getId());
                    }
                }

                list.add(item);
            }
        }
        result.set("list", list);
        return result;
    }

    @PostMapping("view")
    public ReturnResult view(Integer courseId) {
        ReturnResult result = new ReturnResult();

        JSONObject item = new JSONObject();

        CourseInfo courseInfo = courseInfoServiceImpl.getById(courseId);
        if (courseInfo != null) {
            SysUserBasicInfo teacherInfo = sysUserBasicInfoServiceImpl.getById(courseInfo.getSysUserId());
            if (teacherInfo != null) {
                item.put("id", teacherInfo.getSysUserId());
                item.put("nice", teacherInfo.getNice());
                item.put("face", doMain + teacherInfo.getFace());
                DictionaryInfo dictionaryInfo = dictionaryInfoServiceImpl.getById(teacherInfo.getPositionId());
                if (dictionaryInfo != null) {
                    item.put("position", dictionaryInfo.getTitle());
                }
            }
        }
        result.set("item", item);
        return result;
    }

    @PostMapping("save")
    public ReturnResult save(CourseInfo record) {
        ReturnResult result = new ReturnResult();

        record.setSysUserId(getSessionSysUser().getSysUserId());
        record.setCreateTime(LocalDateTime.now());

        courseInfoServiceImpl.saveOrUpdate(record);
        result.setMessage("发布成功");
        return result;
    }

    @PostMapping("sign")
    public ReturnResult sign(CourseSign record) {
        ReturnResult result = new ReturnResult();

        CourseInfo courseInfo = courseInfoServiceImpl.getById(record.getCourseId());

        Datetime nowTime = new Datetime();
        Datetime endTime = new Datetime(courseInfo.getEndTime());
        if (nowTime.getTimeStamp() > endTime.getTimeStamp()) {
            result.setErrorMsg("签到时间已截止");
            result.rollback();
            return result;
        }

        record.setSysUserId(getSessionSysUser().getSysUserId());
        record.setCreateTime(LocalDateTime.now());

        courseSignServiceImpl.saveOrUpdate(record);
        result.setMessage("签到完成");
        return result;
    }
}
