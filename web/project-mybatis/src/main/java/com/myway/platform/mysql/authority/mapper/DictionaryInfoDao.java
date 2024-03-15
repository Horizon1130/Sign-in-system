package com.myway.platform.mysql.authority.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.myway.platform.mysql.authority.entity.DictionaryInfo;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * <p>
 * 字典表 Mapper 接口
 * </p>
 *
 */
public interface DictionaryInfoDao extends BaseMapper<DictionaryInfo> {

    IPage<Map<String, Object>> getDictionaryListByPage(IPage<DictionaryInfo> page, @Param("record") DictionaryInfo record);
}
