package com.jkm.hss.admin.service.impl;

import com.jkm.hss.admin.dao.DataDictionaryDao;
import com.jkm.hss.admin.entity.DataDictionary;
import com.jkm.hss.admin.service.DataDictionaryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by xingliujie on 2017/2/22.
 */
@Slf4j
@Service
public class DataDictionaryServiceImpl implements DataDictionaryService{
    @Autowired
    private DataDictionaryDao dataDictionaryDao;
    /**
     * 新增
     *
     * @param dataDictionary
     */
    @Override
    public void insert(DataDictionary dataDictionary) {
        dataDictionaryDao.insert(dataDictionary);
    }

    /**
     * 根据类型查询列表
     *
     * @param dictType
     * @return
     */
    @Override
    public List<DataDictionary> selectAllByType(String dictType) {
        return dataDictionaryDao.selectAllByType(dictType);
    }

    /**
     * 根据类型查询列表
     *
     * @param dictType
     * @param dictValue
     * @return
     */
    @Override
    public String selectDictNameByDictTypeAndDictValue(String dictType, String dictValue) {
        return dataDictionaryDao.selectDictNameByDictTypeAndDictValue(dictType,dictValue);
    }
}
