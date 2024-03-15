package com.myway.platform.controller.v1.restful.resources;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myway.platform.api.ReturnResult;
import com.myway.platform.controller.BaseController;
import com.myway.platform.mysql.authority.entity.NewsInfo;
import com.myway.platform.mysql.authority.service.INewsInfoService;
import com.myway.platform.utils.Datetime;
import com.myway.platform.utils.UUIDUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1/resources/news")
public class NewsController extends BaseController {

    @Autowired
    INewsInfoService newsInfoServiceImpl;

    @PostMapping("/list")
    public ReturnResult list(long current, long size, NewsInfo record) {
        ReturnResult result = new ReturnResult();
        Page<NewsInfo> page = new Page<>(current, size);
        QueryWrapper<NewsInfo> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<NewsInfo> lambda = queryWrapper.lambda();
        if (!StringUtils.isEmpty(record.getTitle())){
            lambda.like(NewsInfo::getTitle, record.getTitle());
        }
        if (!StringUtils.isEmpty(record.getStatus())){
            lambda.like(NewsInfo::getStatus, record.getStatus());
        }
        lambda.orderByDesc(NewsInfo::getCreateTime);

        IPage<NewsInfo> resources = newsInfoServiceImpl.page(page, queryWrapper);
        result.setListView(resources.getRecords(), resources.getTotal());
        return result;
    }

    @PostMapping("/save")
    public ReturnResult save(NewsInfo record) {
        ReturnResult result = new ReturnResult();
        String newsId = UUIDUtils.getEncryUUID();

        Datetime datetime = new Datetime();
        record.setNewsId(newsId);
        record.setCreateDate(datetime.getDate());
        record.setCreateTime(datetime.getFullDatetime());

        newsInfoServiceImpl.saveOrUpdate(record);
        result.setMessage("新增成功");
        return result;
    }

    @PostMapping("/modify")
    public ReturnResult modify(NewsInfo record) {
        ReturnResult result = new ReturnResult();

        Datetime datetime = new Datetime();
        record.setCreateDate(datetime.getDate());
        record.setCreateTime(datetime.getFullDatetime());
        newsInfoServiceImpl.saveOrUpdate(record);
        result.setMessage("更新成功");
        return result;
    }

    @DeleteMapping("/delete")
    public ReturnResult delete(@RequestParam List<String> ids) {
        ReturnResult result = new ReturnResult();
        newsInfoServiceImpl.removeByIds(ids);
        result.setMessage("删除成功");
        return result;
    }

}
