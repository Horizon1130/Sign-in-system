package com.myway.platform.mysql.authority.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myway.platform.mysql.authority.entity.SysResources;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * <p>
 * 系统资源表 Mapper 接口
 * </p>
 *
 */
public interface SysResourcesDao extends BaseMapper<SysResources> {

    IPage<Map<String, Object>> getResources(Page page, @Param("record") SysResources record);

    IPage<Map<String, Object>> getResourceButtons(Page page, @Param("record") SysResources record);
}
