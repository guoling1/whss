package com.jkm.hss.admin.service;

import com.jkm.hss.admin.entity.DataDictionary;

import java.util.List;

/**
 * Created by xingliujie on 2017/2/22.
 */
public interface DataDictionaryService {
    /**
     * 新增
     * @param dataDictionary
     */
    void insert(DataDictionary dataDictionary);

    /**
     * 根据类型查询列表
     * @param dictType
     * @return
     */
    List<DataDictionary> selectAllByType(String dictType);
}
