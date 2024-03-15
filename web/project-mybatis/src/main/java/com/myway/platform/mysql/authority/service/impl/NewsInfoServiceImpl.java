package com.myway.platform.mysql.authority.service.impl;

import com.myway.platform.mysql.authority.entity.NewsInfo;
import com.myway.platform.mysql.authority.mapper.NewsInfoDao;
import com.myway.platform.mysql.authority.service.INewsInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 新闻资讯表 服务实现类
 * </p>
 *
 */
@Service
public class NewsInfoServiceImpl extends ServiceImpl<NewsInfoDao, NewsInfo> implements INewsInfoService {

}
