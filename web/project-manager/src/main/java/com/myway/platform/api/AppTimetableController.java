package com.myway.platform.api;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.myway.platform.controller.BaseController;
import com.myway.platform.mysql.authority.entity.StudentClass;
import com.myway.platform.mysql.authority.entity.SysUserBasicInfo;
import com.myway.platform.mysql.authority.entity.TimetableInfo;
import com.myway.platform.mysql.authority.service.IStudentClassService;
import com.myway.platform.mysql.authority.service.ISysUserBasicInfoService;
import com.myway.platform.mysql.authority.service.ITimetableInfoService;
import com.myway.platform.utils.Datetime;
import com.myway.platform.utils.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/timetable/")
public class AppTimetableController extends BaseController {

    @Autowired
    ITimetableInfoService timetableInfoServiceImpl;
    @Autowired
    ISysUserBasicInfoService sysUserBasicInfoServiceImpl;

    @PostMapping("save")
    public ReturnResult save(TimetableInfo record) {
        ReturnResult result = new ReturnResult();

        String sysUserId = getSessionSysUser().getSysUserId();

        Integer week = (record.getWeek() - 1) * (24 * 7);
        Integer day = (record.getDay() - 1) * 24;

        String weekStart = TimeUtils.getWeekStart();
        Datetime beginTime = Datetime.parse(weekStart, "yyyy-MM-dd HH:mm:ss");
        beginTime.addHours(week);
        beginTime.addHours(day);

        QueryWrapper<TimetableInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(TimetableInfo::getCreateTime, beginTime.getFullDatetime()).eq(TimetableInfo::getSysUserId, sysUserId).eq(TimetableInfo::getStart, record.getStart());
        TimetableInfo one = timetableInfoServiceImpl.getOne(queryWrapper);

        if (one != null) {
            record.setId(one.getId());
        }

        record.setCreateTime(beginTime.getFullDatetime());
        record.setSysUserId(sysUserId);
        timetableInfoServiceImpl.saveOrUpdate(record);
        result.setMessage("新增成功");
        return result;
    }

    @PostMapping("list")
    public ReturnResult list() {
        ReturnResult result = new ReturnResult();

        String sysUserId = getSessionSysUser().getSysUserId();
        SysUserBasicInfo sysUser = sysUserBasicInfoServiceImpl.getById(sysUserId);

        QueryWrapper<TimetableInfo> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<TimetableInfo> lambda = queryWrapper.lambda();
        if (sysUser.getType().equals(0)) {
            lambda.eq(TimetableInfo::getClassId, sysUser.getPositionId());
        } else {
            lambda.eq(TimetableInfo::getSysUserId, sysUserId);
        }

        List<TimetableInfo> records = timetableInfoServiceImpl.list(queryWrapper);

        List<JSONObject> list = new ArrayList<>();
        for (TimetableInfo record : records) {
            JSONObject item = new JSONObject();
            item.put("id", record.getId());
            item.put("content", record.getTitle());
            item.put("day", record.getDay());
            item.put("start", record.getStart());
            item.put("step", record.getStep());
            item.put("week", record.getWeek());
            item.put("status", 2);
            Datetime datetime = new Datetime();
            if (new Datetime(record.getCreateTime()).getTimeStamp() < datetime.getTimeStamp()) {
                item.put("status", -1);
            }

            list.add(item);
        }
        result.set("list", list);
        return result;
    }

    @PostMapping("delete")
    public ReturnResult delete(Integer day, Integer start, Integer week) {
        ReturnResult result = new ReturnResult();

        String sysUserId = getSessionSysUser().getSysUserId();

        Integer weekValue = (week - 1) * (24 * 7);
        Integer dayValue = (day - 1) * 24;

        String weekStart = TimeUtils.getWeekStart();
        Datetime beginTime = Datetime.parse(weekStart, "yyyy-MM-dd HH:mm:ss");
        beginTime.addHours(weekValue);
        beginTime.addHours(dayValue);

        QueryWrapper<TimetableInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(TimetableInfo::getCreateTime, beginTime.getFullDatetime()).eq(TimetableInfo::getSysUserId, sysUserId).eq(TimetableInfo::getStart, start);
        timetableInfoServiceImpl.remove(queryWrapper);
        result.setMessage("删除成功");
        return result;
    }
}
