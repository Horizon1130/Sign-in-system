package com.myway.platform.api;

import com.alibaba.fastjson.JSON;
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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/sign/")
public class AppSignController extends BaseController {

    @Autowired
    ICourseSignService courseSignServiceImpl;
    @Autowired
    ISysUserBasicInfoService sysUserBasicInfoServiceImpl;
    @Autowired
    ICourseInfoService courseInfoServiceImpl;
    @Autowired
    IStudentClassService studentClassServiceImpl;

    @PostMapping("list")
    public ReturnResult list(Integer courseId, Integer page) {
        ReturnResult result = new ReturnResult();

        String sysUserId = getSessionSysUser().getSysUserId();

        Page<CourseSign> iPage = new Page<>(page, 20);

        QueryWrapper<CourseSign> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CourseSign::getCourseId, courseId).orderByAsc(CourseSign::getCreateTime);

        IPage<CourseSign> records = courseSignServiceImpl.page(iPage, queryWrapper);

        List<JSONObject> list = new ArrayList<>();

        for (CourseSign record : records.getRecords()) {
            JSONObject item = new JSONObject();
            item.put("signId", record.getId());
            item.put("time", TimeUtils.getFriendlyTimeSpanByNow(new Datetime(record.getCreateTime()).getTimeStamp()));

            QueryWrapper<CourseSign> courseSignQueryWrapper = new QueryWrapper<>();
            courseSignQueryWrapper.lambda().eq(CourseSign::getSysUserId, record.getSysUserId()).eq(CourseSign::getCourseId, record.getCourseId());
            CourseSign one = courseSignServiceImpl.getOne(courseSignQueryWrapper);
            item.put("statusVal", "未签到");
            item.put("status", 0);
            if (one != null) {
                item.put("statusVal", one.getNormal().equals(0) ? "外勤签到" : "正常打卡");
                item.put("status", 1);
            }

            SysUserBasicInfo sysUserBasic = sysUserBasicInfoServiceImpl.getById(record.getSysUserId());
            if (sysUserBasic != null) {
                item.put("id", sysUserBasic.getSysUserId());
                item.put("nice", sysUserBasic.getNice());
                item.put("face", doMain + sysUserBasic.getFace());
            }

            list.add(item);
        }
        result.set("list", list);
        return result;
    }

    @PostMapping("view")
    public ReturnResult view(Integer id) {
        ReturnResult result = new ReturnResult();

        JSONObject item = new JSONObject();

        CourseSign courseSign = courseSignServiceImpl.getById(id);
        if (courseSign != null) {
            CourseInfo courseInfo = courseInfoServiceImpl.getById(courseSign.getCourseId());
            if (courseInfo != null) {
                item.put("courseLat", courseInfo.getLat());
                item.put("courseLng", courseInfo.getLng());
                item.put("range", courseInfo.getRangeVal());
            }
            item.put("lat", courseSign.getLat());
            item.put("lng", courseSign.getLng());
        }
        result.set("item", item);
        return result;
    }

    @PostMapping("statistics")
    public ReturnResult statistics(String sysUserId) {
        ReturnResult result = new ReturnResult();

        JSONObject item = new JSONObject();

        if (StringUtils.isEmpty(sysUserId)) {
            sysUserId = getSessionSysUser().getSysUserId();
        }

        SysUserBasicInfo sysUserBasicInfo = sysUserBasicInfoServiceImpl.getById(sysUserId);
        if (sysUserBasicInfo != null) {
            item.put("nice", sysUserBasicInfo.getNice());
            item.put("face", doMain + sysUserBasicInfo.getFace());
            StudentClass classInfo = studentClassServiceImpl.getById(sysUserBasicInfo.getPositionId());
            if (classInfo != null) {
                item.put("className", classInfo.getTitle());
            }
        }

        QueryWrapper<CourseInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().select(CourseInfo::getId).eq(CourseInfo::getClassId, sysUserBasicInfo.getPositionId());

        List<Object> courseIds = courseInfoServiceImpl.listObjs(queryWrapper);

        QueryWrapper<CourseSign> signQueryWrapper = new QueryWrapper<>();
        signQueryWrapper.lambda().in(CourseSign::getCourseId, courseIds).eq(CourseSign::getSysUserId, sysUserId).orderByAsc(CourseSign::getCreateTime);
        List<CourseSign> courseSigns = courseSignServiceImpl.list(signQueryWrapper);

        //课程总数
        float courseTotal = courseIds.size();
        //签到的数量
        float signTotal = courseSigns.size();
        //缺少的数量
        float lackTotal = courseTotal - signTotal;


        item.put("signTotal", new BigDecimal(signTotal / courseTotal * 100).setScale(0, BigDecimal.ROUND_HALF_UP).intValue());
        item.put("lackTotal", new BigDecimal(lackTotal / courseTotal * 100).setScale(0, BigDecimal.ROUND_HALF_UP).intValue());

        //正常签到数
        float normalTotal = courseSigns.stream().filter(p -> p.getNormal().equals(1)).collect(Collectors.toList()).size();
        //外勤签到数
        float lateTotal = courseSigns.stream().filter(p -> p.getNormal().equals(0)).collect(Collectors.toList()).size();

        item.put("normalTotal", 0);
        item.put("lateTotal", 0);
        if (lateTotal > 0) {
            item.put("lateTotal", new BigDecimal(lateTotal / signTotal * 100).setScale(0, BigDecimal.ROUND_HALF_UP).intValue());
        }
        if (normalTotal > 0) {
            item.put("normalTotal", new BigDecimal(normalTotal / signTotal * 100).setScale(0, BigDecimal.ROUND_HALF_UP).intValue());
        }

        result.set("item", item);
        return result;
    }
}

