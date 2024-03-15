package com.myway.platform.mysql.authority.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.myway.platform.mysql.authority.entity.SysResources;

import java.util.List;

/**
 * <p>
 * 系统资源表 服务类
 * </p>
 *
 */
public interface ISysResourcesService extends IService<SysResources> {

    List<JSONObject> getResourcesAll();

}
