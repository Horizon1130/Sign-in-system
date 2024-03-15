package com.myway.platform.mysql.authority.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myway.platform.mysql.authority.entity.DictionaryInfo;
import com.myway.platform.mysql.authority.mapper.DictionaryInfoDao;
import com.myway.platform.mysql.authority.service.IDictionaryInfoService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 字典表 服务实现类
 * </p>
 *
 */
@Service
public class DictionaryInfoServiceImpl extends ServiceImpl<DictionaryInfoDao, DictionaryInfo> implements IDictionaryInfoService {

    @Override
    public IPage<Map<String, Object>> getDictionaryListByPage(IPage<DictionaryInfo> page, DictionaryInfo record) {
        return getBaseMapper().getDictionaryListByPage(page, record);
    }

    @Override
    public List<Map<String, Object>> getDictionaryByClassify(String classify) {
        QueryWrapper<DictionaryInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().select(DictionaryInfo::getDictionaryId, DictionaryInfo::getTitle,DictionaryInfo::getParentId,DictionaryInfo::getLevel)
                .eq(DictionaryInfo::getClassify, classify)
                .eq(DictionaryInfo::getStatus, 1);
        List<Map<String, Object>> list = listMaps(queryWrapper);
        return list;
    }

    @Override
    public boolean saveOrUpdateDictionary(DictionaryInfo record) {
        return this.saveOrUpdate(record);
    }

}
