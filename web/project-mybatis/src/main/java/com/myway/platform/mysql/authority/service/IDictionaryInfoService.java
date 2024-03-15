package com.myway.platform.mysql.authority.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.myway.platform.mysql.authority.entity.DictionaryInfo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 字典表 服务类
 * </p>
 *
 */
public interface IDictionaryInfoService extends IService<DictionaryInfo> {

    IPage<Map<String, Object>> getDictionaryListByPage(IPage<DictionaryInfo> page, DictionaryInfo record);

    List<Map<String, Object>> getDictionaryByClassify(String classify);

    boolean saveOrUpdateDictionary(DictionaryInfo record);

}
