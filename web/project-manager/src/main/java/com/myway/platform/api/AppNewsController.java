package com.myway.platform.api;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myway.platform.controller.BaseController;
import com.myway.platform.mysql.authority.entity.DictionaryInfo;
import com.myway.platform.mysql.authority.entity.NewsInfo;
import com.myway.platform.mysql.authority.service.IDictionaryInfoService;
import com.myway.platform.mysql.authority.service.INewsInfoService;
import com.myway.platform.utils.Datetime;
import com.myway.platform.utils.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/news/")
public class AppNewsController extends BaseController {

    @Autowired
    INewsInfoService newsInfoServiceImpl;
    @Autowired
    IDictionaryInfoService dictionaryInfoServiceImpl;

    @PostMapping("list")
    public ReturnResult list(Integer page) {
        ReturnResult result = new ReturnResult();

        Page<NewsInfo> iPage = new Page<>(page, 20);

        QueryWrapper<NewsInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(NewsInfo::getStatus, 1).orderByDesc(NewsInfo::getCreateTime);

        IPage<NewsInfo> records = newsInfoServiceImpl.page(iPage, queryWrapper);

        List<JSONObject> list = new ArrayList<>();

        if (!CollectionUtils.isEmpty(records.getRecords())) {
            for (NewsInfo newsInfo : records.getRecords()) {
                JSONObject item = new JSONObject();
                item.put("newsId", newsInfo.getNewsId());
                item.put("title", newsInfo.getTitle());
                item.put("content", newsInfo.getContent());
                item.put("time", TimeUtils.getFriendlyTimeSpanByNow(new Datetime(newsInfo.getCreateTime()).getTimeStamp()));

                DictionaryInfo classify = dictionaryInfoServiceImpl.getById(newsInfo.getType());
                item.put("classify", "未知");
                if (classify != null) {
                    item.put("classify", classify.getTitle());
                }

                if (!StringUtils.isEmpty(newsInfo.getContent())) {
                    Document html = Jsoup.parse(newsInfo.getContent());
                    Elements img = html.select("img");

                    if (!img.isEmpty()) {
                        String image = img.first().attr("src");
                        item.put("cover", image);
                    }
                }
                list.add(item);
            }
        }
        result.set("list", list);
        return result;
    }
}
